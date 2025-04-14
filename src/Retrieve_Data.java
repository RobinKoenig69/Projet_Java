import java.sql.*;
import java.time.LocalDateTime;
import java.sql.Timestamp;

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

    public void add_attraction (String nom, int nb_places_tot, String categorie, Boolean ouvert, int prix, Time heure_ouverture, Time heure_fermeture, Time fin_inscription){
        Connection connection = connect();

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

    public void add_reduction (int pourcentage, String concerne, int Id_attraction){
        Connection connection = connect();

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

    public void add_reservation(LocalDateTime date_reservation, float prix, int id_attraction, int id_utilisateur) {
        Connection connection = connect();

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
