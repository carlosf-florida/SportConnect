package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.sportconnect_01.model.Usuario;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

public class RegisterController {

    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private TextField nickField;
    @FXML private TextField correoField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField repetirPasswordField;
    @FXML private ComboBox<String> tipoCombo;
    @FXML private Label mensajeLabel;

    @FXML
    private void initialize() {
        tipoCombo.getItems().addAll("Alumno", "Profesor");
        tipoCombo.setValue("Alumno");
    }

    @FXML
    private void onCrearCuentaClick() {
        String nombre = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();
        String nick = nickField.getText().trim();
        String correo = correoField.getText().trim();
        String password = passwordField.getText();
        String repetirPassword = repetirPasswordField.getText();
        String tipo = tipoCombo.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || nick.isEmpty() || correo.isEmpty() || password.isEmpty() || repetirPassword.isEmpty()) {
            mensajeLabel.setText("Rellena todos los campos.");
            return;
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            mensajeLabel.setText("Introduce un correo válido.");
            return;
        }

        if (!password.equals(repetirPassword)) {
            mensajeLabel.setText("Las contraseñas no coinciden.");
            return;
        }

        if (DataStore.correoExiste(correo)) {
            mensajeLabel.setText("Ese correo ya está registrado.");
            return;
        }

        if (DataStore.nickExiste(nick)) {
            mensajeLabel.setText("Ese nick ya está registrado.");
            return;
        }

        int rolId = tipo.equalsIgnoreCase("Profesor") ? 2 : 3;
        Usuario nuevoUsuario = new Usuario(0, nombre, apellido, nick, correo, password, rolId, tipo, "Nuevo registro");
        DataStore.registrarUsuario(nuevoUsuario);

        AlertUtils.mostrarInfo("Registro correcto", "Cuenta creada correctamente. Ya puedes iniciar sesión.");
        SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login");
    }

    @FXML
    private void onVolverLoginClick() {
        SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login");
    }
}
