import java.sql.*;

public class Retrieve_Data {

    public Connection connect() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/projet_java";
        String username = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connexion réussie !");
            return connection;
        } catch (Exception e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            return null;
        }
    }

    public void add_user(String nom, String prenom, int age, String email, String adresse) {
        Connection connection = connect();

        if (connection != null) {
            try {

                String tranche_age = "";

                if (age <15){
                    tranche_age = "Enfant";
                }

                if (age >15 && age < 18){
                    tranche_age = "Jeune Adultre";
                }

                if  (age > 18 && age < 60){
                    tranche_age = "Adulte";
                }

                if  (age > 60){
                    tranche_age = "Senior";
                }

                String sql = "INSERT INTO Utilisateur (Nom, Prenom, Client_type, Tranche_Age, Email, Adresse) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, nom);
                statement.setString(2, prenom);
                statement.setString(3, "Nouveau");
                statement.setString(4, tranche_age);
                statement.setString(5, email);
                statement.setString(6, adresse);

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


    public void getData() {
        Connection connection = connect();  // Use the connect method

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
