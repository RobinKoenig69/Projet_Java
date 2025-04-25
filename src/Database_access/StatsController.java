package Database_access;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.*;

public class StatsController {

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

    @FXML
    private PieChart reservationParAttraction;

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
}
