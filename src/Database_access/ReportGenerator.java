package Database_access;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * La classe {@code ReportGenerator} est une classe utilitaire responsable de la génération et
 * de l’ouverture d’un rapport PDF contenant des statistiques graphiques.
 * <p>
 * Elle s’appuie sur {@code Jfreechart_DAO} pour générer le fichier, utilise un thread séparé via
 * {@code javafx.concurrent.Task} pour ne pas bloquer l’interface utilisateur, et fournit un retour visuel
 * à l’utilisateur via des boîtes de dialogue.
 * </p>
 *
 * @author Robin KOENIG & Marius LEPERE & Sofia CAILLAUD
 * @version 3.5
 */

public class ReportGenerator {

    /**
     * Lance la génération du rapport PDF en tâche de fond, puis ouvre le fichier PDF
     * si la génération a été effectuée avec succès.
     * <p>
     * Affiche également une boîte de dialogue pour informer l'utilisateur du succès ou de l'échec.
     * </p>
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
     * Affiche une boîte de dialogue d'information signalant que le rapport a été généré avec succès.
     *
     * @param pdfPath Le chemin du fichier PDF généré.
     */
    private static void showSuccessDialog(String pdfPath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rapport généré");
        alert.setHeaderText("Le rapport a été généré avec succès");
        alert.setContentText("Le rapport se trouve à : " + pdfPath);
        alert.showAndWait();
    }

    /**
     * Affiche une boîte de dialogue d’erreur contenant un message détaillant le problème rencontré.
     *
     * @param errorMessage Le message d'erreur à afficher à l’utilisateur.
     */
    private static void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur lors de la génération du rapport");
        alert.setContentText("Détails : " + errorMessage);
        alert.showAndWait();
    }

    /**
     * Tente d’ouvrir le fichier PDF généré en utilisant l’application par défaut du système.
     *
     * @param pdfPath Le chemin du fichier PDF à ouvrir.
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