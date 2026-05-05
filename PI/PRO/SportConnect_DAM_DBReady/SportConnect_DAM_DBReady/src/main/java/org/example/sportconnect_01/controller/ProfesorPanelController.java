package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.sportconnect_01.model.ClaseDeportiva;
import org.example.sportconnect_01.model.Usuario;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

import java.util.ArrayList;

public class ProfesorPanelController {

    @FXML private Label profesorTituloLabel;
    @FXML private Label resumenProfesorLabel;
    @FXML private ListView<String> clasesProfesorListView;
    @FXML private TextField tituloField;
    @FXML private TextArea descripcionArea;
    @FXML private TextField deporteField;
    @FXML private TextField horarioField;
    @FXML private TextField nivelField;
    @FXML private TextField plazasField;
    @FXML private TextField precioField;
    @FXML private CheckBox premiumCheck;

    private ArrayList<ClaseDeportiva> clasesDelProfesor = new ArrayList<>();

    @FXML
    private void initialize() {
        Usuario usuario = Session.getUsuarioActual();
        if (usuario == null || (!usuario.esProfesor() && !usuario.esAdmin())) {
            AlertUtils.mostrarError("Solo un profesor puede entrar en este panel.");
            SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio");
            return;
        }

        profesorTituloLabel.setText("Panel profesor · " + usuario.getNombreCompleto());
        cargarClasesProfesor();
        clasesProfesorListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> cargarClaseSeleccionada(newValue.intValue()));
    }

    private void cargarClasesProfesor() {
        Usuario usuario = Session.getUsuarioActual();
        clasesDelProfesor = DataStore.getClasesDelProfesor(usuario);
        clasesProfesorListView.getItems().clear();

        for (ClaseDeportiva clase : clasesDelProfesor) {
            clasesProfesorListView.getItems().add(clase.getId() + " · " + clase.getTitulo() + " · " + clase.getDeporte() + " · " + clase.getPrecioTexto());
        }

        resumenProfesorLabel.setText("Tienes " + clasesDelProfesor.size() + " clase(s) creadas. Puedes añadir nuevas o modificar las tuyas.");
    }

    private void cargarClaseSeleccionada(int posicion) {
        if (posicion >= 0 && posicion < clasesDelProfesor.size()) {
            ClaseDeportiva clase = clasesDelProfesor.get(posicion);
            tituloField.setText(clase.getTitulo());
            descripcionArea.setText(clase.getDescripcion());
            deporteField.setText(clase.getDeporte());
            horarioField.setText(clase.getHorario());
            nivelField.setText(clase.getNivel());
            plazasField.setText(String.valueOf(clase.getPlazas()));
            precioField.setText(String.valueOf(clase.getPrecio()));
            premiumCheck.setSelected(clase.isPremium());
        }
    }

    @FXML
    private void onCrearClaseClick() {
        ClaseDeportiva clase = crearClaseDesdeFormulario(true);
        if (clase == null) return;

        DataStore.agregarClase(clase);
        limpiarFormulario();
        cargarClasesProfesor();
        AlertUtils.mostrarInfo("Clase creada", "La clase se ha creado correctamente.");
    }

    @FXML
    private void onModificarClaseClick() {
        int posicionLocal = clasesProfesorListView.getSelectionModel().getSelectedIndex();
        if (posicionLocal < 0) {
            AlertUtils.mostrarError("Selecciona una clase de la lista.");
            return;
        }

        ClaseDeportiva claseOriginal = clasesDelProfesor.get(posicionLocal);
        int posicionGeneral = DataStore.getIndiceClase(claseOriginal);
        ClaseDeportiva claseActualizada = crearClaseDesdeFormulario(false);
        if (claseActualizada == null) return;
        claseActualizada.setId(claseOriginal.getId());

        DataStore.actualizarClase(posicionGeneral, claseActualizada);
        cargarClasesProfesor();
        AlertUtils.mostrarInfo("Clase actualizada", "Has modificado tu clase correctamente.");
    }

    @FXML
    private void onEliminarClaseClick() {
        int posicionLocal = clasesProfesorListView.getSelectionModel().getSelectedIndex();
        if (posicionLocal < 0) {
            AlertUtils.mostrarError("Selecciona una clase para eliminar.");
            return;
        }
        int posicionGeneral = DataStore.getIndiceClase(clasesDelProfesor.get(posicionLocal));
        DataStore.eliminarClase(posicionGeneral);
        limpiarFormulario();
        cargarClasesProfesor();
        AlertUtils.mostrarInfo("Clase eliminada", "La clase se ha eliminado correctamente.");
    }

    private ClaseDeportiva crearClaseDesdeFormulario(boolean nueva) {
        Usuario profesor = Session.getUsuarioActual();
        String titulo = tituloField.getText().trim();
        String descripcion = descripcionArea.getText().trim();
        String deporte = deporteField.getText().trim();
        String horario = horarioField.getText().trim();
        String nivel = nivelField.getText().trim();
        String plazasTexto = plazasField.getText().trim();
        String precioTexto = precioField.getText().trim();

        if (titulo.isEmpty() || descripcion.isEmpty() || deporte.isEmpty() || horario.isEmpty() || nivel.isEmpty() || plazasTexto.isEmpty() || precioTexto.isEmpty()) {
            AlertUtils.mostrarError("Rellena todos los campos de la clase.");
            return null;
        }

        try {
            int plazas = Integer.parseInt(plazasTexto);
            double precio = Double.parseDouble(precioTexto.replace(",", "."));
            if (plazas < 0 || precio < 0) {
                AlertUtils.mostrarError("Las plazas y el precio no pueden ser negativos.");
                return null;
            }
            boolean premium = premiumCheck.isSelected() || precio > 0;
            int id = nueva ? 0 : clasesDelProfesor.get(clasesProfesorListView.getSelectionModel().getSelectedIndex()).getId();
            return new ClaseDeportiva(id, titulo, descripcion, deporte, profesor.getId(), profesor.getNombreCompleto(), premium, precio, plazas, horario, nivel, "Nuevo registro");
        } catch (NumberFormatException e) {
            AlertUtils.mostrarError("Las plazas deben ser un número entero y el precio un número válido.");
            return null;
        }
    }

    @FXML
    private void onLimpiarClick() {
        limpiarFormulario();
        clasesProfesorListView.getSelectionModel().clearSelection();
    }

    private void limpiarFormulario() {
        tituloField.clear();
        descripcionArea.clear();
        deporteField.clear();
        horarioField.clear();
        nivelField.clear();
        plazasField.clear();
        precioField.clear();
        premiumCheck.setSelected(false);
    }

    @FXML private void onVolverClick() { SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio"); }
}
