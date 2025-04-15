import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class testGraphic extends Application{
   @Override
   public void start(Stage primaryStage) {
      Button button = new Button("Clique-moi !");
      button.setOnAction(e -> System.out.println("Bouton cliqué"));

      StackPane root = new StackPane(button);
      Scene scene = new Scene(root, 900, 850);

      primaryStage.setTitle("Test JavaFX");
      primaryStage.setScene(scene);
      primaryStage.setFullScreen(true);
      primaryStage.show();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
