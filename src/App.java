import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/projet_java";
        String username = "root";
        String password = ""; 

        try {
            // Connexion à la base
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connexion réussie !");

            //recup liste des attractions
            List<Attraction> attractions = AttractionDAO.getAllAttractions(connection);

            //afficher les attractions
            System.out.println("\n--- LISTE DES ATTRACTIONS ---");
            for (Attraction attraction : attractions) {
                System.out.println(
                    attraction.getNom() + " | " +
                    "Places dispo: " + attraction.getPlacesDispo() + " | " +
                    "Prix: " + attraction.getTarif() + "€"
                );
            }


        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}