package Database_access;

import Model.Session;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public void AdminDAO_Add_Or_Modify_User() throws Exceptions_Database {

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
            if (clients_info != null){
                clients_info.setText("Aucune réservation trouvée pour cet utilisateur.");
            }
        }
    }

}