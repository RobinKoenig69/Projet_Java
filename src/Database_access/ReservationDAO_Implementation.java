package Database_access;

import Model.Reservation;
import Model.Session;
import Model.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Controler.testGraphic.UserID;
import static Controler.testGraphic.UserName;
import static Encryption.MD5.hash;
import static javafx.application.Application.getUserAgentStylesheet;

public class ReservationDAO_Implementation {

    @FXML
    private TextArea ReservationInfo;

    @FXML
    private TextArea ordersummary;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private TextArea past_res;


    public ReservationDAO_Implementation() {

    }

    public int ReservationnDAO_GetPercentage(int id_attraction, String concerne) throws Exceptions_Database {

        int pourcentage = 1; // Valeur par défaut s'il n'y a pas de réduction

        Connection connection = Database_connection.connect();

        if (connection == null) {
            throw new Exceptions_Database("Connexion à la base de données échouée.");
        }

        try {
            String sql = "SELECT * FROM reduction WHERE id_attraction = ? AND Concerne = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id_attraction);
            statement.setString(2, concerne);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                pourcentage = resultSet.getInt("Pourcentage");

            } else {
                System.out.println("Aucune réduction trouvée pour attraction " + id_attraction + " et concerne = " + concerne);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            throw new Exceptions_Database("Erreur lors de la récupération des réductions.", e);
        }

        return pourcentage;
    }




    public void ReservationDAO_Add(LocalDateTime date_reservation, float prix, int id_attraction, int id_utilisateur) throws  Exceptions_Database {

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "INSERT INTO Attraction (Date_reservation, Prix, id_utilisateur, id_attraction) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);

                Timestamp timestamp = Timestamp.valueOf(date_reservation);
                statement.setTimestamp(1, timestamp);
                statement.setFloat(2, prix);
                statement.setInt(3, id_utilisateur);
                statement.setInt(4, id_attraction);

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Insertion réussie !");
                }

                statement.close();
                connection.close();

            } catch (Exception e) {
                throw new  Exceptions_Database("Erreur lors de l'insertion", e);
            }
        } else {
            throw new Exceptions_Database("La connexion à la base de données a échoué");
        }
    }

    @FXML
    public String getNomAttraction(int id_att) throws Exceptions_Database {

        String nom="";

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT * FROM attraction WHERE id_attraction = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1 , String.valueOf(id_att));
                ResultSet resultSet = statement.executeQuery();




                if (resultSet.next()) {
                    nom = resultSet.getString("Nom");
                } else {
                    System.out.println("Aucune attraction trouvée pour l'id : " + id_att);
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
        
        return nom;
    }

    public static float getAttractionPrice(int idAttraction) throws Exceptions_Database {
        float prix = 0.0f;

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT Tarif FROM attraction WHERE id_attraction = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, idAttraction);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    prix = resultSet.getInt("Tarif");
                }

            } catch (SQLException e) {
                throw new Exceptions_Database("Erreur lors de la récupération du prix de l'attraction.", e);
            }
        }

        return prix;
    }



    @FXML
    public void initialize() throws Exceptions_Database {
        List<Reservation> reservations = ReservationDAO_getInfo();

        if (!reservations.isEmpty()) {
            StringBuilder infos = new StringBuilder();
            StringBuilder infos_past = new StringBuilder();

            for (Reservation reservation : reservations) {
                infos.append(String.format(
                        "Code de réservation : %s\nDate : %s\nPrix : %s\nAttraction : %s\n\n",
                        reservation.getId_reservation(),
                        reservation.getDate_reservation(),
                        reservation.getPrix(),
                        getNomAttraction(reservation.getId_attraction())
                ));
                if(reservation.getDate_reservation().isBefore(LocalDateTime.now())){
                    infos_past.append(String.format(
                            "Code de réservation : %s\nDate : %s\nPrix : %s\nAttraction : %s\n\n",
                            reservation.getId_reservation(),
                            reservation.getDate_reservation(),
                            reservation.getPrix(),
                            getNomAttraction(reservation.getId_attraction())
                    ));
                }
            }

            if (ReservationInfo != null) {
                ReservationInfo.setText(infos.toString());
            }

            if(past_res!=null){
                past_res.setText(infos_past.toString());
            }


        } else {
            if (ReservationInfo != null){
                ReservationInfo.setText("Aucune réservation trouvée pour cet utilisateur.");
            }
        }


        if (Session.getCartvalue() != 0) {
            StringBuilder infos = new StringBuilder();
            StringBuilder infos_past = new StringBuilder();

            Session.setNbpersonnes(Session.getCartvalue());

            //System.out.println(ReservationnDAO_GetPercentage(Session.getUserBooking(), Session.getCategorie()));

            float pourcentage = ReservationnDAO_GetPercentage(Session.getUserBooking(), Session.getCategorie());
            float prixfinal = Session.getCartvalue()*(getAttractionPrice(Session.getUserBooking())) * (1 - pourcentage / 100f);

            System.out.println(prixfinal);

            Session.setCartvalue(prixfinal);

            //Session.setCartvalue(prixfinal);

            infos.append(String.format(
                    "Réservation pour : %s\nMontant : %.2f",
                    Session.getUserName(),
                    prixfinal
            ));

            if (ordersummary != null) {
                ordersummary.setText(infos.toString());
            }
            }
    }


    @FXML
    public List<Reservation> ReservationDAO_getInfo() throws Exceptions_Database{
        List<Reservation> reservations = new ArrayList<>();

        try (Connection connection = Database_connection.connect()) {
            if (connection != null) {
                String sql = "SELECT * FROM reservation WHERE id_utilisateur = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, UserID);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id_reservation");
                            Date sqlDate = resultSet.getDate("Date_reservation");
                            LocalDateTime date = sqlDate.toLocalDate().atStartOfDay(); // adapte si besoin
                            int prix = resultSet.getInt("Prix");
                            int idAttraction = resultSet.getInt("id_attraction");

                            reservations.add(new Reservation(id, date, prix, UserID, idAttraction));
                        }
                    }
                }
            }
            else {
                throw new Exceptions_Database("Connexion à la base de données échouée.");
            }
        } catch (SQLException e) {
            throw new Exceptions_Database("Erreur lors de la recherche", e);
        }

        return reservations;
    }

    @FXML
    public void ReservationDAO_Book(ActionEvent event) throws Exceptions_Database {
        int id_attraction = Session.getUserBooking();
        int id_user = Session.getUserID();

        int prix = 0;
        int nb_places_tot = 0;

        // Étape 1 : Récupérer le tarif et le nombre de places totales de l'attraction
        try (Connection connection = Database_connection.connect()) {
            String sql = "SELECT Nb_places_tot, Tarif FROM attraction WHERE id_attraction = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id_attraction);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        nb_places_tot = resultSet.getInt("Nb_places_tot");
                        prix = resultSet.getInt("Tarif");
                    } else {
                        throw new Exceptions_Database("Attraction non trouvée.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new Exceptions_Database("Erreur lors de la récupération des informations d'attraction", e);
        }

        // Étape 2 : Vérifier le nombre de réservations existantes pour la date choisie
        int nb_reservations_existantes = 0;
        Date dateChoisie = Session.getDatebooking();  // Doit être un Timestamp

        if (dateChoisie == null) {
            throw new Exceptions_Database("Date de réservation non définie.");
        }

        try (Connection connection = Database_connection.connect()) {
            String sql = "SELECT SUM(nb_personnes) FROM reservation WHERE id_attraction = ? AND DATE(Date_reservation) = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id_attraction);
                statement.setDate(2, new java.sql.Date(dateChoisie.getTime()));  // Pour matcher avec DATE() SQL
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        nb_reservations_existantes = resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new Exceptions_Database("Erreur lors de la vérification des réservations existantes", e);
        }


        int nbDemandes = (int) Session.getNbpersonnes();
        if (nbDemandes + nb_reservations_existantes > nb_places_tot * 24) {
            throw new Exceptions_Database("Nombre de places insuffisant pour cette date.");
        }

        // Étape 4 : Insérer la réservation
        try (Connection connection = Database_connection.connect()) {
            String sql = "INSERT INTO reservation (Date_reservation, Prix, id_utilisateur, id_attraction, nb_personnes) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                float prixTotal = Session.getCartvalue();  // prix déjà réduit

                statement.setDate(1, dateChoisie);
                statement.setFloat(2, prixTotal);
                statement.setInt(3, id_user);
                statement.setInt(4, id_attraction);
                statement.setInt(5, nbDemandes);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Réservation effectuée avec succès !");
                } else {
                    throw new Exceptions_Database("Échec de l'enregistrement de la réservation.");
                }
            }
        } catch (SQLException e) {
            throw new Exceptions_Database("Erreur lors de l'enregistrement de la réservation", e);
        }

        // Redirection vers le menu après confirmation
        ReservationDAO_redirectMenu(event);
    }


    @FXML
    public void ReservationDAO_redirectConfirm(ActionEvent event) {

        Session.setCartvalue(spinner.getValue());
        Session.setDatebooking(Date.valueOf(datePicker.getValue()));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Confirm_booking.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ReservationDAO_redirectMenu(ActionEvent event) {
        if (Session.getAdmin()){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Admin_Template.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root, 1920, 1080);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
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