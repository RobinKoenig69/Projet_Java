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

import static Controler.testGraphic.UserID;
import static Controler.testGraphic.UserName;
import static Encryption.MD5.hash;
import static java.sql.Types.NULL;

import Encryption.MD5.*;

public class UtilisateurDAO_Implementation {

    @FXML
    private TextArea UserInfo;

    @FXML
    private TextField Email;

    @FXML
    private TextField Mdp;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private Spinner age;

    @FXML
    private TextField adresse;

    @FXML
    private TextFlow Error_message;


    public UtilisateurDAO_Implementation() {

    }

    public void UtilisateurDAO_Add(String nom, String prenom, int age, String email, String adresse, String Mdp) throws Exceptions_Database {

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {

                String tranche_age = "";

                if (age < 15) {
                    tranche_age = "Enfant";
                }

                if (age > 15 && age < 18) {
                    tranche_age = "Jeune Adulte";
                }

                if (age > 18 && age < 60) {
                    tranche_age = "Adulte";
                }

                if (age > 60) {
                    tranche_age = "Senior";
                }

                String sql = "INSERT INTO Utilisateur (Nom, Prenom, Client_type, Tranche_Age, Email, Adresse, Mdp) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, nom);
                statement.setString(2, prenom);
                statement.setString(3, "Nouveau");
                statement.setString(4, tranche_age);
                statement.setString(5, email);
                statement.setString(6, adresse);
                statement.setString(7, Mdp);

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

        connection = Database_connection.connect();

        if (connection != null) {
            try {

                String tranche_age = "";

                if (age < 15) {
                    tranche_age = "Enfant";
                }

                if (age > 15 && age < 18) {
                    tranche_age = "Jeune Adulte";
                }

                if (age > 18 && age < 60) {
                    tranche_age = "Adulte";
                }

                if (age > 60) {
                    tranche_age = "Senior";
                }

                String sql = "SELECT *  FROM Utilisateur where Nom = ? AND Prenom = ? AND Client_type = ? AND Tranche_Age = ? AND Email = ? AND Adresse= ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, nom);
                statement.setString(2, prenom);
                statement.setString(3, "Nouveau");
                statement.setString(4, tranche_age);
                statement.setString(5, email);
                statement.setString(6, adresse);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int id = resultSet.getInt("id_utilisateur");

                    Session.setUserID(id);
                    Session.setUserName(prenom);
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

        Date date = new Date(0);

        if (Session.getUserID() == -1) {
            user = new Utilisateur(-1, "Guest", "", "", "","","", date, -1);

            return user;
        }


        if (connection != null) {
            try {
                String sql = "SELECT * FROM utilisateur WHERE Prenom LIKE ? AND id_utilisateur = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + Session.getUserName() + "%");
                statement.setInt(2, Session.getUserID());

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

        String Pwd_encrypted = hash(Pwd);

        //System.out.println(Pwd_encrypted);

        Utilisateur user = null;

        if (connection != null) {
            try {
                String sql = "SELECT * FROM utilisateur WHERE Email = ? AND Mdp = ?";
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

                    UtilisateurDAO_LoginRegister_redirect(id);
                } else {
                    System.out.println("Utilisateur n'existe pas");
                    Error_message.getChildren().clear(); // Nettoyer d'abord l'ancien message
                    Text errorText = new Text("L'utilisateur n'existe pas !");
                    errorText.setStyle("-fx-fill: red; -fx-font-size: 24px;");
                    Error_message.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
                    Error_message.getChildren().add(errorText);
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
    public void UtilisateurDAO_Login_redirect() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Email.getScene().getWindow(); // Email est ton champ de login, donc il est déjà dans la fenêtre !
            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void UtilisateurDAO_LoginRegister_redirect(int userID) throws Exception {

        Connection connection = Database_connection.connect();

        if (userID == -1) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Client_Template.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Email.getScene().getWindow(); // Email est ton champ de login, donc il est déjà dans la fenêtre !
            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();
        } else {
            if (connection != null) {
                try {
                    String sql = "SELECT * FROM administrateur WHERE id_utilisateur = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1,  userID);

                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Admin_Template.fxml"));
                            Parent root = loader.load();
                            Stage stage = (Stage) Email.getScene().getWindow(); // Email est ton champ de login, donc il est déjà dans la fenêtre !
                            Scene scene = new Scene(root, 1920, 1080);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Client_Template.fxml"));
                            Parent root = loader.load();
                            Stage stage = (Stage) Email.getScene().getWindow(); // Email est ton champ de login, donc il est déjà dans la fenêtre !
                            Scene scene = new Scene(root, 1920, 1080);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
        }


    }


    public boolean validateInputs() {
        Error_message.getChildren().clear();

        boolean isValid = true;
        StringBuilder errors = new StringBuilder();

        if (nom.getText().trim().isEmpty()) {
            errors.append("Le nom est requis.\n");
            isValid = false;
        }

        if (prenom.getText().trim().isEmpty()) {
            errors.append("Le prénom est requis.\n");
            isValid = false;
        }

        if (age.getValue() == null) {
            errors.append("L'âge est requis.\n");
            isValid = false;
        }

        if (Email.getText().trim().isEmpty() || !Email.getText().matches("^\\S+@\\S+\\.\\S+$")) {
            errors.append("Adresse email invalide.\n");
            isValid = false;
        }

        if (adresse.getText().trim().isEmpty()) {
            errors.append("L'adresse est requise.\n");
            isValid = false;
        }

        if (Mdp.getText().trim().isEmpty() || Mdp.getText().length() < 6) {
            errors.append("Le mot de passe est requis (au moins 6 caractères).\n");
            isValid = false;
        }

        if (!isValid) {
            Text errorText = new Text(errors.toString());
            errorText.setStyle("-fx-fill: red; -fx-font-size: 18px;");
            Error_message.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            Error_message.getChildren().add(errorText);
        }

        return isValid;
    }


    @FXML
    public void UtilisateurDAO_Register() throws Exception {
        if (!validateInputs()) {
            return;
        }

        String Pwd = Mdp.getText();

        String Pwd_encrypted = hash(Pwd);

        UtilisateurDAO_Add(nom.getText(), prenom.getText(), Integer.parseInt(age.getValue().toString()), Email.getText(), adresse.getText(), Pwd_encrypted);

        UtilisateurDAO_LoginRegister_redirect(-1);
    }

    @FXML
    public void UtilisateurDAO_GuestAccess(){
        Session.setUserName("Guest");
        Session.setUserID(-1);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Client_Template.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Email.getScene().getWindow(); // Email est ton champ de login, donc il est déjà dans la fenêtre !
            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void Print_input(){
        System.out.println(Mdp.getText());
        System.out.println(Email.getText());
    }


}

