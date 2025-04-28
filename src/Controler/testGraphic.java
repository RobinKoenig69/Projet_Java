package Controler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import Encryption.AES.*;

public class testGraphic extends Application {

   public static String UserName = "John";
   public static int UserID = 1;

   @Override
   public void start(Stage primaryStage) {
      try {
         // Load the FXML file (assuming it's located in /resources/Controler/main_view.fxml)
         Parent root = FXMLLoader.load(getClass().getResource("../Database_access/Stats.fxml"));

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

      //Enregistrement de tous les évènements console dans un fichier spécifique :
      System.out.print("\n\nDébut des logs en date du :");

      System.out.println(new Date() +"\n");

      launch(args);


      System.out.print("\n\n");
      Log.stopLogging();
   }
}
