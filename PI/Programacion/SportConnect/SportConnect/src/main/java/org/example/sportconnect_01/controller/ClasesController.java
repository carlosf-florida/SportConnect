package org.example.sportconnect_01.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.sportconnect_01.model.ClaseDeportiva;
import org.example.sportconnect_01.model.Usuario;
import org.example.sportconnect_01.model.Valoracion;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ClasesController {

    @FXML private VBox listaClasesBox;
    @FXML private TextField busquedaField;
    @FXML private TextField busquedaProfesorField;
    @FXML private ComboBox<String> filtroDeporteCombo;
    @FXML private Label resultadosLabel;
    @FXML private Label sidebarUserLabel;

    @FXML
    private void initialize() {
        LinkedHashSet<String> deportes = new LinkedHashSet<>();
        deportes.add("Todos");
        for (ClaseDeportiva c : DataStore.getClases()) deportes.add(c.getDeporte());
        filtroDeporteCombo.setItems(FXCollections.observableArrayList(new ArrayList<>(deportes)));
        filtroDeporteCombo.setValue("Todos");
        cargarClases(DataStore.getClases());
        // Sidebar user info
        Usuario u = Session.getUsuarioActual();
        if (sidebarUserLabel != null && u != null) sidebarUserLabel.setText("Hola, " + u.getNombreCompleto());
    }

    private void cargarClases(ArrayList<ClaseDeportiva> lista) {
        listaClasesBox.getChildren().clear();
        resultadosLabel.setText(lista.size() + " clase(s) encontrada(s)");
        Usuario usuario = Session.getUsuarioActual();

        if (lista.isEmpty()) {
            Label vacio = new Label("No se encontraron clases.");
            vacio.getStyleClass().add("muted-text");
            listaClasesBox.getChildren().add(vacio);
            return;
        }

        for (ClaseDeportiva clase : lista) {
            VBox tarjeta = new VBox(8);
            tarjeta.getStyleClass().add("list-card");

            Label nombreLabel = new Label(clase.getTitulo());
            nombreLabel.getStyleClass().add("item-title");

            double media = DataStore.getMediaValoraciones(clase.getId());
            String estrellas = media == 0
                    ? "Sin valoraciones"
                    : String.format("%.1f ★ (%d valoraciones)", media, DataStore.getValoracionesDeClase(clase.getId()).size());

            Label infoLabel = new Label(
                    "🏃 " + clase.getDeporte() + "  ·  👨‍🏫 " + clase.getProfesor()
                    + "\n⏰ " + clase.getHorario() + "  ·  📊 " + clase.getNivel()
                    + "  ·  🪑 Plazas: " + clase.getPlazas()
                    + "\n💰 " + clase.getPrecioTexto() + "  ·  ⭐ " + estrellas);
            infoLabel.getStyleClass().add("normal-text");
            infoLabel.setWrapText(true);

            HBox botonesBox = new HBox(8);

            if (usuario != null && usuario.esAlumno()) {
                boolean apuntado = DataStore.estaApuntado(usuario, clase);
                Button inscribirBtn = new Button(apuntado ? "✓ Apuntado" : "+ Apuntarme");
                inscribirBtn.getStyleClass().add(apuntado ? "secondary-button" : "primary-button");
                inscribirBtn.setMaxWidth(140.0);
                inscribirBtn.setDisable(apuntado);
                if (!apuntado) inscribirBtn.setOnAction(e -> inscribirEnClase(clase));
                botonesBox.getChildren().add(inscribirBtn);

                if (apuntado && !DataStore.yaValoroClase(usuario.getId(), clase.getId())) {
                    Button valorarBtn = new Button("⭐ Valorar");
                    valorarBtn.getStyleClass().add("secondary-button");
                    valorarBtn.setMaxWidth(120.0);
                    valorarBtn.setOnAction(e -> mostrarDialogoValoracion(clase));
                    botonesBox.getChildren().add(valorarBtn);
                }
            }

            tarjeta.getChildren().addAll(nombreLabel, infoLabel, botonesBox);
            listaClasesBox.getChildren().add(tarjeta);
        }
    }

    private void inscribirEnClase(ClaseDeportiva clase) {
        Usuario usuario = Session.getUsuarioActual();
        if (!clase.hayPlazas()) { AlertUtils.mostrarError("No quedan plazas disponibles."); return; }
        try {
            if (DataStore.apuntarUsuarioAClase(usuario, clase)) {
                AlertUtils.mostrarInfo("¡Inscrito!", "Te has apuntado a " + clase.getTitulo() + ".");
                cargarClases(obtenerListaFiltrada());
            } else {
                AlertUtils.mostrarError("No se pudo completar la inscripción. Puede que ya estés apuntado.");
            }
        } catch (RuntimeException e) {
            AlertUtils.mostrarError("Error al inscribirse: " + e.getMessage());
        }
    }

    private void mostrarDialogoValoracion(ClaseDeportiva clase) {
        Usuario usuario = Session.getUsuarioActual();
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Valorar clase");
        dialog.setHeaderText("¿Cuántas estrellas le das a " + clase.getTitulo() + "?");

        ComboBox<Integer> estrellasCombo = new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        estrellasCombo.setValue(5);
        estrellasCombo.setStyle("-fx-background-color: #07130d; -fx-text-fill: #f0fdf4;");
        dialog.getDialogPane().setContent(estrellasCombo);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK ? estrellasCombo.getValue() : null);

        dialog.showAndWait().ifPresent(estrellas -> {
            try {
                Valoracion v = new Valoracion(DataStore.getSiguienteValoracionId(), usuario.getId(),
                        clase.getId(), estrellas, "", java.time.LocalDate.now().toString());
                DataStore.agregarValoracion(v);
                AlertUtils.mostrarInfo("¡Gracias!", "Tu valoración de " + estrellas + " estrella(s) ha sido guardada.");
                cargarClases(obtenerListaFiltrada());
            } catch (RuntimeException e) {
                AlertUtils.mostrarError("No se pudo guardar la valoración: " + e.getMessage());
            }
        });
    }

    private ArrayList<ClaseDeportiva> obtenerListaFiltrada() {
        String texto    = busquedaField.getText() == null ? "" : busquedaField.getText().trim();
        String profesor = busquedaProfesorField != null && busquedaProfesorField.getText() != null
                        ? busquedaProfesorField.getText().trim().toLowerCase() : "";
        String deporte  = filtroDeporteCombo.getValue();
        ArrayList<ClaseDeportiva> lista = DataStore.buscarClases(texto, deporte);
        if (!profesor.isEmpty()) {
            ArrayList<ClaseDeportiva> filtrado = new ArrayList<>();
            for (ClaseDeportiva c : lista) {
                if (c.getProfesor() != null && c.getProfesor().toLowerCase().contains(profesor))
                    filtrado.add(c);
            }
            return filtrado;
        }
        return lista;
    }

    @FXML private void onBuscarClick()        { cargarClases(obtenerListaFiltrada()); }
    @FXML private void onBuscarKeyReleased()  { cargarClases(obtenerListaFiltrada()); }
    @FXML private void onLimpiarFiltros()     { busquedaField.clear(); if (busquedaProfesorField != null) busquedaProfesorField.clear(); filtroDeporteCombo.setValue("Todos"); cargarClases(DataStore.getClases()); }
    @FXML private void onVolverClick()         { SceneManager.cambiarVista("home-view.fxml",     "SportConnect - Inicio"); }
    @FXML private void onMiPerfilClick()       { SceneManager.cambiarVista("perfil-view.fxml",    "SportConnect - Perfil"); }
    @FXML private void onMisClasesClick()      { SceneManager.cambiarVista("mis-clases-view.fxml","SportConnect - Mis clases"); }
    @FXML private void onProductosClick()      { SceneManager.cambiarVista("productos-view.fxml", "SportConnect - Tienda"); }
    @FXML private void onCarritoClick()        { SceneManager.cambiarVista("carrito-view.fxml",   "SportConnect - Carrito"); }
    @FXML private void onProfesoresClick()     { SceneManager.cambiarVista("profesores-view.fxml","SportConnect - Profesores"); }
    @FXML private void onCerrarSesionClick()   { Session.cerrarSesion(); SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login"); }
}
