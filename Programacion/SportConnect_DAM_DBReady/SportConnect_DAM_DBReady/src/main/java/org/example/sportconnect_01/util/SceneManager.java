package org.example.sportconnect_01.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage stage;

    public static void setStage(Stage stagePrincipal) {
        stage = stagePrincipal;
    }

    public static void cambiarVista(String archivoFXML, String tituloVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/org/example/sportconnect_01/view/" + archivoFXML));
            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(SceneManager.class.getResource("/org/example/sportconnect_01/css/styles.css").toExternalForm());

            stage.setTitle(tituloVentana);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.mostrarError("No se pudo cargar la pantalla: " + archivoFXML);
        }
    }
}
