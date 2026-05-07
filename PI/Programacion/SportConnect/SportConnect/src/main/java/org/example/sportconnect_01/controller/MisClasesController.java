package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    @FXML private Label sidebarUserLabel;
    @FXML private VBox listaMisClasesBox;
    @FXML private VBox calendarioBox;

    @FXML
    private void initialize() {
        Usuario _u = Session.getUsuarioActual();
        if (sidebarUserLabel != null && _u != null) sidebarUserLabel.setText("Hola, " + _u.getNombreCompleto());
        cargarMisClases();
    }

    private void cargarMisClases() {
        listaMisClasesBox.getChildren().clear();
        calendarioBox.getChildren().clear();

        Usuario usuario = Session.getUsuarioActual();

        if (usuario == null) {
            resumenMisClasesLabel.setText("Inicia sesión para ver tus clases.");
            return;
        }

        if (usuario.esProfesor()) {
            // Para profesores mostramos sus clases impartidas
            ArrayList<ClaseDeportiva> clasesProfesor = DataStore.getClasesDelProfesor(usuario);
            resumenMisClasesLabel.setText("Impartes " + clasesProfesor.size() + " clase(s).");
            mostrarTarjetasClases(clasesProfesor, false);
            mostrarCalendario(clasesProfesor);
            return;
        }

        ArrayList<ClaseDeportiva> clasesApuntadas = DataStore.getClasesApuntadas(usuario);
        resumenMisClasesLabel.setText("Tienes " + clasesApuntadas.size() + " clase(s) apuntada(s).");

        if (clasesApuntadas.isEmpty()) {
            VBox vacia = new VBox(8);
            vacia.getStyleClass().add("list-card");
            Label msg = new Label("No te has apuntado a ninguna clase todavía.");
            msg.getStyleClass().add("normal-text");
            Label ayuda = new Label("Puedes apuntarte desde 'Ver clases'.");
            ayuda.getStyleClass().add("muted-text");
            vacia.getChildren().addAll(msg, ayuda);
            listaMisClasesBox.getChildren().add(vacia);
        } else {
            mostrarTarjetasClases(clasesApuntadas, true);
            mostrarCalendario(clasesApuntadas);
        }
    }

    private void mostrarTarjetasClases(ArrayList<ClaseDeportiva> clases, boolean mostrarCancelar) {
        for (ClaseDeportiva clase : clases) {
            VBox tarjeta = new VBox(8);
            tarjeta.getStyleClass().add("list-card");

            Label nombre = new Label(clase.getTitulo());
            nombre.getStyleClass().add("item-title");

            double media = DataStore.getMediaValoraciones(clase.getId());
            String estrellas = media == 0 ? "Sin valoraciones" : String.format("%.1f ★", media);

            Label info = new Label("🏃 " + clase.getDeporte()
                    + "  ·  ⏰ " + clase.getHorario()
                    + "  ·  📊 " + clase.getNivel()
                    + "  ·  ⭐ " + estrellas);
            info.getStyleClass().add("normal-text");
            info.setWrapText(true);

            tarjeta.getChildren().addAll(nombre, info);

            if (mostrarCancelar) {
                Button cancelarBtn = new Button("Cancelar");
                cancelarBtn.getStyleClass().add("danger-button");
                cancelarBtn.setMaxWidth(200.0);
                cancelarBtn.setOnAction(e -> cancelarInscripcion(clase));
                tarjeta.getChildren().add(cancelarBtn);
            }

            listaMisClasesBox.getChildren().add(tarjeta);
        }
    }

    private void mostrarCalendario(ArrayList<ClaseDeportiva> clases) {
        // Ordenar por horario (como texto, suficiente para este proyecto)
        ArrayList<ClaseDeportiva> ordenadas = new ArrayList<>(clases);
        ordenadas.sort((a, b) -> a.getHorario().compareTo(b.getHorario()));

        // Agrupar por día de la semana
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        for (String dia : dias) {
            boolean tieneClases = false;
            for (ClaseDeportiva c : ordenadas) {
                if (c.getHorario().startsWith(dia)) {
                    if (!tieneClases) {
                        Label diaLabel = new Label("📅 " + dia);
                        diaLabel.getStyleClass().add("section-title");
                        calendarioBox.getChildren().add(diaLabel);
                        tieneClases = true;
                    }
                    String hora = c.getHorario().contains(" ") ? c.getHorario().split(" ")[1] : c.getHorario();
                    VBox evento = new VBox(2);
                    evento.getStyleClass().add("list-card");
                    Label eventoLabel = new Label("  🕐 " + hora + "  →  " + c.getTitulo());
                    eventoLabel.getStyleClass().add("normal-text");
                    Label profeLabel = new Label("  👨‍🏫 " + c.getProfesor());
                    profeLabel.getStyleClass().add("muted-text");
                    evento.getChildren().addAll(eventoLabel, profeLabel);
                    calendarioBox.getChildren().add(evento);
                }
            }
        }

        if (calendarioBox.getChildren().isEmpty()) {
            Label vacio = new Label("No hay clases en el calendario.");
            vacio.getStyleClass().add("muted-text");
            calendarioBox.getChildren().add(vacio);
        }
    }

    private void cancelarInscripcion(ClaseDeportiva clase) {
        Usuario usuario = Session.getUsuarioActual();
        if (DataStore.cancelarInscripcion(usuario, clase)) {
            AlertUtils.mostrarInfo("Inscripción cancelada", "Has cancelado la inscripción a " + clase.getTitulo() + ".");
            cargarMisClases();
        } else {
            AlertUtils.mostrarError("No se pudo cancelar la inscripción.");
        }
    }

    @FXML private void onVerMasClasesClick() { SceneManager.cambiarVista("clases-view.fxml",    "SportConnect - Clases"); }
    @FXML private void onVolverClick()        { SceneManager.cambiarVista("home-view.fxml",     "SportConnect - Inicio"); }
    @FXML private void onMiPerfilClick()      { SceneManager.cambiarVista("perfil-view.fxml",   "SportConnect - Perfil"); }
    @FXML private void onProductosClick()     { SceneManager.cambiarVista("productos-view.fxml","SportConnect - Tienda"); }
    @FXML private void onCarritoClick()       { SceneManager.cambiarVista("carrito-view.fxml",  "SportConnect - Carrito"); }
    @FXML private void onCerrarSesionClick()  { Session.cerrarSesion(); SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login"); }
}
