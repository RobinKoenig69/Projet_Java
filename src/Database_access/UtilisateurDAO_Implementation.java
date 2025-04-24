package Database_access;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UtilisateurDAO_Implementation {

    public UtilisateurDAO_Implementation(String nom, String prenom, int age, String email, String adresse) {

        Connection connection = Database_connection.connect();

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
}
