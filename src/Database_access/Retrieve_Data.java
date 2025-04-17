package Database_access;

import java.sql.*;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class Retrieve_Data {

    public void getData() {
        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM your_table_name";  // Change this to your actual table

                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    // Example: retrieve "id" and "name"
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    System.out.println("ID: " + id + ", Name: " + name);
                }

                // Cleanup
                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                System.out.println("Erreur lors de la récupération : " + e.getMessage());
            }
        }
    }
}
