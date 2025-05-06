package Database_access;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Classe utilitaire pour générer les rapports de statistiques
 */
public class ReportGenerator {

    /**
     * Génère un rapport PDF de statistiques et l'ouvre si généré avec succès
     */
    public static void generateAndOpenReport() {
        // Création d'une tâche en arrière-plan pour ne pas bloquer l'interface utilisateur
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                Jfreechart_DAO chartGenerator = new Jfreechart_DAO();
                return chartGenerator.generateStatisticsReport();
            }
        };

        // Actions à exécuter quand la tâche est terminée
        task.setOnSucceeded(event -> {
            String pdfPath = task.getValue();
            if (pdfPath != null) {
                showSuccessDialog(pdfPath);
                openPdfFile(pdfPath);
            }
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            showErrorDialog(exception.getMessage());
        });

        // Démarrer la tâche dans un thread séparé
        new Thread(task).start();
    }

    /**
     * Affiche une boîte de dialogue de succès
     * @param pdfPath Chemin du fichier PDF généré
     */
    private static void showSuccessDialog(String pdfPath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rapport généré");
        alert.setHeaderText("Le rapport a été généré avec succès");
        alert.setContentText("Le rapport se trouve à : " + pdfPath);
        alert.showAndWait();
    }

    /**
     * Affiche une boîte de dialogue d'erreur
     * @param errorMessage Message d'erreur à afficher
     */
    private static void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur lors de la génération du rapport");
        alert.setContentText("Détails : " + errorMessage);
        alert.showAndWait();
    }

    /**
     * Ouvre le fichier PDF avec l'application par défaut
     * @param pdfPath Chemin du fichier PDF à ouvrir
     */
    private static void openPdfFile(String pdfPath) {
        try {
            File file = new File(pdfPath);
            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            System.err.println("Impossible d'ouvrir le fichier PDF : " + e.getMessage());
        }
    }
}