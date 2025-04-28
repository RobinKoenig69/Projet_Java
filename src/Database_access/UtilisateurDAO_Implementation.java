package Database_access;

import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;

import static Controler.testGraphic.UserID;
import static Controler.testGraphic.UserName;
import static Encryption.AES.encrypt;
import static Encryption.AES.generateKey;

public class UtilisateurDAO_Implementation {

    @FXML
    private TextArea UserInfo;

    @FXML
    private TextField Email;

    @FXML
    private TextField Mdp;


    public UtilisateurDAO_Implementation() {

    }

    public void UtilisateurDAO_Add(String nom, String prenom, int age, String email, String adresse) throws Exceptions_Database {

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {

                String tranche_age = "";

                if (age < 15) {
                    tranche_age = "Enfant";
                }

                if (age > 15 && age < 18) {
                    tranche_age = "Jeune Adultre";
                }

                if (age > 18 && age < 60) {
                    tranche_age = "Adulte";
                }

                if (age > 60) {
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
                throw new Exceptions_Database("Erreur de insertion", e);
            }
        } else {
            throw new Exceptions_Database("La connexion à la base de données a échoué");
        }
    }

    @FXML

    public void initialize() throws Exceptions_Database {
        Utilisateur user = UtilisateurDAO_getInfo();

        if (UserInfo != null) { // Vérifie que UserInfo existe avant d'y accéder
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
        } else {
            System.out.println("UserInfo n'est pas lié dans le FXML."); // Debug simple
        }
    }

    /*
    public void initialize() throws Exceptions_Database {
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

     */

    @FXML
    public Utilisateur UtilisateurDAO_getInfo() throws Exceptions_Database {
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
                throw new Exceptions_Database("Erreur lors de la recherche", e);
            }
        } else {
            throw new Exceptions_Database("La connexion à la base de données a échoué");
        }

        return user;
    }

    @FXML
    public Utilisateur UtilisateurDAO_Login() throws Exception {
        Connection connection = Database_connection.connect();

        String Pwd = Mdp.getText();

        String Key_AES = "ECE";

        SecretKey key = generateKey(Key_AES);

        String Pwd_encrypted = encrypt(Pwd, key);

        Utilisateur user = null;

        if (connection != null) {
            try {
                String sql = "SELECT id_utilisateur, Prenom FROM utilisateur WHERE Email = ? AND Mdp = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,  Email.getText());
                statement.setString(2, Pwd_encrypted);

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

                    UserID = id;
                    UserName = prenom;
                } else {
                    throw new Exceptions_Database("Le compte n'existe pas");

                    //renvoi eventuel sur une autre page permettant de se creer un compte
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

        return user;
    }


    @FXML
    public void Print_input(){
        System.out.println(Mdp.getText());
        System.out.println(Email.getText());
    }


}

