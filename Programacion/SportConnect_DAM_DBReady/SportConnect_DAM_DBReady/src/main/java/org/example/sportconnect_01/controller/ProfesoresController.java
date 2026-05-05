package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.sportconnect_01.model.Profesor;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.util.SceneManager;

public class ProfesoresController {

    @FXML private VBox listaProfesoresBox;

    @FXML
    private void initialize() {
        for (Profesor profesor : DataStore.getProfesores()) {
            VBox tarjeta = new VBox(6);
            tarjeta.getStyleClass().add("list-card");

            Label nombreLabel = new Label(profesor.getNombreCompleto());
            nombreLabel.getStyleClass().add("item-title");

            Label infoLabel = new Label("ID usuario/profesor: " + profesor.getId()
                    + "\nNick: " + profesor.getNick()
                    + "\nEmail: " + profesor.getEmail()
                    + "\nEspecialidad: " + profesor.getEspecialidad()
                    + "\nExperiencia: " + profesor.getExperiencia());
            infoLabel.getStyleClass().add("normal-text");
            infoLabel.setWrapText(true);

            tarjeta.getChildren().addAll(nombreLabel, infoLabel);
            listaProfesoresBox.getChildren().add(tarjeta);
        }
    }

    @FXML private void onVolverClick() { SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio"); }
}
