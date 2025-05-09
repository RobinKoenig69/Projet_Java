package Database_access;

import Model.Reduction;
import Model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReductionDAO_Implementation {

    public ReductionDAO_Implementation() {
        // constructeur vide requis par FXMLLoader
    }

    @FXML
    private TextArea reductionTable;

    @FXML
    public void  ReductionDAO_Add(int pourcentage, String concerne, int Id_attraction){

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {

                String sql = "INSERT INTO reduction (Pourcentage, concerne, id_attraction) VALUES (?, ?, ?)";
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
    public void initialize() throws Exceptions_Database {
        if (reductionTable != null) {
            Connection connection = Database_connection.connect();

            if (connection == null) {
                reductionTable.setText("Erreur : Connexion à la base de données échouée.");
                return;
            }

            String sql = "SELECT r.Pourcentage, r.Concerne, a.Nom, a.Tarif, a.Categorie " +
                    "FROM reduction r " +
                    "JOIN attraction a ON r.id_attraction = a.id_attraction";

            StringBuilder sb = new StringBuilder();

            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    int pourcentage = resultSet.getInt("Pourcentage");
                    String concerne = resultSet.getString("Concerne");
                    String nomAttraction = resultSet.getString("Nom");
                    float tarif = resultSet.getFloat("Tarif");
                    String categorie = resultSet.getString("Categorie");

                    sb.append(String.format("Réduction : %d%% pour %s\n", pourcentage, concerne));
                    sb.append(String.format(" - Attraction : %s (%.2f€, %s)\n\n", nomAttraction, tarif, categorie));
                }

                reductionTable.setText(sb.toString());

            } catch (SQLException e) {
                reductionTable.setText("Erreur lors de la récupération des réductions : " + e.getMessage());
            }
        } else {
            System.out.println("reductionTable n’est pas lié dans le FXML.");
        }
    }

    @FXML
    public List<Reduction> ReductionDAO_GetAll() throws Exceptions_Database {
        List<Reduction> reductions = new ArrayList<>();
        Connection connection = Database_connection.connect();

        if (connection == null) {
            throw new Exceptions_Database("Connexion à la base de données échouée.");
        }

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
            throw new Exceptions_Database("Erreur lors de la récupération des réductions.", e);
        }

        // Debug
        for (Reduction a : reductions) {
            System.out.println(a);
        }

        return reductions;
    }


    public void ReductionDAO_redirectMenu(ActionEvent event) {
        if (Session.getAdmin()){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Admin_Template.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

                Scene scene = new Scene(root, 1920, 1080);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Client_Template.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

                Scene scene = new Scene(root, 1920, 1080);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}