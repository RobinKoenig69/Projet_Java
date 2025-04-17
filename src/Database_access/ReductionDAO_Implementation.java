package Database_access;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReductionDAO_Implementation {

    public ReductionDAO_Implementation (int pourcentage, String concerne, int Id_attraction){

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {

                String sql = "INSERT INTO Attraction (Pourcentage, concerne, id_attraction) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, pourcentage);
                statement.setString(2, concerne);
                statement.setInt(3, Id_attraction);

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
}
