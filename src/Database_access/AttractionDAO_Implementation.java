package Database_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Time;


public class AttractionDAO_Implementation {

    public AttractionDAO_Implementation(String nom, int nb_places_tot, String categorie, Boolean ouvert, int prix, Time heure_ouverture, Time heure_fermeture, Time fin_inscription){

        Connection connection = Database_connection.connect();

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

    public void AttractionDAO_GetAll(){

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {

                String sql = "SELECT * FROM attraction";


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
