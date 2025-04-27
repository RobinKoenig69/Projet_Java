package Database_access;

import Model.Reservation;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Controler.testGraphic.UserID;
import static Controler.testGraphic.UserName;
import static javafx.application.Application.getUserAgentStylesheet;

public class ReservationDAO_Implementation {

    @FXML
    private TextArea ReservationInfo; // ou TextArea si tu as utilisé <TextArea>

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

            ReservationInfo.setText(infos.toString());
        } else {
            ReservationInfo.setText("Aucune réservation trouvée pour cet utilisateur.");
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

}
