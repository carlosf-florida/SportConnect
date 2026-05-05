package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.sportconnect_01.model.ClaseDeportiva;
import org.example.sportconnect_01.model.Usuario;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

public class ClasesController {

    @FXML private VBox listaClasesBox;

    @FXML
    private void initialize() {
        cargarClases();
    }

    private void cargarClases() {
        listaClasesBox.getChildren().clear();
        Usuario usuarioActual = Session.getUsuarioActual();

        for (ClaseDeportiva clase : DataStore.getClases()) {
            VBox tarjeta = new VBox(7);
            tarjeta.getStyleClass().add("list-card");

            Label nombreLabel = new Label(clase.getTitulo());
            nombreLabel.getStyleClass().add("item-title");

            Label infoLabel = new Label("Deporte: " + clase.getDeporte()
                    + "\nProfesor: " + clase.getProfesor() + " (ID: " + clase.getProfesorId() + ")"
                    + "\nDescripción: " + clase.getDescripcion()
                    + "\nHorario: " + clase.getHorario()
                    + "\nNivel: " + clase.getNivel()
                    + "\nTipo: " + clase.getTipoClaseTexto() + " · Precio: " + clase.getPrecioTexto()
                    + "\nPlazas libres: " + clase.getPlazas());
            infoLabel.getStyleClass().add("normal-text");
            infoLabel.setWrapText(true);

            Button apuntarseButton = new Button("Apuntarme");
            apuntarseButton.getStyleClass().add("small-primary-button");
            apuntarseButton.setOnAction(evento -> apuntarseAClase(clase));

            if (usuarioActual == null || !usuarioActual.esAlumno()) {
                apuntarseButton.setDisable(true);
                apuntarseButton.setText("Solo alumnos");
            } else if (DataStore.estaApuntado(usuarioActual, clase)) {
                apuntarseButton.setDisable(true);
                apuntarseButton.setText("Ya apuntado");
            } else if (!clase.hayPlazas()) {
                apuntarseButton.setDisable(true);
                apuntarseButton.setText("Sin plazas");
            }

            tarjeta.getChildren().addAll(nombreLabel, infoLabel, apuntarseButton);
            listaClasesBox.getChildren().add(tarjeta);
        }
    }

    private void apuntarseAClase(ClaseDeportiva clase) {
        Usuario usuarioActual = Session.getUsuarioActual();

        if (DataStore.apuntarUsuarioAClase(usuarioActual, clase)) {
            AlertUtils.mostrarInfo("Reserva realizada", "Te has apuntado a " + clase.getTitulo() + ".");
            cargarClases();
        } else {
            AlertUtils.mostrarError("No se ha podido realizar la reserva.");
        }
    }

    @FXML private void onMisClasesClick() { SceneManager.cambiarVista("mis-clases-view.fxml", "SportConnect - Mis clases"); }
    @FXML private void onVolverClick() { SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio"); }
}
