package Database_access;

import Model.Session;
import Model.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
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

/**
 * Implémentation du DAO (Data Access Object) pour gérer les opérations
 * liées à l'entité {@link Utilisateur} dans la base de données.
 * Gère notamment l'inscription, la connexion, l'accès invité et les redirections de scène.
 * <p>
 * Cette classe utilise JavaFX pour les composants de l'interface utilisateur.
 * </p>
 */

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

    /**
     * Constructeur vide de la classe UtilisateurDAO_Implementation.
     */

    public UtilisateurDAO_Implementation() {

    }

    /**
     * Ajoute un nouvel utilisateur à la base de données.
     *
     * @param nom      Le nom de l'utilisateur.
     * @param prenom   Le prénom de l'utilisateur.
     * @param age      L'âge de l'utilisateur.
     * @param email    L'adresse email.
     * @param adresse  L'adresse postale.
     * @param Mdp      Le mot de passe (déjà hashé).
     * @throws Exceptions_Database en cas d'erreur lors de l'insertion.
     */

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

    /**
     * Initialise les champs de l'interface avec les informations utilisateur si disponibles.
     *
     * @throws Exceptions_Database en cas d'erreur lors de la récupération des données.
     */

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

    /**
     * Récupère les informations complètes de l'utilisateur connecté depuis la base de données.
     *
     * @return Un objet {@link Utilisateur} rempli ou un invité par défaut.
     * @throws Exceptions_Database en cas d'erreur de connexion ou de requête SQL.
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

    /**
     * Authentifie un utilisateur en comparant l'email et le mot de passe hashé avec la base de données.
     *
     * @return L'utilisateur connecté s'il existe, sinon null.
     * @throws Exception En cas d'erreur de base ou de connexion.
     */

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

                    Session.setCategorie(client_type);

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

    /**
     * Redirige vers l'écran d'inscription (Register.fxml).
     *
     * @throws Exception si le fichier FXML n'est pas trouvé.
     */

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

    /**
     * Redirige vers le template utilisateur ou administrateur selon l'ID utilisateur.
     *
     * @param userID L'identifiant de l'utilisateur connecté.
     * @throws Exception En cas d'erreur de redirection ou de base.
     */

    @FXML
    public void UtilisateurDAO_LoginRegister_redirect(int userID) throws Exception {

        Connection connection = Database_connection.connect();

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
                    Session.setAdmin(true);
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

    /**
     * Valide les champs de saisie pour l'inscription.
     *
     * @return true si tous les champs sont valides, false sinon.
     */

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

    /**
     * Gère l'inscription d'un nouvel utilisateur après validation des champs.
     *
     * @throws Exception en cas d'erreur d'insertion ou de redirection.
     */

    @FXML
    public void UtilisateurDAO_Register() throws Exception {
        if (!validateInputs()) {
            return;
        }

        String Pwd = Mdp.getText();

        String Pwd_encrypted = hash(Pwd);

        UtilisateurDAO_Add(nom.getText(), prenom.getText(), Integer.parseInt(age.getValue().toString()), Email.getText(), adresse.getText(), Pwd_encrypted);

        Session.setCategorie("Nouveau");

        UtilisateurDAO_LoginRegister_redirect(-1);
    }

    /**
     * Active l'accès invité et redirige vers l'interface client.
     */

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

    /**
     * Redirige vers le menu principal selon l'ID utilisateur actuel dans la session.
     *
     * @throws Exception en cas d'erreur de redirection.
     */

    @FXML
    public void UtilisateurDAO_redirectMenu() throws Exception {

        int iduser = Session.getUserID();

        UtilisateurDAO_LoginRegister_redirect(iduser);
    }

    /**
     * Supprime le compte de l'utilisateur actuellement connecté.
     *
     * @throws Exceptions_Database si une erreur survient pendant la suppression.
     */

    @FXML
    public void UtilisateurDAO_DeleteAccount() throws Exceptions_Database {

        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1,  Session.getUserID());

                ResultSet resultSet = statement.executeQuery();

                resultSet.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                throw new Exceptions_Database("Erreur lors de la suppression", e);
            }
        } else {
            throw new Exceptions_Database("La connexion à la base de données a échoué");
        }
    }

    /**
     * Redirige l'utilisateur vers l'écran principal selon son statut (admin ou client).
     *
     * @param event L'événement JavaFX déclencheur.
     */

    @FXML
    public void UtilisateurDAO_redirectMenu(ActionEvent event) {
        if (Session.getAdmin()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Admin_Template.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root, 1920, 1080);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Variante de redirection vers le menu depuis une option de MenuItem.
     *
     * @param event L'événement déclencheur provenant d'un MenuItem.
     */

    @FXML
    public void UtilisateurDAO_redirectMenu2(ActionEvent event) {
        if (Session.getAdmin()) {
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
        } else {
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

    /**
     * Redirige vers la page de recherche (Search.fxml).
     *
     * @param event L'événement déclencheur.
     */

    @FXML
    public void UtilisateurDAO_RedirectSearch(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Search.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Redirige vers la page de suppression de compte.
     *
     * @param event L'événement déclencheur.
     */

    @FXML
    public void UtilisateurDAO_RedirectDelete(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Delete_Account.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Redirige vers la page de réservation (Search.fxml).
     *
     * @param event L'événement déclencheur.
     */

    @FXML
    public void UtilisateurDAO_RedirectNewReservation(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Search.fxml"));
            Parent root = loader.load();

            // Obtenir la fenêtre à partir d'un quelconque élément de l'interface
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Redirige vers la page des réductions.
     *
     * @param event L'événement déclencheur.
     */

    @FXML
    public void UtilisateurDAO_RedirectReductions(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Reductions.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Redirige vers les statistiques si l'utilisateur est un administrateur.
     *
     * @param event L'événement déclencheur.
     */

    @FXML
    public void UtilisateurDAO_redirectStats(ActionEvent event) {
        if (Session.getAdmin()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Stats.fxml"));
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

    /**
     * Redirige vers la gestion des attractions si l'utilisateur est administrateur.
     *
     * @param event L'événement déclencheur.
     */

    @FXML
    public void UtilisateurDAO_redirectManageAttraction(ActionEvent event) {
        if (Session.getAdmin()){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Admin_Modify_Attraction.fxml"));
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

    /**
     * Redirige vers la gestion des réductions si l'utilisateur est administrateur.
     *
     * @param event L'événement déclencheur.
     */

    @FXML
    public void UtilisateurDAO_redirectManageReduction(ActionEvent event) {
        if (Session.getAdmin()){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Admin_Modify_Reduction.fxml"));
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

    /**
     * Redirige vers la gestion des utilisateurs si l'utilisateur est administrateur.
     *
     * @param event L'événement déclencheur.
     */

    @FXML
    public void UtilisateurDAO_redirectManageUser(ActionEvent event) {
        if (Session.getAdmin()){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Database_access/Admin_Modify_User.fxml"));
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