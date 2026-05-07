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
    @FXML private Label modoConexionLabel;

    @FXML
    private void initialize() {
        if (DataStore.isUsandoBaseDeDatos()) {
            modoConexionLabel.setText("🟢 Conectado a MySQL");
            modoConexionLabel.setStyle("-fx-text-fill: #22c55e;");
        } else {
            String err = DataStore.getErrorConexion();
            modoConexionLabel.setText("🔴 Sin MySQL — Modo local" + (err != null ? "\n" + abreviarError(err) : ""));
            modoConexionLabel.setStyle("-fx-text-fill: #f59e0b;");
        }
    }

    /** Acorta el mensaje de error para no saturar la UI */
    private String abreviarError(String error) {
        if (error == null) return "";
        if (error.length() > 80) return error.substring(0, 80) + "…";
        return error;
    }

    @FXML
    private void onEntrarClick() {
        String correo   = correoField.getText().trim();
        String password = passwordField.getText();

        if (correo.isEmpty() || password.isEmpty()) {
            mensajeLabel.setText("Rellena todos los campos.");
            return;
        }

        try {
            Usuario usuario = DataStore.iniciarSesion(correo, password);
            if (usuario != null) {
                Session.setUsuarioActual(usuario);
                SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio");
            } else {
                mensajeLabel.setText("Correo o contraseña incorrectos.");
            }
        } catch (Exception e) {
            mensajeLabel.setText("Error al conectar: " + e.getMessage());
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
