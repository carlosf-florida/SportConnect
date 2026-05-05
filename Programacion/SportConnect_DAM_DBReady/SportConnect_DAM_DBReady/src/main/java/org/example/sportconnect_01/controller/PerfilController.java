package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.sportconnect_01.model.Usuario;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.SceneManager;

public class PerfilController {

    @FXML private Label nombreLabel;
    @FXML private Label apellidoLabel;
    @FXML private Label nickLabel;
    @FXML private Label correoLabel;
    @FXML private Label tipoLabel;
    @FXML private Label idLabel;

    @FXML
    private void initialize() {
        Usuario usuario = Session.getUsuarioActual();
        if (usuario != null) {
            idLabel.setText("ID usuario: " + usuario.getId());
            nombreLabel.setText("Nombre: " + usuario.getNombre());
            apellidoLabel.setText("Apellido: " + usuario.getApellido());
            nickLabel.setText("Nick: " + usuario.getNick());
            correoLabel.setText("Correo: " + usuario.getCorreo());
            tipoLabel.setText("Rol: " + usuario.getTipo() + " (rol_id: " + usuario.getRolId() + ")");
        }
    }

    @FXML private void onVolverClick() { SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio"); }

    @FXML
    private void onCerrarSesionClick() {
        Session.cerrarSesion();
        SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login");
    }
}
