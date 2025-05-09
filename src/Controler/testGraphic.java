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

public class testGraphic extends Application {

   public static String UserName = "";
   public static int UserID = -1;

   Session newsession = new Session(UserName, UserID, 2, true, "");



   //public String Current_page = "User_Anciennes_Reservations";
   public String Current_page = "Admin_Template";
   //public String Current_page = "Book";
   //public String Current_page = "Reservation_cancel";

   @Override
   public void start(Stage primaryStage) {
      try {

         Parent root = FXMLLoader.load(getClass().getResource("../Database_access/" +Current_page +".fxml"));

         Scene scene = new Scene(root, 1920, 1080);
         primaryStage.setTitle("Test JavaFX avec FXML");
         primaryStage.setScene(scene);
         primaryStage.setFullScreen(false);
         primaryStage.show();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) throws IOException {
      Log.startLogging("console_output.txt");

      System.out.print("\n\nDébut des logs en date du :");

      System.out.println(new Date() +"\n");

      launch(args);


      System.out.print("\n\n");
      Log.stopLogging();
   }
}
