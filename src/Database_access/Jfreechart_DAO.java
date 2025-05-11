package Database_access;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La classe {@code Jfreechart_DAO} permet de générer un rapport PDF contenant des graphiques statistiques
 * relatifs aux attractions, aux réservations, aux utilisateurs, et aux réductions.
 * Elle utilise la bibliothèque iText pour le PDF et JFreeChart pour les graphiques.
 *
 * <p>Les graphiques inclus sont :
 * <ul>
 *   <li>Réservations par attraction</li>
 *   <li>Réservations par catégorie</li>
 *   <li>Utilisateurs par tranche d'âge</li>
 *   <li>Réductions par type de client</li>
 *   <li>Revenus par attraction</li>
 *   <li>Places disponibles par catégorie</li>
 *   <li>Top 5 des attractions les plus réservées</li>
 * </ul>
 *
 * @author Robin KOENIG
 * @version 1.1
 */

public class Jfreechart_DAO {

    private static final int CHART_WIDTH = 500;
    private static final int CHART_HEIGHT = 300;
    private static final String PDF_PATH = "rapport_statistiques.pdf";

    /**
     * Constructeur vide de la classe Jfreechart_DAO.
     */

    public Jfreechart_DAO() {
        // Constructor is now empty
    }

    /**
     * Génère un rapport PDF contenant tous les graphiques statistiques
     * @return Le chemin du fichier PDF généré
     */
    public String generateStatisticsReport() {
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(PDF_PATH));
            document.open();

            // Ajouter l'en-tête du document
            addHeader(document);

            // Générer et ajouter chaque graphique
            addReservationParAttractionChart(document);
            document.add(new Paragraph(" ")); // Espacement

            addReservationsParCategorieChart(document);
            document.add(new Paragraph(" "));

            addUtilisateursParTrancheAgeChart(document);
            document.add(new Paragraph(" "));

            addReductionsParTypeClientChart(document);
            document.add(new Paragraph(" "));

            addRevenuParAttractionChart(document);
            document.add(new Paragraph(" "));

            addPlacesDispoParCategorieChart(document);
            document.add(new Paragraph(" "));

            addTopAttractionsChart(document);

            System.out.println("Rapport PDF généré avec succès: " + PDF_PATH);
            return PDF_PATH;

        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du rapport PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    /**
     * Ajoute l'en-tête du document PDF.
     *
     * @param document Le document PDF en cours de génération.
     * @throws DocumentException si une erreur survient lors de l'ajout du contenu.
     */

    private void addHeader(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        Paragraph title = new Paragraph("Rapport Statistique des Attractions", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Paragraph date = new Paragraph("Généré le: " + dateFormat.format(new Date()), normalFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        document.add(date);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
    }

    /**
     * Ajoute un graphique au document PDF.
     *
     * @param document Le document PDF.
     * @param chart Le graphique JFreeChart à insérer.
     * @param title Le titre du graphique.
     * @throws DocumentException si une erreur d’écriture dans le PDF se produit.
     * @throws IOException si une erreur de lecture/écriture de fichier survient.
     */

    private void addChartToDocument(Document document, JFreeChart chart, String title) throws DocumentException, IOException {
        // Créer une image temporaire du graphique
        File tempFile = File.createTempFile("chart_", ".png");
        ChartUtils.saveChartAsPNG(tempFile, chart, CHART_WIDTH, CHART_HEIGHT);

        // Ajouter un titre pour ce graphique
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        Paragraph chartTitle = new Paragraph(title, subTitleFont);
        chartTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(chartTitle);

        // Ajouter l'image au document
        Image chartImage = Image.getInstance(tempFile.getAbsolutePath());
        chartImage.setAlignment(Element.ALIGN_CENTER);
        document.add(chartImage);

        // Supprimer le fichier temporaire
        tempFile.delete();
    }

    /**
     * Génère et ajoute un graphique en camembert représentant la répartition des réservations par attraction.
     *
     * @param document Le document PDF.
     * @throws SQLException en cas d'erreur SQL.
     * @throws DocumentException en cas d'erreur d’écriture PDF.
     * @throws IOException en cas d'erreur de lecture/écriture de fichier.
     */

    private void addReservationParAttractionChart(Document document) throws SQLException, DocumentException, IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();

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
                dataset.setValue(attractionName, count);
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Répartition des réservations par attraction",
                    dataset,
                    true, // legend
                    true, // tooltips
                    false // URLs
            );

            addChartToDocument(document, chart, "Répartition des réservations par attraction");

        } catch (SQLException e) {
            throw new SQLException("Erreur lors du chargement des statistiques: " + e.getMessage());
        }
    }

    /**
     * Génère et ajoute un graphique en camembert représentant la répartition des réservations par catégorie d’attraction.
     *
     * @param document Le document PDF.
     * @throws SQLException en cas d'erreur SQL.
     * @throws DocumentException en cas d'erreur PDF.
     * @throws IOException en cas d'erreur de fichier.
     */

    private void addReservationsParCategorieChart(Document document) throws SQLException, DocumentException, IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();

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
                dataset.setValue(categorie, count);
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Réservations par Catégorie",
                    dataset,
                    true,
                    true,
                    false
            );

            addChartToDocument(document, chart, "Réservations par Catégorie");

        } catch (SQLException e) {
            throw new SQLException("Erreur lors du chargement des statistiques: " + e.getMessage());
        }
    }

    /**
     * Génère et ajoute un graphique représentant la répartition des utilisateurs par tranche d’âge.
     *
     * @param document Le document PDF.
     * @throws SQLException en cas d'erreur SQL.
     * @throws DocumentException en cas d'erreur PDF.
     * @throws IOException en cas d'erreur de fichier.
     */

    private void addUtilisateursParTrancheAgeChart(Document document) throws SQLException, DocumentException, IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();

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
                dataset.setValue(tranche, count);
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Répartition des utilisateurs par âge",
                    dataset,
                    true,
                    true,
                    false
            );

            addChartToDocument(document, chart, "Répartition des utilisateurs par âge");

        } catch (SQLException e) {
            throw new SQLException("Erreur : " + e.getMessage());
        }
    }

    /**
     * Génère et ajoute un graphique représentant la répartition des réductions par type de client.
     *
     * @param document Le document PDF.
     * @throws SQLException en cas d'erreur SQL.
     * @throws DocumentException en cas d'erreur PDF.
     * @throws IOException en cas d'erreur de fichier.
     */

    private void addReductionsParTypeClientChart(Document document) throws SQLException, DocumentException, IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();

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
                dataset.setValue(typeClient, count);
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Réductions par type de client",
                    dataset,
                    true,
                    true,
                    false
            );

            addChartToDocument(document, chart, "Réductions par type de client");

        } catch (SQLException e) {
            throw new SQLException("Erreur : " + e.getMessage());
        }
    }

    /**
     * Génère et ajoute un graphique en barres représentant le chiffre d’affaires généré par chaque attraction.
     *
     * @param document Le document PDF.
     * @throws SQLException en cas d'erreur SQL.
     * @throws DocumentException en cas d'erreur PDF.
     * @throws IOException en cas d'erreur de fichier.
     */

    private void addRevenuParAttractionChart(Document document) throws SQLException, DocumentException, IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

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
                dataset.addValue(revenu, "Chiffre d'affaires (€)", nom);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Chiffre d'affaires par attraction",
                    "Attraction",
                    "Chiffre d'affaires (€)",
                    dataset
            );

            addChartToDocument(document, chart, "Chiffre d'affaires par attraction");

        } catch (SQLException e) {
            throw new SQLException("Erreur (revenu) : " + e.getMessage());
        }
    }

    /**
     * Génère et ajoute un graphique en barres représentant le nombre de places disponibles par catégorie d’attraction.
     *
     * @param document Le document PDF.
     * @throws SQLException en cas d'erreur SQL.
     * @throws DocumentException en cas d'erreur PDF.
     * @throws IOException en cas d'erreur de fichier.
     */

    private void addPlacesDispoParCategorieChart(Document document) throws SQLException, DocumentException, IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

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
                dataset.addValue(dispo, "Places disponibles", cat);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Places disponibles par catégorie",
                    "Catégorie",
                    "Nombre de places",
                    dataset
            );

            addChartToDocument(document, chart, "Places disponibles par catégorie");

        } catch (SQLException e) {
            throw new SQLException("Erreur (places dispo) : " + e.getMessage());
        }
    }

    /**
     * Génère et ajoute un graphique en barres représentant les 5 attractions les plus réservées.
     *
     * @param document Le document PDF.
     * @throws SQLException en cas d'erreur SQL.
     * @throws DocumentException en cas d'erreur PDF.
     * @throws IOException en cas d'erreur de fichier.
     */

    private void addTopAttractionsChart(Document document) throws SQLException, DocumentException, IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

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
                dataset.addValue(nb, "Réservations", nom);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Top 5 attractions les plus réservées",
                    "Attraction",
                    "Nombre de réservations",
                    dataset
            );

            addChartToDocument(document, chart, "Top 5 attractions les plus réservées");

        } catch (SQLException e) {
            throw new SQLException("Erreur (top attractions) : " + e.getMessage());
        }
    }
}