package org.example.sportconnect_01.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import org.example.sportconnect_01.model.ClaseDeportiva;
import org.example.sportconnect_01.model.MensajeChat;
import org.example.sportconnect_01.model.Producto;
import org.example.sportconnect_01.model.Usuario;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

import java.util.ArrayList;

public class HomeController {

    @FXML private Label appNameLabel;
    @FXML private Label bienvenidaLabel;
    @FXML private Label tipoUsuarioLabel;
    @FXML private Label roleStatusLabel;
    @FXML private Label chatEstadoLabel;
    @FXML private Label statClasesLabel;
    @FXML private Label statAlumnosLabel;
    @FXML private Label statProductosLabel;
    @FXML private Label deporteMesLabel;
    @FXML private Label deporteMesDescLabel;
    @FXML private Button profesorButton;
    @FXML private Button adminButton;
    @FXML private ComboBox<ClaseDeportiva> chatClaseCombo;
    @FXML private ListView<String> chatListView;
    @FXML private TextField chatMensajeField;
    @FXML private VBox clasesDeporteMesBox;
    @FXML private VBox productosDeporteMesBox;

    // Deporte del mes estático (puede rotarse por mes si se desea)
    private static final String DEPORTE_MES = "Boxeo";
    private static final String DEPORTE_MES_DESC =
            "Mejora tu condición física, reflejos y disciplina mental. " +
            "El boxeo trabaja todo el cuerpo y es ideal para cualquier nivel.";

    @FXML
    private void initialize() {
        Usuario usuario = Session.getUsuarioActual();

        // Label "SportConnect" simple (sin modo ni texto extra)
        appNameLabel.setText("SportConnect");

        statClasesLabel.setText(String.valueOf(DataStore.getClases().size()));
        statAlumnosLabel.setText(String.valueOf(DataStore.contarAlumnos()));
        statProductosLabel.setText(String.valueOf(DataStore.getProductos().size()));

        chatClaseCombo.setItems(FXCollections.observableArrayList(DataStore.getClases()));
        if (!chatClaseCombo.getItems().isEmpty()) chatClaseCombo.getSelectionModel().selectFirst();
        cargarChat();

        if (usuario != null) {
            bienvenidaLabel.setText("Hola, " + usuario.getNombreCompleto());
            tipoUsuarioLabel.setText("@" + usuario.getNick());
            actualizarVistaRol(usuario);
        } else {
            bienvenidaLabel.setText("Bienvenido");
            tipoUsuarioLabel.setText("");
            roleStatusLabel.setText("");
            if (profesorButton != null) { profesorButton.setVisible(false); profesorButton.setManaged(false); }
            if (adminButton != null)    { adminButton.setVisible(false);    adminButton.setManaged(false); }
        }

        cargarDeporteMes();
    }

    private void actualizarVistaRol(Usuario usuario) {
        boolean esProfesor = usuario.esProfesor() || usuario.esAdmin();
        boolean esAdmin    = usuario.esAdmin();
        if (profesorButton != null) { profesorButton.setVisible(esProfesor); profesorButton.setManaged(esProfesor); }
        if (adminButton != null)    { adminButton.setVisible(esAdmin);       adminButton.setManaged(esAdmin); }
        if (roleStatusLabel != null) roleStatusLabel.setText("Modo: " + usuario.getRol());
    }

    private void cargarDeporteMes() {
        deporteMesLabel.setText(DEPORTE_MES);
        deporteMesDescLabel.setText(DEPORTE_MES_DESC);

        // Cargar clases del deporte del mes
        clasesDeporteMesBox.getChildren().clear();
        ArrayList<ClaseDeportiva> clasesFiltradas = DataStore.buscarClases("", DEPORTE_MES);
        if (clasesFiltradas.isEmpty()) {
            // Mostrar todas si no hay del deporte específico
            clasesFiltradas = DataStore.getClases();
        }
        int maxClases = Math.min(clasesFiltradas.size(), 4);
        for (int i = 0; i < maxClases; i++) {
            ClaseDeportiva c = clasesFiltradas.get(i);
            VBox card = new VBox(4);
            card.setStyle("-fx-background-color: #0a1622; -fx-background-radius: 8; " +
                         "-fx-border-color: rgba(0,212,255,0.15); -fx-border-radius: 8; " +
                         "-fx-border-width: 1; -fx-padding: 10;");
            Label titulo = new Label("🏃 " + c.getTitulo());
            titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #00d4ff;");
            Label info = new Label("👨‍🏫 " + c.getProfesor() + "  ·  ⏰ " + c.getHorario());
            info.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
            Label precio = new Label("💰 " + c.getPrecioTexto() + "  ·  🪑 " + c.getPlazas() + " plazas");
            precio.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
            card.getChildren().addAll(titulo, info, precio);
            clasesDeporteMesBox.getChildren().add(card);
        }
        if (clasesFiltradas.isEmpty()) {
            Label vacio = new Label("No hay clases disponibles.");
            vacio.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");
            clasesDeporteMesBox.getChildren().add(vacio);
        }

        // Cargar productos relacionados (búsqueda por nombre del deporte)
        productosDeporteMesBox.getChildren().clear();
        ArrayList<Producto> productosFiltrados = DataStore.buscarProductos(DEPORTE_MES.toLowerCase());
        if (productosFiltrados.isEmpty()) {
            productosFiltrados = DataStore.getProductos();
        }
        int maxProd = Math.min(productosFiltrados.size(), 4);
        for (int i = 0; i < maxProd; i++) {
            Producto p = productosFiltrados.get(i);
            VBox card = new VBox(4);
            card.setStyle("-fx-background-color: #0a1622; -fx-background-radius: 8; " +
                         "-fx-border-color: rgba(99,102,241,0.20); -fx-border-radius: 8; " +
                         "-fx-border-width: 1; -fx-padding: 10;");
            Label nombre = new Label("🛒 " + p.getNombre());
            nombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #6366f1;");
            Label info = new Label(p.getPrecioTexto() + "  ·  Stock: " + p.getStock());
            info.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
            card.getChildren().addAll(nombre, info);
            productosDeporteMesBox.getChildren().add(card);
        }
        if (productosFiltrados.isEmpty()) {
            Label vacio = new Label("No hay productos relacionados.");
            vacio.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");
            productosDeporteMesBox.getChildren().add(vacio);
        }
    }

    private void cargarChat() {
        chatListView.getItems().clear();
        for (MensajeChat m : DataStore.getUltimosMensajes(10))
            chatListView.getItems().add(m.getVistaCorta());
        if (chatListView.getItems().isEmpty())
            chatListView.getItems().add("Todavía no hay mensajes en el chat.");
        chatEstadoLabel.setText("Chat general");
    }

    @FXML
    private void onEnviarChatClick() {
        Usuario usuario = Session.getUsuarioActual();
        ClaseDeportiva clase = chatClaseCombo.getValue();
        String texto = chatMensajeField.getText() == null ? "" : chatMensajeField.getText().trim();

        if (usuario == null) { AlertUtils.mostrarError("Debes iniciar sesión para enviar mensajes."); return; }
        if (clase == null)   { AlertUtils.mostrarError("Selecciona una clase para enviar el mensaje."); return; }
        if (texto.isEmpty()) return; // no alert, just ignore empty send

        try {
            DataStore.enviarMensaje(usuario.getId(), clase.getId(), texto);
            chatMensajeField.clear();
            cargarChat();
        } catch (RuntimeException e) {
            AlertUtils.mostrarError("No se pudo enviar el mensaje: " + e.getMessage());
        }
    }

    @FXML private void onVerClasesClick()      { SceneManager.cambiarVista("clases-view.fxml",    "SportConnect - Clases"); }
    @FXML private void onMisClasesClick()      { SceneManager.cambiarVista("mis-clases-view.fxml","SportConnect - Mis clases"); }
    @FXML private void onVerProfesoresClick()  { SceneManager.cambiarVista("profesores-view.fxml","SportConnect - Profesores"); }
    @FXML private void onProductosClick()      { SceneManager.cambiarVista("productos-view.fxml", "SportConnect - Tienda"); }
    @FXML private void onCarritoClick()        { SceneManager.cambiarVista("carrito-view.fxml",   "SportConnect - Carrito"); }
    @FXML private void onMiPerfilClick()       { SceneManager.cambiarVista("perfil-view.fxml",    "SportConnect - Perfil"); }
    @FXML private void onProfesorPanelClick()  { SceneManager.cambiarVista("profesor-view.fxml",  "SportConnect - Panel Profesor"); }
    @FXML private void onAdminClick()          { SceneManager.cambiarVista("admin-view.fxml",     "SportConnect - Panel Admin"); }
    @FXML private void onCerrarSesionClick()   { Session.cerrarSesion(); SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login"); }
}
