package org.example.sportconnect_01;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.sportconnect_01.util.SceneManager;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login");
    }

    public static void main(String[] args) {
        launch();
    }
}
