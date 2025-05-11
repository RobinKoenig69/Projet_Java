package Database_access;

import Model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

/**
 * La classe {@code StatsController} est responsable de l'affichage des statistiques
 * dans l'interface graphique sous forme de graphiques (camembert et histogramme).
 * <p>
 * Elle interroge la base de données pour afficher :
 * <ul>
 *     <li>La répartition des réservations par attraction ou catégorie</li>
 *     <li>Les utilisateurs par tranche d'âge</li>
 *     <li>Les réductions appliquées selon le type de client</li>
 *     <li>Les revenus générés par attraction</li>
 *     <li>Le nombre de places disponibles par catégorie</li>
 *     <li>Les 5 attractions les plus réservées</li>
 * </ul>
 * Elle permet aussi de générer un rapport PDF via {@link ReportGenerator}.
 * </p>
 */

public class StatsController {

    @FXML
    private Button generateReportButton; // Ajoutez ce bouton dans votre fichier FXML

    @FXML
    private TextArea infos_parc;

    /**
     * Méthode appelée automatiquement lors de l'initialisation du contrôleur.
     * Elle charge toutes les statistiques nécessaires à l'affichage.
     */

    @FXML
    public void initialize() {
        loadReservationsParCategorie();
        loadUtilisateursParTrancheAge();
        loadReductionsParTypeClient();
        loadReservationParAttraction();

        loadRevenuParAttraction();
        loadPlacesDispoParCategorie();
        loadTopAttractions();
    }

    /**
     * Génère un rapport PDF des statistiques via {@link ReportGenerator}.
     *
     * @param event L'événement déclencheur (clic sur le bouton).
     */

    // Méthode pour générer un rapport PDF
    @FXML
    public void generatePdfReport(ActionEvent event) {
        ReportGenerator.generateAndOpenReport();
    }

    // Le reste du code reste inchangé
    @FXML
    private PieChart reservationParAttraction;

    /**
     * Charge la répartition des réservations par attraction dans un graphique en camembert.
     */

    public void loadReservationParAttraction() {
        ObservableList<PieChart.Data> dataList = FXCollections.observableArrayList();

        String query = """
            SELECT a.Nom, COUNT(r.id_reservation) as nb_reservations
            FROM reservation r
            JOIN attraction a ON r.id_attraction = a.id_attraction
            GROUP BY a.Nom
        """;

        try (Connection conn = Database_connection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String attractionName = rs.getString("Nom");
                int count = rs.getInt("nb_reservations");
                dataList.add(new PieChart.Data(attractionName, count));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des statistiques : " + e.getMessage());
        }

        reservationParAttraction.setData(dataList);
        reservationParAttraction.setTitle("Répartition des réservations par attraction");
    }


    @FXML
    private PieChart reservationsParCategorie;

    /**
     * Charge la répartition des réservations par catégorie d’attraction dans un graphique en camembert.
     */

    public void loadReservationsParCategorie() {
        ObservableList<PieChart.Data> dataList = FXCollections.observableArrayList();

        String query = """
        SELECT a.Categorie, COUNT(r.id_reservation) as nb_reservations
        FROM reservation r
        JOIN attraction a ON r.id_attraction = a.id_attraction
        GROUP BY a.Categorie
    """;

        try (Connection conn = Database_connection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String categorie = rs.getString("Categorie");
                int count = rs.getInt("nb_reservations");
                dataList.add(new PieChart.Data(categorie, count));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement : " + e.getMessage());
        }

        reservationsParCategorie.setData(dataList);
        reservationsParCategorie.setTitle("Réservations par Catégorie");
    }

    @FXML
    private PieChart utilisateursParTrancheAge;

    /**
     * Charge la répartition des utilisateurs par tranche d’âge dans un graphique en camembert.
     */

    public void loadUtilisateursParTrancheAge() {
        ObservableList<PieChart.Data> dataList = FXCollections.observableArrayList();

        String query = """
        SELECT Tranche_Age, COUNT(*) AS nb_utilisateurs
        FROM utilisateur
        GROUP BY Tranche_Age
    """;

        try (Connection conn = Database_connection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String tranche = rs.getString("Tranche_Age");
                int count = rs.getInt("nb_utilisateurs");
                dataList.add(new PieChart.Data(tranche, count));
            }

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        utilisateursParTrancheAge.setData(dataList);
        utilisateursParTrancheAge.setTitle("Répartition des utilisateurs par âge");
    }


    @FXML
    private PieChart reductionsParTypeClient;

    /**
     * Charge le nombre de réductions appliquées par type de client dans un graphique en camembert.
     */

    public void loadReductionsParTypeClient() {
        ObservableList<PieChart.Data> dataList = FXCollections.observableArrayList();

        String query = """
        SELECT Concerne, COUNT(*) AS nb_reductions
        FROM reduction
        GROUP BY Concerne
    """;

        try (Connection conn = Database_connection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String typeClient = rs.getString("Concerne");
                int count = rs.getInt("nb_reductions");
                dataList.add(new PieChart.Data(typeClient, count));
            }

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        reductionsParTypeClient.setData(dataList);
        reductionsParTypeClient.setTitle("Réductions par type de client");
    }

    @FXML
    private BarChart<String, Number> revenuParAttraction;

    /**
     * Charge le chiffre d'affaires généré par chaque attraction dans un histogramme.
     */

    public void loadRevenuParAttraction() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Chiffre d'affaires (€)");

        String query = """
        SELECT a.Nom, SUM(r.Prix) AS total_revenu
        FROM reservation r
        JOIN attraction a ON r.id_attraction = a.id_attraction
        GROUP BY a.Nom
        ORDER BY total_revenu DESC
    """;

        try (Connection conn = Database_connection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nom = rs.getString("Nom");
                float revenu = rs.getFloat("total_revenu");
                series.getData().add(new XYChart.Data<>(nom, revenu));
            }

        } catch (SQLException e) {
            System.out.println("Erreur (revenu) : " + e.getMessage());
        }

        revenuParAttraction.getData().add(series);
    }

    @FXML
    private BarChart<String, Number> placesDispoParCategorie;

    /**
     * Charge le nombre total de places disponibles par catégorie dans un histogramme.
     */

    public void loadPlacesDispoParCategorie() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Places disponibles");

        String query = """
        SELECT Categorie, SUM(Nb_places_dispo) AS places_dispo
        FROM attraction
        GROUP BY Categorie
    """;

        try (Connection conn = Database_connection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String cat = rs.getString("Categorie");
                int dispo = rs.getInt("places_dispo");
                series.getData().add(new XYChart.Data<>(cat, dispo));
            }

        } catch (SQLException e) {
            System.out.println("Erreur (places dispo) : " + e.getMessage());
        }

        placesDispoParCategorie.getData().add(series);
    }


    @FXML
    private BarChart<String, Number> topAttractions;

    /**
     * Charge les 5 attractions les plus populaires (en nombre de réservations) dans un histogramme.
     */

    public void loadTopAttractions() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Top 5 Réservations");

        String query = """
        SELECT a.Nom, COUNT(*) AS nb
        FROM reservation r
        JOIN attraction a ON r.id_attraction = a.id_attraction
        GROUP BY a.Nom
        ORDER BY nb DESC
        LIMIT 5
    """;

        try (Connection conn = Database_connection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nom = rs.getString("Nom");
                int nb = rs.getInt("nb");
                series.getData().add(new XYChart.Data<>(nom, nb));
            }

        } catch (SQLException e) {
            System.out.println("Erreur (top attractions) : " + e.getMessage());
        }

        topAttractions.getData().add(series);
    }

    /**
     * Redirige l'utilisateur vers le menu principal approprié (admin ou client).
     *
     * @param event L'événement déclenché par le menu.
     */

    public void Stats_redirectMenu(ActionEvent event) {
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


}