package Database_access;

import Model.Session;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import com.mysql.cj.util.Util;

import static Controler.testGraphic.UserID;
import static Controler.testGraphic.UserName;
import static Encryption.MD5.hash;
import static java.sql.Types.NULL;

import Encryption.MD5.*;

import java.util.ArrayList;
import java.util.List;

public class AdminDAO_Implementation_User {

    @FXML
    private TextArea clients_info;

    @FXML
    private Spinner id_txt;

    @FXML
    private TextArea nom_txt;

    @FXML
    private TextArea prenom_txt;

    @FXML
    private TextArea type_txt;

    @FXML
    private TextArea age_txt;

    @FXML
    private TextArea email_txt;

    @FXML
    private TextArea adresse_txt;

    @FXML
    private DatePicker visite_txt;

    @FXML
    private Spinner pref_txt;

    /*
     * public AdminDAO_Implementation() {
     * 
     * }
    */


    public void AdminDAO_Delete_User() throws Exceptions_Database{
        int id = (int)id_txt.getValue();
        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT COUNT(*) FROM utilisateur WHERE id_utilisateur = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    String updateQuery = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, id);

                        updateStmt.executeUpdate();
                    }
                } else {
                    System.out.println("L'utilisateur avec cet id n'existe pas");
                }


                stmt.close();
                connection.close();

            } catch (Exception e) {
                throw new  Exceptions_Database("Erreur lors de l'insertion", e);
            }
        } else {
            throw new Exceptions_Database("La connexion à la base de données a échoué");
        }

    }
    

    public void AdminDAO_Add_Or_Modify_User() throws Exceptions_Database {

        int id = (int)id_txt.getValue();
        String nom = nom_txt.getText();
        String prenom = prenom_txt.getText();
        String type = type_txt.getText();
        String age = age_txt.getText();
        String email = email_txt.getText();
        String adresse = adresse_txt.getText();
        LocalDate visite = visite_txt.getValue();
        int pref = (int)pref_txt.getValue();


        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT COUNT(*) FROM utilisateur WHERE id_utilisateur = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    String updateQuery = "UPDATE utilisateur SET Nom = ?, Prenom = ?, Client_type = ?, Tranche_Age = ?, Email = ?, Adresse = ?, Derniere_visite = ?, Attraction_preferee_id = ? WHERE id_utilisateur = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, nom);
                        updateStmt.setString(2, prenom);
                        updateStmt.setString(3, type);
                        updateStmt.setString(4, age);
                        updateStmt.setString(5, email);
                        updateStmt.setString(6, adresse);
                        updateStmt.setDate(7, java.sql.Date.valueOf(visite));
                        updateStmt.setInt(8, pref);
                        updateStmt.setInt(9, id);
                        updateStmt.executeUpdate();
                    }
                } else {
                    String insertQuery = "INSERT INTO utilisateur (id_utilisateur, Nom, Prenom, Client_type, Tranche_Age, Email, Adresse, Derniere_visite, Attraction_preferee_id, Mdp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, id);
                        insertStmt.setString(2, nom);
                        insertStmt.setString(3, prenom);
                        insertStmt.setString(4, type);
                        insertStmt.setString(5, age);
                        insertStmt.setString(6, email);
                        insertStmt.setString(7, adresse);
                        insertStmt.setDate(8, java.sql.Date.valueOf(visite));
                        insertStmt.setInt(9, pref);
                        insertStmt.setString(10, "e10adc3949ba59abbe56e057f20f883e"); //Mdp 123456
                        insertStmt.executeUpdate();

                    }
                    //existe pas
                }


                stmt.close();
                connection.close();

            } catch (Exception e) {
                throw new  Exceptions_Database("Erreur lors de l'insertion", e);
            }
        } else {
            throw new Exceptions_Database("La connexion à la base de données a échoué");
        }
    }



    @FXML
    public List<Utilisateur> AdminDAO_GetUsers() throws Exceptions_Database {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT * FROM utilisateur";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id_utilisateur");
                    String nom = resultSet.getString("Nom");
                    String prenom = resultSet.getString("Prenom");
                    String type = resultSet.getString("Client_Type");
                    String age = resultSet.getString("Tranche_Age");
                    String email = resultSet.getString("Email");
                    String adresse = resultSet.getString("Adresse");
                    Date derniere_visite = resultSet.getDate("Derniere_visite");
                    int attraction_pref = resultSet.getInt("Attraction_preferee_id");

                    Utilisateur utilisateur = new Utilisateur(id,
                            nom, prenom, type, age, email,
                            adresse, derniere_visite, attraction_pref);
                    utilisateurs.add(utilisateur);
                }

                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                throw new Exceptions_Database("Erreur lors de la recherche", e);
            }
        } else {
            throw new Exceptions_Database("La connexion à la base de données a échoué");
        }

        return utilisateurs;
    }

    @FXML
    public void initialize() throws Exceptions_Database {
        List<Utilisateur> utilisateurs = AdminDAO_GetUsers();

        if (!utilisateurs.isEmpty()) {
            StringBuilder infos = new StringBuilder();

            for (Utilisateur utilisateur : utilisateurs) {
                infos.append(String.format(
                        "ID : %s\nNom : %s\nPrenom : %s\nType : %s\nTranche age : %s\nType : %s\nEmail : %s\nAdresse : %s\nDerniere visite : %s\nAttraction pref ID : %s\n\n",
                        utilisateur.getId_utilisateur(),
                        utilisateur.getNom(),
                        utilisateur.getPrenom(),
                        utilisateur.getClientType(),
                        utilisateur.getTrancheAge(),
                        utilisateur.getClientType(),
                        utilisateur.getEmail(),
                        utilisateur.getAdresse(),
                        utilisateur.getDerniereVisite(),
                        utilisateur.getAttractionPrefereeId()

                ));
            }

            if (clients_info != null) {
                clients_info.setText(infos.toString());
            }

        } else {
            if (clients_info != null) {
                clients_info.setText("Aucune réservation trouvée pour cet utilisateur.");
            }
        }
    }

}