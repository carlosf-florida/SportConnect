package org.example.sportconnect_01.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneManager {

    private static final double DESIGN_WIDTH = 1920.0;
    private static final double DESIGN_HEIGHT = 1080.0;

    private static Stage stage;

    public static void setStage(Stage stagePrincipal) {
        stage = stagePrincipal;
    }

    public static void cambiarVista(String archivoFXML, String tituloVentana) {
        try {
            URL fxmlUrl = SceneManager.class.getResource("/org/example/sportconnect_01/view/" + archivoFXML);
            if (fxmlUrl == null) {
                throw new IOException("No existe el archivo FXML: " + archivoFXML);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Pane contenedorEscalable = crearContenedorEscalable(root);

            double anchoPantalla = Screen.getPrimary().getVisualBounds().getWidth();
            double altoPantalla = Screen.getPrimary().getVisualBounds().getHeight();

            Scene scene = new Scene(contenedorEscalable, anchoPantalla, altoPantalla);

            URL cssUrl = SceneManager.class.getResource("/org/example/sportconnect_01/css/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setTitle(tituloVentana);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMinWidth(1100);
            stage.setMinHeight(700);
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.mostrarError("No se pudo cargar la pantalla: " + archivoFXML);
        }
    }

    private static Pane crearContenedorEscalable(Parent root) {
        Pane contenedor = new Pane();
        contenedor.getStyleClass().add("screen");

        Group grupo = new Group(root);
        Scale escala = new Scale(1, 1, 0, 0);
        grupo.getTransforms().add(escala);

        NumberBinding escalaAdaptada = Bindings.min(
                contenedor.widthProperty().divide(DESIGN_WIDTH),
                contenedor.heightProperty().divide(DESIGN_HEIGHT)
        );

        escala.xProperty().bind(escalaAdaptada);
        escala.yProperty().bind(escalaAdaptada);

        grupo.layoutXProperty().bind(contenedor.widthProperty()
                .subtract(escalaAdaptada.multiply(DESIGN_WIDTH))
                .divide(2));
        grupo.layoutYProperty().bind(contenedor.heightProperty()
                .subtract(escalaAdaptada.multiply(DESIGN_HEIGHT))
                .divide(2));

        contenedor.getChildren().add(grupo);
        return contenedor;
    }
}
