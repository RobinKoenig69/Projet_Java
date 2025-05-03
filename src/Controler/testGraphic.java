package Controler;

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
   public static int UserID = 13;

   Session newsession = new Session(UserName, UserID, -1);


   public String Current_page = "User_anciennes_reservations";

   @Override
   public void start(Stage primaryStage) {
      try {
         // Load the FXML file (assuming it's located in /resources/Controler/main_view.fxml)
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
