package Database_access;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminGetInfo implements Initializable {

    @FXML
    private TextArea infos_parc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        afficherInfosParc();
    }

    public void afficherInfosParc() {
        Connection connection = Database_connection.connect();
        StringBuilder infos = new StringBuilder();

        if (connection != null) {
            try {
                // Chiffre d'affaires total
                String sqlChiffreAffaire = "SELECT SUM(Prix) AS chiffre_affaire FROM reservation";
                try (PreparedStatement stmt = connection.prepareStatement(sqlChiffreAffaire);
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        infos.append("Chiffre d'affaires total : ")
                                .append(rs.getFloat("chiffre_affaire"))
                                .append(" €\n");
                    }
                }

                // Nombre d'utilisateurs
                String sqlUsers = "SELECT COUNT(*) AS nb_utilisateurs FROM utilisateur";
                try (PreparedStatement stmt = connection.prepareStatement(sqlUsers);
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        infos.append("Nombre d'utilisateurs : ")
                                .append(rs.getInt("nb_utilisateurs"))
                                .append("\n");
                    }
                }

                // Nombre de réservations
                String sqlRes = "SELECT COUNT(*) AS nb_reservations FROM reservation";
                try (PreparedStatement stmt = connection.prepareStatement(sqlRes);
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        infos.append("Nombre de réservations : ")
                                .append(rs.getInt("nb_reservations"))
                                .append("\n");
                    }
                }

                // Nombre d'attractions
                String sqlAttr = "SELECT COUNT(*) AS nb_attractions FROM attraction";
                try (PreparedStatement stmt = connection.prepareStatement(sqlAttr);
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        infos.append("Nombre d'attractions : ")
                                .append(rs.getInt("nb_attractions"))
                                .append("\n");
                    }
                }

                connection.close();

            } catch (Exception e) {
                infos.append("Erreur lors de la récupération des données : ").append(e.getMessage());
            }
        } else {
            infos.append("Erreur : Connexion à la base de données échouée.");

        }

        infos_parc.setText(infos.toString());
    }
}