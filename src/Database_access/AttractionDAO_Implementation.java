package Database_access;

import Model.Attraction;
import Model.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Time;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AttractionDAO_Implementation {

    @FXML
    private TextField inputtext_attraction;

    @FXML
    private TableView<Attraction> attractionTable;
    @FXML
    private TableColumn<Attraction, Integer> colId;
    @FXML
    private TableColumn<Attraction, String> colNom;
    @FXML
    private TableColumn<Attraction, String> colPlaces;
    @FXML
    private TableColumn<Attraction, Double> colTarif;
    @FXML
    private TableColumn<Attraction, String> colOuvert;
    @FXML
    private TableColumn<Attraction, String> colCategorie;

    @FXML
    private TableColumn<Attraction, Void> colAction;

    @FXML
    private int search_criteria;

    @FXML
    private ToggleGroup Select_option;


    public AttractionDAO_Implementation() {
        // constructeur vide requis par FXMLLoader
    }

    @FXML
    public void AttractionDAO_Add(String nom, int nb_places_tot, String categorie, Boolean ouvert, int prix, Time heure_ouverture, Time heure_fermeture, Time fin_inscription) throws Exceptions_Database {

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {

                String sql = "INSERT INTO Attraction (Nom, Nb_places_tot, Nb_places_dispo, Ouvert, Tarif, Categorie, Heure_ouverture, Heure_fermeture, Heure_fin_inscription) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, nom);
                statement.setInt(2, nb_places_tot);
                statement.setInt(3, nb_places_tot);
                statement.setBoolean(4, ouvert);
                statement.setInt(5, prix);
                statement.setString(6, categorie);
                statement.setTime(7, heure_ouverture);
                statement.setTime(8, heure_fermeture);
                statement.setTime(9, fin_inscription);

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Insertion réussie !");
                }

                statement.close();
                connection.close();

            } catch (Exception e) {
                throw new Exceptions_Database("Erreur de insertion", e);
            }
        } else {
            throw new Exceptions_Database("La connexion à la base de données a échoué");
        }
    }

    @FXML
    public List<Attraction> AttractionDAO_GetAll() throws Exceptions_Database {
        List<Attraction> attractions = new ArrayList<>();
        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT * FROM attraction";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id_attraction");
                    String nom = resultSet.getString("Nom");
                    int nbPlacesTot = resultSet.getInt("Nb_places_tot");
                    int nbPlacesDispo = resultSet.getInt("Nb_places_dispo");
                    boolean ouvert = resultSet.getBoolean("Ouvert");
                    double tarif = resultSet.getDouble("Tarif");
                    String categorie = resultSet.getString("Categorie");
                    Time heureOuverture = resultSet.getTime("Heure_ouverture");
                    Time heureFermeture = resultSet.getTime("Heure_fermeture");
                    Time heureFinInscription = resultSet.getTime("Heure_fin_inscription");

                    Attraction attraction = new Attraction(id,
                            nom, nbPlacesTot, nbPlacesDispo, tarif, ouvert,
                            categorie, heureOuverture, heureFermeture, heureFinInscription
                    );
                    attractions.add(attraction);
                }

                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                throw new Exceptions_Database("Erreur lors de la recherche", e);
            }
        } else {
            throw new Exceptions_Database("La connexion à la base de données a échoué");
        }

        return attractions;
    }


    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colPlaces.setCellValueFactory(data -> {
            String places = data.getValue().getNb_places_dispo() + " / " + data.getValue().getNb_places_tot();
            return new SimpleStringProperty(places);
        });
        colTarif.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTarif()));
        colOuvert.setCellValueFactory(data -> {
            String ouvert = data.getValue().isOuvert() ? "Oui" : "Non";
            return new SimpleStringProperty(ouvert);
        });

        colCategorie.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCategorie()));

        attractionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colId.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        colNom.setMaxWidth(1f * Integer.MAX_VALUE * 17);
        colPlaces.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        colTarif.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        colOuvert.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        colCategorie.setMaxWidth(1f * Integer.MAX_VALUE * 23);


        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Reserver");

            {
                btn.setOnAction(event -> {
                    Attraction attraction = getTableView().getItems().get(getIndex());
                    Session.setUserBooking(attraction.getId());

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Book.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) inputtext_attraction.getScene().getWindow(); // Email est ton champ de login, donc il est déjà dans la fenêtre !
                        Scene scene = new Scene(root, 1920, 1080);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }


    @FXML
    public List<Attraction> AttractionDAO_Get() throws Exceptions_Database {
        List<Attraction> attractions = new ArrayList<>();

        if (inputtext_attraction == null) {
            throw new Exceptions_Database("Le champ inputtext n'est pas initialisé !");
        }

        Toggle selectedToggle = Select_option.getSelectedToggle();
        if (selectedToggle == null) {
            throw new Exceptions_Database("Aucun critère de recherche sélectionné !");
        }

        String selectedText = ((RadioButton) selectedToggle).getText();
        int search_criteria;
        switch (selectedText) {
            case "Nom" -> search_criteria = 0;
            case "Tarif" -> search_criteria = 1;
            case "Ouvert" -> search_criteria = 2;
            case "Places disponibles" -> search_criteria = 3;
            case "Catégorie" -> search_criteria = 4;
            default -> throw new Exceptions_Database("Critère de recherche inconnu !");
        }

        String search = inputtext_attraction.getText().trim();

        String sql;
        boolean needsParameter = true;

        switch (search_criteria) {
            case 0 -> sql = "SELECT * FROM attraction WHERE Nom LIKE ?";
            case 1 -> sql = "SELECT * FROM attraction WHERE Tarif < ?";
            case 2 -> {
                sql = "SELECT * FROM attraction WHERE Ouvert = 1";
                needsParameter = false;
            }
            case 3 -> sql = "SELECT * FROM attraction WHERE Nb_places_dispo > ?";
            case 4 -> sql = "SELECT * FROM attraction WHERE Categorie LIKE ?";
            default -> throw new Exceptions_Database("Critère non pris en charge !");
        }

        try (Connection connection = Database_connection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (connection == null) {
                throw new Exceptions_Database("Connexion à la base de données échouée.");
            }

            if (needsParameter) {
                switch (search_criteria) {
                    case 0, 4 -> statement.setString(1, "%" + search + "%");
                    case 1 -> {
                        try {
                            statement.setDouble(1, Double.parseDouble(search));
                        } catch (NumberFormatException e) {
                            throw new Exceptions_Database("Le tarif doit être un nombre valide.", e);
                        }
                    }
                    case 3 -> {
                        try {
                            statement.setInt(1, Integer.parseInt(search));
                        } catch (NumberFormatException e) {
                            throw new Exceptions_Database("Le nombre de places doit être un entier valide.", e);
                        }
                    }
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Attraction attraction = new Attraction(
                            resultSet.getInt("id_attraction"),
                            resultSet.getString("Nom"),
                            resultSet.getInt("Nb_places_tot"),
                            resultSet.getInt("Nb_places_dispo"),
                            resultSet.getDouble("Tarif"),
                            resultSet.getBoolean("Ouvert"),
                            resultSet.getString("Categorie"),
                            resultSet.getTime("Heure_ouverture"),
                            resultSet.getTime("Heure_fermeture"),
                            resultSet.getTime("Heure_fin_inscription")
                    );
                    attractions.add(attraction);
                }
            }

        } catch (SQLException e) {
            throw new Exceptions_Database("Erreur lors de la récupération des attractions.", e);
        }

        attractionTable.getItems().setAll(attractions);
        return attractions;
    }

    public void AttractionDAO_redirectMenu(ActionEvent event) {
        if (Session.getAdmin()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Admion_Template.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root, 1920, 1080);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Client_Template.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root, 1920, 1080);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}