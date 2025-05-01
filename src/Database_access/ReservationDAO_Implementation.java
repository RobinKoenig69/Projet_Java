package Database_access;

import Model.Reservation;
import Model.Session;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

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
    private DatePicker datePicker;

    @FXML
    private Spinner spinner;


    public ReservationDAO_Implementation() {

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
    public void initialize() throws Exceptions_Database {
        List<Reservation> reservations = ReservationDAO_getInfo();

        if (!reservations.isEmpty()) {
            StringBuilder infos = new StringBuilder();

            for (Reservation reservation : reservations) {
                infos.append(String.format(
                        "Code de réservation : %s\nDate : %s\nPrix : %s\nId Attraction : %s\n\n",
                        reservation.getId_reservation(),
                        reservation.getDate_reservation(),
                        reservation.getPrix(),
                        reservation.getId_attraction()
                ));
            }

            if (ReservationInfo != null) {
                ReservationInfo.setText(infos.toString());
            }


        } else {
            if (ReservationInfo != null){
                ReservationInfo.setText("Aucune réservation trouvée pour cet utilisateur.");
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
    public void ReservationDAO_Book() throws Exceptions_Database {
        int id_attraction = Session.getUserBooking();
        int id_user = Session.getUserID();

        int prix = 0;
        int nb_places_tot = 0;

        // Étape 1 : Récupérer le tarif et le nombre de places totales
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

        // Étape 2 : Calculer le nombre de réservations déjà existantes pour cette date
        int nb_reservations_existantes = 0;
        LocalDateTime dateChoisie = datePicker.getValue().atStartOfDay();

        try (Connection connection = Database_connection.connect()) {
            String sql = "SELECT COUNT(*) FROM reservation WHERE id_attraction = ? AND DATE(Date_reservation) = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id_attraction);
                statement.setDate(2, java.sql.Date.valueOf(datePicker.getValue()));
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        nb_reservations_existantes = resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new Exceptions_Database("Erreur lors de la vérification des réservations existantes", e);
        }

        // Étape 3 : Vérifier si la réservation est possible
        int nbDemandes = (int) spinner.getValue();
        if (nbDemandes + nb_reservations_existantes > nb_places_tot * 24) {
            throw new Exceptions_Database("Nombre de places insuffisant pour cette date.");
        }

        // Étape 4 : Insérer la réservation
        try (Connection connection = Database_connection.connect()) {
            String sql = "INSERT INTO reservation (Date_reservation, Prix, id_utilisateur, id_attraction, nb_personnes) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                Timestamp timestamp = Timestamp.valueOf(dateChoisie);
                float prixTotal = prix * nbDemandes;

                statement.setTimestamp(1, timestamp);
                statement.setFloat(2, prixTotal);
                statement.setInt(3, id_user);
                statement.setInt(4, id_attraction);
                statement.setInt(5, nbDemandes);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Réservation effectuée avec succès !");
                }
            }
        } catch (SQLException e) {
            throw new Exceptions_Database("Erreur lors de l'enregistrement de la réservation", e);
        }
    }


}
