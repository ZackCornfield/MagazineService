package magazine_service;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/Main.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/application.css").toExternalForm()); 
            primaryStage.setScene(scene);
            primaryStage.setTitle("Magazine Service");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
