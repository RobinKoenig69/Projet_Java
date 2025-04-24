import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import dao.AttractionDAO;
import dao.ReductionDAO;
import model.Attraction;
import model.Reduction;

public class App {
    public static void main(String[] args) {
        try {
            // Connexion à la base
            String jdbcUrl = "jdbc:mysql://localhost:3306/projet_java";
            String username = "root";
            String password = "";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connexion réussie !");
            connection.close();

            /* recup liste des attractions
            List<Attraction> attractions = AttractionDAO.getAllAttractions(connection);

            // afficher les attractions
            System.out.println("\n--- LISTE DES ATTRACTIONS ---");
            for (Attraction attraction : attractions) {
                System.out.println(
                        attraction.getNom() + " | " +
                                "Places dispo: " + attraction.getPlacesDispo() + " | " +
                                "Prix: " + attraction.getTarif() + "€"
                );
            }

            // afficher les reduc
            System.out.println("\n--- LISTE DES REDUCTIONS ---");
            List<Reduction> reductions = ReductionDAO.getAllReductions(connection);
            for (Reduction red : reductions) {
                System.out.println(red);
            }

            Retrieve_Data rd = new Retrieve_Data();
            rd.afficher_utilisateurs(); */

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}