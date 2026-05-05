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

import java.util.ArrayList;

public class MisClasesController {

    @FXML private Label resumenMisClasesLabel;
    @FXML private VBox listaMisClasesBox;

    @FXML
    private void initialize() {
        cargarMisClases();
    }

    private void cargarMisClases() {
        listaMisClasesBox.getChildren().clear();
        Usuario usuarioActual = Session.getUsuarioActual();

        if (usuarioActual == null || !usuarioActual.esAlumno()) {
            resumenMisClasesLabel.setText("Esta pantalla es para alumnos inscritos en clases.");
            return;
        }

        ArrayList<ClaseDeportiva> clasesApuntadas = DataStore.getClasesApuntadas(usuarioActual);
        resumenMisClasesLabel.setText("Tienes " + clasesApuntadas.size() + " clase(s) apuntada(s).");

        if (clasesApuntadas.isEmpty()) {
            VBox tarjetaVacia = new VBox(8);
            tarjetaVacia.getStyleClass().add("list-card");
            Label mensajeLabel = new Label("Todavía no te has apuntado a ninguna clase.");
            mensajeLabel.getStyleClass().add("normal-text");
            Label ayudaLabel = new Label("Puedes apuntarte desde la pantalla Ver clases.");
            ayudaLabel.getStyleClass().add("muted-text");
            tarjetaVacia.getChildren().addAll(mensajeLabel, ayudaLabel);
            listaMisClasesBox.getChildren().add(tarjetaVacia);
            return;
        }

        for (ClaseDeportiva clase : clasesApuntadas) {
            VBox tarjeta = new VBox(8);
            tarjeta.getStyleClass().add("list-card");

            Label nombreLabel = new Label(clase.getTitulo());
            nombreLabel.getStyleClass().add("item-title");

            Label infoLabel = new Label("Profesor: " + clase.getProfesor()
                    + "\nDeporte: " + clase.getDeporte()
                    + "\nDescripción: " + clase.getDescripcion()
                    + "\nHorario: " + clase.getHorario()
                    + "\nNivel: " + clase.getNivel()
                    + "\nTipo: " + clase.getTipoClaseTexto()
                    + "\nPrecio: " + clase.getPrecioTexto()
                    + "\nEstado: apuntado");
            infoLabel.getStyleClass().add("normal-text");
            infoLabel.setWrapText(true);

            Button cancelarButton = new Button("Cancelar inscripción");
            cancelarButton.getStyleClass().add("danger-button");
            cancelarButton.setMaxWidth(210.0);
            cancelarButton.setOnAction(evento -> cancelarInscripcion(clase));

            tarjeta.getChildren().addAll(nombreLabel, infoLabel, cancelarButton);
            listaMisClasesBox.getChildren().add(tarjeta);
        }
    }

    private void cancelarInscripcion(ClaseDeportiva clase) {
        Usuario usuarioActual = Session.getUsuarioActual();
        if (DataStore.cancelarInscripcion(usuarioActual, clase)) {
            AlertUtils.mostrarInfo("Inscripción cancelada", "Has cancelado la inscripción a " + clase.getTitulo() + ".");
            cargarMisClases();
        } else {
            AlertUtils.mostrarError("No se pudo cancelar la inscripción.");
        }
    }

    @FXML private void onVerMasClasesClick() { SceneManager.cambiarVista("clases-view.fxml", "SportConnect - Clases"); }
    @FXML private void onVolverClick() { SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio"); }
}
