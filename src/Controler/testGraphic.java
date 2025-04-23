package Controler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class testGraphic extends Application {
   @Override
   public void start(Stage primaryStage) {
      try {
         // Load the FXML file (assuming it's located in /resources/Controler/main_view.fxml)
         Parent root = FXMLLoader.load(getClass().getResource("../Database_access/Search.fxml"));

         Scene scene = new Scene(root, 1920, 1080);

         primaryStage.setTitle("Test JavaFX avec FXML");
         primaryStage.setScene(scene);
         primaryStage.setFullScreen(false);
         primaryStage.show();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {
      launch(args);
   }
}
