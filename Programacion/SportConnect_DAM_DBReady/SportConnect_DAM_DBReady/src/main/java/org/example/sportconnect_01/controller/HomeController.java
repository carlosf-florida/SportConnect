package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.sportconnect_01.model.AppConfig;
import org.example.sportconnect_01.model.Usuario;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.SceneManager;

public class HomeController {

    @FXML private Label appNameLabel;
    @FXML private Label bienvenidaLabel;
    @FXML private Label tipoUsuarioLabel;
    @FXML private Label mensajeInicioLabel;
    @FXML private Label resumenLabel;
    @FXML private Label footerLabel;
    @FXML private Button profesorButton;
    @FXML private Button adminButton;

    @FXML
    private void initialize() {
        AppConfig config = DataStore.getAppConfig();
        Usuario usuario = Session.getUsuarioActual();

        appNameLabel.setText("⚽ " + config.getNombreAplicacion());
        mensajeInicioLabel.setText(config.getMensajeInicio());
        resumenLabel.setText("Clases: " + DataStore.getClases().size()
                + "  ·  Profesores: " + DataStore.contarProfesoresUsuarios()
                + "  ·  Productos: " + DataStore.getProductos().size()
                + "  ·  Reservas: " + DataStore.contarReservasTotales());
        footerLabel.setText(config.getTextoFooter());

        if (usuario != null) {
            bienvenidaLabel.setText("Bienvenido, " + usuario.getNombreCompleto());
            tipoUsuarioLabel.setText("Rol: " + usuario.getTipo() + "  ·  Nick: " + usuario.getNick());

            boolean esProfesor = usuario.esProfesor() || usuario.esAdmin();
            boolean esAdmin = usuario.esAdmin();
            profesorButton.setVisible(esProfesor);
            profesorButton.setManaged(esProfesor);
            adminButton.setVisible(esAdmin);
            adminButton.setManaged(esAdmin);
        } else {
            bienvenidaLabel.setText("Bienvenido");
            tipoUsuarioLabel.setText("Rol: invitado");
            profesorButton.setVisible(false);
            profesorButton.setManaged(false);
            adminButton.setVisible(false);
            adminButton.setManaged(false);
        }
    }

    @FXML private void onVerClasesClick() { SceneManager.cambiarVista("clases-view.fxml", "SportConnect - Clases"); }
    @FXML private void onMisClasesClick() { SceneManager.cambiarVista("mis-clases-view.fxml", "SportConnect - Mis clases"); }
    @FXML private void onVerProfesoresClick() { SceneManager.cambiarVista("profesores-view.fxml", "SportConnect - Profesores"); }
    @FXML private void onProductosClick() { SceneManager.cambiarVista("productos-view.fxml", "SportConnect - Productos"); }
    @FXML private void onMiPerfilClick() { SceneManager.cambiarVista("perfil-view.fxml", "SportConnect - Perfil"); }
    @FXML private void onProfesorPanelClick() { SceneManager.cambiarVista("profesor-view.fxml", "SportConnect - Panel Profesor"); }
    @FXML private void onAdminClick() { SceneManager.cambiarVista("admin-view.fxml", "SportConnect - Panel Admin"); }

    @FXML
    private void onCerrarSesionClick() {
        Session.cerrarSesion();
        SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login");
    }
}
