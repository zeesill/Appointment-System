package seals.c195task;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seals.c195task.Helper.JDBC;

import java.io.IOException;

/***
 * LAUNCH APPLICATION
 */
public class RunApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Login-View.fxml"));
        stage.setTitle("Appointment Scheduling System");
        stage.setScene(new Scene(root, 650, 450));
        stage.show();
   }

    public static void main(String[] args) {

        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}