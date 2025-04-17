package Database_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ReservationDAO_Implementation {

    public ReservationDAO_Implementation(LocalDateTime date_reservation, float prix, int id_attraction, int id_utilisateur) {

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
                System.out.println("Erreur lors de l'insertion : " + e.getMessage());
            }
        } else {
            System.out.println("La connexion à la base de données a échoué.");
        }
    }

}
