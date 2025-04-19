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
         Parent root = FXMLLoader.load(getClass().getResource("AppTemplate.fxml"));

         Scene scene = new Scene(root, 900, 850);

         primaryStage.setTitle("Test JavaFX avec FXML");
         primaryStage.setScene(scene);
         primaryStage.setFullScreen(true);
         primaryStage.show();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {
      launch(args);
   }
}
