package Database_access;

import Model.Utilisateur;
import Model.Attraction;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import java.sql.*;
import java.time.LocalDate;


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

    @FXML
    private Spinner id_attraction_txt;

    @FXML
    private TextArea nom_attraction_txt;

    @FXML
    private Spinner places_tot_txt;

    @FXML
    private Spinner places_dispo_txt;

    @FXML
    private Spinner ouvert_txt;

    @FXML
    private TextArea tarif_txt;

    @FXML
    private TextArea categorie_txt;

    @FXML
    private TextArea ouverture_txt;

    @FXML
    private TextArea fermeture_txt;

    @FXML
    private TextArea fermeture_inscriptions_txt;



    @FXML
    private TextArea afficher_attractions;

    /*
     * public AdminDAO_Implementation() {
     * 
     * }
    */




    public void AdminDAO_Delete_Attraction() throws Exceptions_Database{
        int id = (int)id_attraction_txt.getValue();
        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT COUNT(*) FROM attraction WHERE id_attraction = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    String updateQuery = "DELETE FROM attraction WHERE id_attraction = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, id);

                        updateStmt.executeUpdate();
                    }
                } else {
                    System.out.println("L'attraction avec cet id n'existe pas");
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

    public void AdminDAO_Add_Or_Modify_Attraction() throws Exceptions_Database {
        int id_attraction = (Integer) id_attraction_txt.getValue();
        String nom_attraction = nom_attraction_txt.getText();
        int nb_places_tot = (Integer) places_tot_txt.getValue();
        int nb_places_dispo = (Integer) places_dispo_txt.getValue();
        boolean ouvert = ((Integer) ouvert_txt.getValue()) == 1; // ou (Boolean) ouvert_txt.getValue()
        Float tarif = Float.parseFloat(tarif_txt.getText());
        String categorie = categorie_txt.getText();
        String heure_ouverture = ouverture_txt.getText();
        String heure_fermeture = fermeture_txt.getText();
        String heure_fin_inscription = fermeture_inscriptions_txt.getText();



        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT COUNT(*) FROM attraction WHERE id_attraction = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, id_attraction);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    String updateQuery = "UPDATE attraction SET Nom = ?, Nb_places_tot = ?, Nb_places_dispo = ?, Ouvert = ?, Tarif = ?, Categorie = ?, Heure_ouverture = ?, Heure_fermeture = ?, Heure_fin_inscription = ? WHERE id_attraction = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, nom_attraction);
                        updateStmt.setInt(2, nb_places_tot);
                        updateStmt.setInt(3, nb_places_dispo);
                        updateStmt.setBoolean(4, ouvert);
                        updateStmt.setFloat(5, tarif);
                        updateStmt.setString(6, categorie);
                        updateStmt.setString(7, heure_ouverture);
                        updateStmt.setString(8, heure_fermeture);
                        updateStmt.setString(9, heure_fin_inscription);
                        updateStmt.setInt(10, id_attraction);
                        updateStmt.executeUpdate();
                    }
                } else {
                    String insertQuery = "INSERT INTO attraction (id_attraction, Nom, Nb_places_tot, Nb_places_dispo, Ouvert, Tarif, Categorie, Heure_ouverture, Heure_fermeture, Heure_fin_inscription) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, id_attraction);
                        insertStmt.setString(2, nom_attraction);
                        insertStmt.setInt(3, nb_places_tot);
                        insertStmt.setInt(4, nb_places_dispo);
                        insertStmt.setBoolean(5, ouvert);
                        insertStmt.setFloat(6, tarif);
                        insertStmt.setString(7, categorie);
                        insertStmt.setString(8, heure_ouverture);
                        insertStmt.setString(9, heure_fermeture);
                        insertStmt.setString(10, heure_fin_inscription);
                        insertStmt.executeUpdate();
                    }
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
    public List<Attraction> AdminDAO_GetAttractions() throws Exceptions_Database {
        List<Attraction> attractions = new ArrayList<>();
        Connection connection = Database_connection.connect();

        if (connection != null) {
            try {
                String sql = "SELECT * FROM attraction";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id_attraction = resultSet.getInt("id_attraction");
                    String nom_attraction = resultSet.getString("Nom");
                    int nb_places_tot = resultSet.getInt("Nb_places_tot");
                    int Nb_places_dispo = resultSet.getInt("Nb_places_dispo");
                    boolean ouvert = resultSet.getBoolean("Ouvert");
                    float tarif = resultSet.getFloat("Tarif");
                    String categorie = resultSet.getString("Categorie");
                    Time heure_ouverture = resultSet.getTime("Heure_ouverture");
                    Time heure_fermeture = resultSet.getTime("Heure_fermeture");
                    Time heure_fin_inscription = resultSet.getTime("Heure_fin_inscription");

                    Attraction attraction = new Attraction(
                        id_attraction,
                        nom_attraction,
                        Nb_places_dispo,
                        nb_places_tot,
                        tarif,
                        ouvert,
                        categorie,
                        heure_ouverture,
                        heure_fermeture,
                        heure_fin_inscription
                    );
                    attractions.add(attraction);
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

        return attractions;
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
        List<Attraction> attractions = AdminDAO_GetAttractions();

        if (!utilisateurs.isEmpty()) {
            StringBuilder infos = new StringBuilder();

            for (Utilisateur utilisateur : utilisateurs) {
                infos.append(String.format(
                        "ID : %s\nNom : %s\nPrenom : %s\nType : %s\nTranche age : %s\nEmail : %s\nAdresse : %s\nDerniere visite : %s\nAttraction pref ID : %s\n\n",
                        utilisateur.getId_utilisateur(),
                        utilisateur.getNom(),
                        utilisateur.getPrenom(),
                        utilisateur.getClientType(),
                        utilisateur.getTrancheAge(),
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
                clients_info.setText("Aucun utilisateur trouvé.");
            }
        }

        if(!attractions.isEmpty()){
            StringBuilder infos_attractions = new StringBuilder();

            for(Attraction attraction : attractions){
                infos_attractions.append(String.format(
                    "ID : %s\nNom : %s\nNombre de places disponibles : %s\nNombre de places total : %s\nTarif : %s\nOuvert : %s\nCategorie : %s\nHeure ouverture : %s\nHeure fermeture : %s\nHeure fin inscription : %s\n\n",
                    attraction.getId(),
                    attraction.getNom(),
                    attraction.getNb_places_dispo(),
                    attraction.getNb_places_tot(),
                    attraction.getTarif(),
                    attraction.isOuvert(),
                    attraction.getCategorie(),
                    attraction.getHeureOuverture(),
                    attraction.getHeureFermeture(),
                    attraction.getHeureFinInscription()
                ));
            }

            if(afficher_attractions!=null){
                afficher_attractions.setText(infos_attractions.toString());
            }
        } else {
            if (clients_info != null) {
                clients_info.setText("Aucune attractions trouvées.");
            }
        }

    }

}