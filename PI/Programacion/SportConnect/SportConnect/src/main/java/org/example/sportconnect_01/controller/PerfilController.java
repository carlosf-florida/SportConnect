package org.example.sportconnect_01.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.sportconnect_01.model.CompraHistorial;
import org.example.sportconnect_01.model.Usuario;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

import java.util.ArrayList;

public class PerfilController {

    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private TextField nickField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> rolCombo;
    @FXML private Label rolLabel;
    @FXML private Label mensajeLabel;
    @FXML private VBox historialBox;
    @FXML private Label totalComprasLabel;

    @FXML
    private void initialize() {
        rolCombo.setItems(FXCollections.observableArrayList("Alumno", "Profesor"));
        Usuario usuario = Session.getUsuarioActual();
        if (usuario != null) {
            rolLabel.setText("Rol actual: " + usuario.getRol() + "  ·  Nick: " + usuario.getNick());
            nombreField.setText(usuario.getNombre());
            apellidoField.setText(usuario.getApellido());
            nickField.setText(usuario.getNick());
            emailField.setText(usuario.getEmail());
            rolCombo.setValue(usuario.getRol().equals("Admin") ? "Alumno" : usuario.getRol());
        }
        cargarHistorial();
    }

    private void cargarHistorial() {
        historialBox.getChildren().clear();
        Usuario usuario = Session.getUsuarioActual();
        if (usuario == null) return;

        ArrayList<CompraHistorial> compras = DataStore.getComprasDeUsuario(usuario.getId());
        double total = 0;
        if (compras.isEmpty()) {
            Label vacio = new Label("No has realizado ninguna compra todavía.");
            vacio.getStyleClass().add("muted-text");
            historialBox.getChildren().add(vacio);
        } else {
            for (CompraHistorial c : compras) {
                VBox tarjeta = new VBox(4);
                tarjeta.getStyleClass().add("list-card");
                Label nombre = new Label("📦 " + c.getNombreProducto() + " x" + c.getCantidad());
                nombre.getStyleClass().add("item-title");
                Label info = new Label(c.getPrecioUnitarioTexto() + " c/u  ·  Total: " + c.getTotalTexto() + "  ·  " + c.getFecha());
                info.getStyleClass().add("normal-text");
                tarjeta.getChildren().addAll(nombre, info);
                historialBox.getChildren().add(tarjeta);
                total += c.getTotal();
            }
        }
        totalComprasLabel.setText(String.format("Total gastado: %.2f €", total));
    }

    @FXML
    private void onGuardarClick() {
        Usuario usuario = Session.getUsuarioActual();
        if (usuario == null) return;

        String nombre   = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();
        String nick     = nickField.getText().trim();
        String email    = emailField.getText().trim();
        String rolNuevo = rolCombo.getValue();

        if (nombre.isEmpty() || nick.isEmpty() || email.isEmpty()) {
            mensajeLabel.setText("❌ Nombre, nick y email son obligatorios.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            mensajeLabel.setText("❌ El correo no tiene un formato válido.");
            return;
        }

        // Verificar nick duplicado (ignorando el propio usuario)
        for (Usuario u : DataStore.getUsuarios()) {
            if (u.getId() != usuario.getId() && u.getNick().equalsIgnoreCase(nick)) {
                mensajeLabel.setText("❌ Ese nick ya está en uso.");
                return;
            }
        }

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setNick(nick);
        usuario.setEmail(email);
        if (rolNuevo != null && !usuario.esAdmin()) usuario.setRol(rolNuevo);

        try {
            DataStore.actualizarUsuarioPorId(usuario);
            Session.setUsuarioActual(usuario);
            rolLabel.setText("Rol actual: " + usuario.getRol() + "  ·  Nick: " + usuario.getNick());
            mensajeLabel.setText("✅ Perfil actualizado correctamente.");
        } catch (RuntimeException e) {
            mensajeLabel.setText("❌ Error al guardar: " + e.getMessage());
        }
    }

    @FXML private void onVerCarritoClick()    { SceneManager.cambiarVista("carrito-view.fxml",  "SportConnect - Carrito"); }
    @FXML private void onVolverClick()        { SceneManager.cambiarVista("home-view.fxml",      "SportConnect - Inicio"); }
    @FXML private void onCerrarSesionClick()  { Session.cerrarSesion(); SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login"); }
}
