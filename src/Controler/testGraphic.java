package Controler;

import Database_access.UtilisateurDAO_Implementation;
import Model.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;


/**
 * La classe testGraphic est une application JavaFX qui initialise et affiche l'interface graphique de l'application.
 */


public class testGraphic extends Application {

   public static String UserName = "";
   public static int UserID = -1;

   Session newsession = new Session(UserName, UserID, -1, false, "");



   //public String Current_page = "User_Anciennes_Reservations";
   //public String Current_page = "Admin_Template";
   //public String Current_page = "Book";
   //public String Current_page = "Reservation_cancel";

   public String Current_page = "Login";



 /**
     * Méthode principale qui démarre l'application JavaFX.
     *
     * @param primaryStage La scène principale de l'application.
     */

   @Override
   public void start(Stage primaryStage) {
      try {

         Parent root = FXMLLoader.load(getClass().getResource("../Database_access/" +Current_page +".fxml"));

         Scene scene = new Scene(root, 1920, 1080);
         primaryStage.setTitle("Logiciel de gestion du Parc : ECE-Tycoon !");
         primaryStage.setScene(scene);
         primaryStage.setFullScreen(false);
         primaryStage.show();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }


   /**
     * Méthode main qui lance l'application et initialise la journalisation.
     *
     * @param args Les arguments de la ligne de commande.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */


   public static void main(String[] args) throws IOException {
      Log.startLogging("console_output.txt");

      System.out.print("\n\nDébut des logs en date du :");

      System.out.println(new Date() +"\n");

      launch(args);


      System.out.print("\n\n");
      Log.stopLogging();
   }
}
