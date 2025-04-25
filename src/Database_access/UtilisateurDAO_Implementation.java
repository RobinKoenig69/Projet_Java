package Database_access;

import Model.Attraction;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.sql.*;
import java.time.LocalDate;

import static Controler.testGraphic.UserID;
import static Controler.testGraphic.UserName;

public class UtilisateurDAO_Implementation {

    @FXML
    private TextArea UserInfo;


    public UtilisateurDAO_Implementation() {

    }

    public void UtilisateurDAO_Add(String nom, String prenom, int age, String email, String adresse) {

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

    @FXML
    public void initialize() {
        Utilisateur user = UtilisateurDAO_getInfo();

        if (user != null) {
            String infos = String.format(
                    "Nom : %s\nPrénom : %s\nType de client : %s\nTranche d'âge : %s\nEmail : %s\nAdresse : %s\nDernière visite : %s\nAttraction préférée (ID) : %d",
                    user.getNom(),
                    user.getPrenom(),
                    user.getClientType(),
                    user.getTrancheAge(),
                    user.getEmail(),
                    user.getAdresse(),
                    user.getDerniereVisite() != null ? user.getDerniereVisite().toString() : "Aucune",
                    user.getAttractionPrefereeId()
            );
            UserInfo.setText(infos);
        } else {
            UserInfo.setText("Aucun utilisateur trouvé.");
        }
    }

    @FXML
    public Utilisateur UtilisateurDAO_getInfo() {
        Utilisateur user = null;

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT * FROM utilisateur WHERE Prenom LIKE ? AND id_utilisateur = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + UserName + "%");
                statement.setInt(2, UserID);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int id = resultSet.getInt("id_utilisateur");
                    String nom = resultSet.getString("Nom");
                    String prenom = resultSet.getString("Prenom");
                    String client_type = resultSet.getString("Client_type");
                    String tranche_age = resultSet.getString("Tranche_Age");
                    String email = resultSet.getString("Email");
                    String adresse = resultSet.getString("Adresse");
                    Date derniere_visite = resultSet.getDate("Derniere_visite");
                    int id_attractionpref = resultSet.getInt("Attraction_preferee_id");

                    user = new Utilisateur(id, nom, prenom, client_type, tranche_age, email, adresse, derniere_visite, id_attractionpref);
                }

                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                System.out.println("Erreur lors de la recherche : " + e.getMessage());
            }
        }

        return user;
    }
}

