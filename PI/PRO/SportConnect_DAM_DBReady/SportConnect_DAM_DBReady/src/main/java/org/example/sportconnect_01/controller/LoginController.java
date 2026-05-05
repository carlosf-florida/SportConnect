package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.sportconnect_01.model.Usuario;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

public class LoginController {

    @FXML private TextField correoField;
    @FXML private PasswordField passwordField;
    @FXML private Label mensajeLabel;

    @FXML
    private void onEntrarClick() {
        String correo = correoField.getText().trim();
        String password = passwordField.getText();

        if (correo.isEmpty() || password.isEmpty()) {
            mensajeLabel.setText("Rellena todos los campos.");
            return;
        }

        Usuario usuario = DataStore.iniciarSesion(correo, password);

        if (usuario != null) {
            Session.setUsuarioActual(usuario);
            SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio");
        } else {
            mensajeLabel.setText("Correo o contraseña incorrectos.");
        }
    }

    @FXML
    private void onRegistrarseClick() {
        SceneManager.cambiarVista("register-view.fxml", "SportConnect - Registro");
    }

    @FXML
    private void onAyudaClick() {
        AlertUtils.mostrarInfo("Usuarios de prueba",
                "Admin:\naniri@mail.com / 123456\n\n" +
                "Profesor:\nana@mail.com / 123456\n\n" +
                "Alumno:\ncarlos@mail.com / 123456");
    }
}
