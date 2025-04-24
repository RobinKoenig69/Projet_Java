package Database_access;

import Model.Attraction;
import Model.Reduction;
import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ReductionDAO_Implementation {

    public ReductionDAO_Implementation() {
        // constructeur vide requis par FXMLLoader
    }

    @FXML
    public void  ReductionDAO_Add(int pourcentage, String concerne, int Id_attraction){

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

    @FXML
    public List<Reduction> ReductionDAO_GetAll(){
        List<Reduction> reductions = new ArrayList<>();
        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT * FROM reduction";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id_reduction");
                    int pourcentage = resultSet.getInt("Pourcentage");
                    String concerne = resultSet.getString("Concerne");
                    int idattraction = resultSet.getInt("id_attraction");

                    Reduction reduction = new Reduction(id, pourcentage, concerne, idattraction);

                    reductions.add(reduction);
                }

                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                System.out.println("Erreur lors de la recherche : " + e.getMessage());
            }
        } else {
            System.out.println("Connexion à la base de données échouée.");
        }

        // For debug

        for (Reduction a : reductions) {
            System.out.println(a);
        }



        return reductions;
    }

}

