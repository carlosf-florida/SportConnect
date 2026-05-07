package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        clasesProfesorListView.getSelectionModel().selectedIndexProperty()
                .addListener((obs, o, n) -> cargarClaseSeleccionada(n.intValue()));
    }

    private void cargarClasesProfesor() {
        Usuario usuario = Session.getUsuarioActual();
        clasesDelProfesor = DataStore.getClasesDelProfesor(usuario);
        clasesProfesorListView.getItems().clear();
        for (ClaseDeportiva c : clasesDelProfesor)
            clasesProfesorListView.getItems().add(c.getId() + " · " + c.getTitulo() + " · " + c.getDeporte() + " · " + c.getPrecioTexto());
        resumenProfesorLabel.setText("Tienes " + clasesDelProfesor.size() + " clase(s) creadas.");
    }

    private void cargarClaseSeleccionada(int pos) {
        if (pos < 0 || pos >= clasesDelProfesor.size()) return;
        ClaseDeportiva c = clasesDelProfesor.get(pos);
        tituloField.setText(c.getTitulo()); descripcionArea.setText(c.getDescripcion());
        deporteField.setText(c.getDeporte()); horarioField.setText(c.getHorario());
        nivelField.setText(c.getNivel()); plazasField.setText(String.valueOf(c.getPlazas()));
        precioField.setText(String.valueOf(c.getPrecio())); premiumCheck.setSelected(c.isPremium());
    }

    @FXML
    private void onCrearClaseClick() {
        ClaseDeportiva clase = crearClaseDesdeFormulario(true); if (clase == null) return;
        try { DataStore.agregarClase(clase); limpiarFormulario(); cargarClasesProfesor(); AlertUtils.mostrarInfo("Clase creada", "La clase se ha creado correctamente."); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al crear clase: " + e.getMessage()); }
    }

    @FXML
    private void onModificarClaseClick() {
        int posLocal = clasesProfesorListView.getSelectionModel().getSelectedIndex();
        if (posLocal < 0) { AlertUtils.mostrarError("Selecciona una clase de la lista."); return; }
        ClaseDeportiva original = clasesDelProfesor.get(posLocal);
        int posGeneral = DataStore.getIndiceClase(original);
        ClaseDeportiva actualizada = crearClaseDesdeFormulario(false); if (actualizada == null) return;
        actualizada.setId(original.getId());
        try { DataStore.actualizarClase(posGeneral, actualizada); cargarClasesProfesor(); AlertUtils.mostrarInfo("Clase actualizada", "Has modificado tu clase correctamente."); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al modificar clase: " + e.getMessage()); }
    }

    @FXML
    private void onEliminarClaseClick() {
        int posLocal = clasesProfesorListView.getSelectionModel().getSelectedIndex();
        if (posLocal < 0) { AlertUtils.mostrarError("Selecciona una clase para eliminar."); return; }
        int posGeneral = DataStore.getIndiceClase(clasesDelProfesor.get(posLocal));
        try { DataStore.eliminarClase(posGeneral); limpiarFormulario(); cargarClasesProfesor(); AlertUtils.mostrarInfo("Clase eliminada", "La clase se ha eliminado correctamente."); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al eliminar clase: " + e.getMessage()); }
    }

    private ClaseDeportiva crearClaseDesdeFormulario(boolean nueva) {
        Usuario profesor = Session.getUsuarioActual();
        String titulo  = tituloField.getText().trim();
        String desc    = descripcionArea.getText().trim();
        String deporte = deporteField.getText().trim();
        String horario = horarioField.getText().trim();
        String nivel   = nivelField.getText().trim();
        String plazasT = plazasField.getText().trim();
        String precioT = precioField.getText().trim();
        if (titulo.isEmpty()||desc.isEmpty()||deporte.isEmpty()||horario.isEmpty()||nivel.isEmpty()||plazasT.isEmpty()||precioT.isEmpty()) {
            AlertUtils.mostrarError("Rellena todos los campos de la clase."); return null;
        }
        try {
            int plazas = Integer.parseInt(plazasT);
            double precio = Double.parseDouble(precioT.replace(",","."));
            if (plazas < 0 || precio < 0) { AlertUtils.mostrarError("Las plazas y el precio no pueden ser negativos."); return null; }
            boolean premium = premiumCheck.isSelected() || precio > 0;
            int id = nueva ? 0 : clasesDelProfesor.get(clasesProfesorListView.getSelectionModel().getSelectedIndex()).getId();
            return new ClaseDeportiva(id, titulo, desc, deporte, profesor.getId(), profesor.getNombreCompleto(), premium, precio, plazas, horario, nivel, "");
        } catch (NumberFormatException e) { AlertUtils.mostrarError("Las plazas y el precio deben ser números válidos."); return null; }
    }

    @FXML private void onLimpiarClick() { limpiarFormulario(); clasesProfesorListView.getSelectionModel().clearSelection(); }
    private void limpiarFormulario() { tituloField.clear(); descripcionArea.clear(); deporteField.clear(); horarioField.clear(); nivelField.clear(); plazasField.clear(); precioField.clear(); premiumCheck.setSelected(false); }
    @FXML private void onVolverClick() { SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio"); }
}
