package Database_access;

import Model.Attraction;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    public void AttractionDAO_Add(String nom, int nb_places_tot, String categorie, Boolean ouvert, int prix, Time heure_ouverture, Time heure_fermeture, Time fin_inscription){

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
                System.out.println("Erreur lors de l'insertion : " + e.getMessage());
            }
        }
    }

    @FXML
    public List<Attraction> AttractionDAO_GetAll() {
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
                System.out.println("Erreur lors de la recherche : " + e.getMessage());
            }
        } else {
            System.out.println("Connexion à la base de données échouée.");
        }

        // For debug

        /*
        for (Attraction a : attractions) {
            System.out.println(a);
        }

         */

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
    }

    @FXML
    public List<Attraction> AttractionDAO_Get() {
        List<Attraction> attractions = new ArrayList<>();
        Connection connection = Database_connection.connect();

        if (inputtext_attraction == null) {
            System.out.println("Le champ inputtext_attraction n'est pas initialisé !");
            //return attractions;
        }

        Toggle selectedToggle = Select_option.getSelectedToggle();

        if (selectedToggle != null) {
            String selectedText = ((RadioButton) selectedToggle).getText();

            switch (selectedText) {
                case "Nom":
                    search_criteria = 0;
                    break;
                case "Tarif":
                    search_criteria = 1;
                    break;
                case "Ouvert":
                    search_criteria = 2;
                    break;
                case "Places disponibles":
                    search_criteria = 3;
                    break;
                case "Catégorie":
                    search_criteria = 4;
                    break;
                default:
                    System.out.println("Critère inconnu");
                    return attractions;
            }

            // Lire le texte de recherche uniquement si le critère le nécessite
            String search = "";
            if (search_criteria == 0 || search_criteria == 1 || search_criteria == 3 || search_criteria == 4) {
                if (inputtext_attraction == null) {
                    System.out.println("Champ de recherche non initialisé.");
                    return attractions;
                }
                search = inputtext_attraction.getText();
            }


            if (search_criteria == 0) {
                if (connection != null) {
                    try {
                        String sql = "SELECT * FROM attraction WHERE Nom LIKE ?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setString(1, "%" + search + "%");

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

                            Attraction attraction = new Attraction(id, nom, nbPlacesTot, nbPlacesDispo, tarif, ouvert,
                                    categorie, heureOuverture, heureFermeture, heureFinInscription);

                            attractions.add(attraction);
                        }

                        resultSet.close();
                        statement.close();
                        connection.close();

                    } catch (Exception e) {
                        System.out.println("Erreur lors de la recherche : " + e.getMessage());
                    }
                }
            }

            else if (search_criteria == 1) {
                if (connection != null) {
                    try {
                        String sql = "SELECT * FROM attraction WHERE Tarif <  ?";
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

                            Attraction attraction = new Attraction(id, nom, nbPlacesTot, nbPlacesDispo, tarif, ouvert,
                                    categorie, heureOuverture, heureFermeture, heureFinInscription);

                            attractions.add(attraction);
                        }

                        resultSet.close();
                        statement.close();
                        connection.close();

                    } catch (Exception e) {
                        System.out.println("Erreur lors de la recherche : " + e.getMessage());
                    }
                }
            }

            else if  (search_criteria == 2) {
                if (connection != null) {
                    try {
                        String sql = "SELECT * FROM attraction WHERE Ouvert = 1";
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

                            Attraction attraction = new Attraction(id, nom, nbPlacesTot, nbPlacesDispo, tarif, ouvert,
                                    categorie, heureOuverture, heureFermeture, heureFinInscription);

                            attractions.add(attraction);
                        }

                        resultSet.close();
                        statement.close();
                        connection.close();

                    } catch (Exception e) {
                        System.out.println("Erreur lors de la recherche : " + e.getMessage());
                    }
                }
            }

            else if (search_criteria == 3) {
                if (connection != null) {
                    try {
                        String sql = "SELECT * FROM attraction WHERE Nb_places_dispo >  ?";
                        PreparedStatement statement = connection.prepareStatement(sql);

                        statement.setInt(1, Integer.parseInt(search)); // ou catch NumberFormatException

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

                            Attraction attraction = new Attraction(id, nom, nbPlacesTot, nbPlacesDispo, tarif, ouvert,
                                    categorie, heureOuverture, heureFermeture, heureFinInscription);

                            attractions.add(attraction);
                        }

                        resultSet.close();
                        statement.close();
                        connection.close();

                    } catch (Exception e) {
                        System.out.println("Erreur lors de la recherche : " + e.getMessage());
                    }
                }
            }

            else if (search_criteria == 4) {
                if (connection != null) {
                    try {
                        String sql = "SELECT * FROM attraction WHERE Categorie LIKE  ?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setString(1, "%" + search + "%");

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

                            Attraction attraction = new Attraction(id, nom, nbPlacesTot, nbPlacesDispo, tarif, ouvert,
                                    categorie, heureOuverture, heureFermeture, heureFinInscription);

                            attractions.add(attraction);
                        }

                        resultSet.close();
                        statement.close();
                        connection.close();

                    } catch (Exception e) {
                        System.out.println("Erreur lors de la recherche : " + e.getMessage());
                    }
                }
            }



        }

        attractionTable.getItems().setAll(attractions);

        return attractions;
    }
}


