package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.sportconnect_01.model.*;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

public class AdminController {

    @FXML private Label resumenLabel;
    @FXML private TextField appNombreField;
    @FXML private TextField appMensajeField;
    @FXML private TextField appFooterField;

    @FXML private ListView<String> clasesListView;
    @FXML private TextField claseTituloField;
    @FXML private TextArea claseDescripcionArea;
    @FXML private TextField claseDeporteField;
    @FXML private TextField claseProfesorIdField;
    @FXML private TextField claseHorarioField;
    @FXML private TextField claseNivelField;
    @FXML private TextField clasePlazasField;
    @FXML private TextField clasePrecioField;
    @FXML private CheckBox clasePremiumCheck;

    @FXML private ListView<String> productosListView;
    @FXML private TextField productoNombreField;
    @FXML private TextArea productoDescripcionArea;
    @FXML private TextField productoPrecioField;
    @FXML private TextField productoStockField;
    @FXML private TextField productoVendedorIdField;

    @FXML private ListView<String> usuariosListView;
    @FXML private TextField usuarioNombreField;
    @FXML private TextField usuarioApellidoField;
    @FXML private TextField usuarioNickField;
    @FXML private TextField usuarioEmailField;
    @FXML private TextField usuarioPasswordField;
    @FXML private ComboBox<String> usuarioRolCombo;

    @FXML
    private void initialize() {
        Usuario usuario = Session.getUsuarioActual();
        if (usuario == null || !usuario.esAdmin()) {
            AlertUtils.mostrarError("Solo un administrador puede entrar en esta pantalla.");
            SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio");
            return;
        }
        usuarioRolCombo.getItems().addAll("Admin", "Profesor", "Alumno");
        usuarioRolCombo.setValue("Alumno");
        cargarDatosAplicacion();
        cargarListas();
        prepararSelecciones();
    }

    private void cargarDatosAplicacion() {
        AppConfig config = DataStore.getAppConfig();
        appNombreField.setText(config.getNombreAplicacion());
        appMensajeField.setText(config.getMensajeInicio());
        appFooterField.setText(config.getTextoFooter());
    }

    private void prepararSelecciones() {
        clasesListView.getSelectionModel().selectedIndexProperty()
                .addListener((obs, o, n) -> cargarClaseSeleccionada(n.intValue()));
        productosListView.getSelectionModel().selectedIndexProperty()
                .addListener((obs, o, n) -> cargarProductoSeleccionado(n.intValue()));
        usuariosListView.getSelectionModel().selectedIndexProperty()
                .addListener((obs, o, n) -> cargarUsuarioSeleccionado(n.intValue()));
    }

    private void cargarListas() { cargarClases(); cargarProductos(); cargarUsuarios(); actualizarResumen(); }

    private void cargarClases() {
        clasesListView.getItems().clear();
        for (ClaseDeportiva c : DataStore.getClases())
            clasesListView.getItems().add(c.getId() + " · " + c.getTitulo() + " · prof_id: " + c.getProfesorId() + " · " + c.getPrecioTexto());
    }

    private void cargarProductos() {
        productosListView.getItems().clear();
        for (Producto p : DataStore.getProductos())
            productosListView.getItems().add(p.getId() + " · " + p.getNombre() + " · Stock: " + p.getStock() + " · " + p.getPrecioTexto());
    }

    private void cargarUsuarios() {
        usuariosListView.getItems().clear();
        for (Usuario u : DataStore.getUsuarios())
            usuariosListView.getItems().add(u.getId() + " · " + u.getNick() + " · " + u.getEmail() + " · " + u.getTipo());
    }

    private void actualizarResumen() {
        resumenLabel.setText("Usuarios: " + DataStore.getUsuarios().size()
                + "  |  Alumnos: " + DataStore.contarAlumnos()
                + "  |  Profesores: " + DataStore.contarProfesoresUsuarios()
                + "  |  Clases: " + DataStore.getClases().size()
                + "  |  Productos: " + DataStore.getProductos().size()
                + "  |  Suscripciones: " + DataStore.contarReservasTotales());
    }

    private void cargarClaseSeleccionada(int pos) {
        if (pos < 0 || pos >= DataStore.getClases().size()) return;
        ClaseDeportiva c = DataStore.getClases().get(pos);
        claseTituloField.setText(c.getTitulo()); claseDescripcionArea.setText(c.getDescripcion());
        claseDeporteField.setText(c.getDeporte()); claseProfesorIdField.setText(String.valueOf(c.getProfesorId()));
        claseHorarioField.setText(c.getHorario()); claseNivelField.setText(c.getNivel());
        clasePlazasField.setText(String.valueOf(c.getPlazas())); clasePrecioField.setText(String.valueOf(c.getPrecio()));
        clasePremiumCheck.setSelected(c.isPremium());
    }

    private void cargarProductoSeleccionado(int pos) {
        if (pos < 0 || pos >= DataStore.getProductos().size()) return;
        Producto p = DataStore.getProductos().get(pos);
        productoNombreField.setText(p.getNombre()); productoDescripcionArea.setText(p.getDescripcion());
        productoPrecioField.setText(String.valueOf(p.getPrecio())); productoStockField.setText(String.valueOf(p.getStock()));
        productoVendedorIdField.setText(String.valueOf(p.getVendedorId()));
    }

    private void cargarUsuarioSeleccionado(int pos) {
        if (pos < 0 || pos >= DataStore.getUsuarios().size()) return;
        Usuario u = DataStore.getUsuarios().get(pos);
        usuarioNombreField.setText(u.getNombre()); usuarioApellidoField.setText(u.getApellido());
        usuarioNickField.setText(u.getNick()); usuarioEmailField.setText(u.getEmail());
        usuarioPasswordField.setText(u.getPassword()); usuarioRolCombo.setValue(u.getTipo());
    }

    @FXML
    private void onGuardarAppClick() {
        if (appNombreField.getText().trim().isEmpty() || appMensajeField.getText().trim().isEmpty()) {
            AlertUtils.mostrarError("El nombre y el mensaje de inicio no pueden estar vacíos.");
            return;
        }
        AppConfig config = DataStore.getAppConfig();
        config.setNombreAplicacion(appNombreField.getText().trim());
        config.setMensajeInicio(appMensajeField.getText().trim());
        config.setTextoFooter(appFooterField.getText().trim());
        AlertUtils.mostrarInfo("Aplicación actualizada", "Los textos principales se han guardado.");
    }

    @FXML private void onNuevaClaseClick() {
        ClaseDeportiva c = crearClaseDesdeFormulario(0); if (c == null) return;
        try { DataStore.agregarClase(c); limpiarClase(); cargarListas(); AlertUtils.mostrarInfo("Clase añadida", "La clase se ha añadido correctamente."); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al añadir clase: " + e.getMessage()); }
    }

    @FXML private void onActualizarClaseClick() {
        int pos = clasesListView.getSelectionModel().getSelectedIndex();
        if (pos < 0) { AlertUtils.mostrarError("Selecciona una clase."); return; }
        ClaseDeportiva c = crearClaseDesdeFormulario(DataStore.getClases().get(pos).getId()); if (c == null) return;
        try { DataStore.actualizarClase(pos, c); cargarListas(); clasesListView.getSelectionModel().select(pos); AlertUtils.mostrarInfo("Clase actualizada", "Los datos de la clase se han actualizado."); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al actualizar clase: " + e.getMessage()); }
    }

    @FXML private void onEliminarClaseClick() {
        int pos = clasesListView.getSelectionModel().getSelectedIndex();
        if (pos < 0) { AlertUtils.mostrarError("Selecciona una clase."); return; }
        try { DataStore.eliminarClase(pos); limpiarClase(); cargarListas(); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al eliminar clase: " + e.getMessage()); }
    }

    private ClaseDeportiva crearClaseDesdeFormulario(int id) {
        String titulo = claseTituloField.getText().trim();
        String desc   = claseDescripcionArea.getText().trim();
        String deporte= claseDeporteField.getText().trim();
        String profId = claseProfesorIdField.getText().trim();
        String hor    = claseHorarioField.getText().trim();
        String nivel  = claseNivelField.getText().trim();
        String plazas = clasePlazasField.getText().trim();
        String precio = clasePrecioField.getText().trim();
        if (titulo.isEmpty()||desc.isEmpty()||deporte.isEmpty()||profId.isEmpty()||plazas.isEmpty()||precio.isEmpty()) {
            AlertUtils.mostrarError("Rellena todos los campos de la clase."); return null;
        }
        try {
            int profesorId = Integer.parseInt(profId);
            int pl = Integer.parseInt(plazas);
            double pr = Double.parseDouble(precio.replace(",", "."));
            Usuario prof = DataStore.buscarUsuarioPorId(profesorId);
            if (prof == null || !prof.esProfesor()) { AlertUtils.mostrarError("El profesor_id debe pertenecer a un Profesor."); return null; }
            if (pl < 0 || pr < 0) { AlertUtils.mostrarError("Las plazas y el precio no pueden ser negativos."); return null; }
            boolean premium = clasePremiumCheck.isSelected() || pr > 0;
            return new ClaseDeportiva(id, titulo, desc, deporte, profesorId, prof.getNombreCompleto(), premium, pr, pl, hor, nivel, "");
        } catch (NumberFormatException e) { AlertUtils.mostrarError("profesor_id, plazas y precio deben ser números válidos."); return null; }
    }

    @FXML private void onLimpiarClaseClick() { limpiarClase(); clasesListView.getSelectionModel().clearSelection(); }
    private void limpiarClase() {
        claseTituloField.clear(); claseDescripcionArea.clear(); claseDeporteField.clear(); claseProfesorIdField.clear();
        claseHorarioField.clear(); claseNivelField.clear(); clasePlazasField.clear(); clasePrecioField.clear(); clasePremiumCheck.setSelected(false);
    }

    @FXML private void onNuevoProductoClick() {
        Producto p = crearProductoDesdeFormulario(0); if (p == null) return;
        try { DataStore.agregarProducto(p); limpiarProducto(); cargarListas(); AlertUtils.mostrarInfo("Producto añadido", "El producto se ha añadido correctamente."); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al añadir producto: " + e.getMessage()); }
    }

    @FXML private void onActualizarProductoClick() {
        int pos = productosListView.getSelectionModel().getSelectedIndex();
        if (pos < 0) { AlertUtils.mostrarError("Selecciona un producto."); return; }
        Producto p = crearProductoDesdeFormulario(DataStore.getProductos().get(pos).getId()); if (p == null) return;
        try { DataStore.actualizarProducto(pos, p); cargarListas(); productosListView.getSelectionModel().select(pos); AlertUtils.mostrarInfo("Producto actualizado", "Los datos del producto se han actualizado."); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al actualizar producto: " + e.getMessage()); }
    }

    @FXML private void onEliminarProductoClick() {
        int pos = productosListView.getSelectionModel().getSelectedIndex();
        if (pos < 0) { AlertUtils.mostrarError("Selecciona un producto."); return; }
        try { DataStore.eliminarProducto(pos); limpiarProducto(); cargarListas(); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al eliminar producto: " + e.getMessage()); }
    }

    private Producto crearProductoDesdeFormulario(int id) {
        String nombre = productoNombreField.getText().trim();
        String desc   = productoDescripcionArea.getText().trim();
        String precioT= productoPrecioField.getText().trim();
        String stockT = productoStockField.getText().trim();
        String vendT  = productoVendedorIdField.getText().trim();
        if (nombre.isEmpty()||desc.isEmpty()||precioT.isEmpty()||stockT.isEmpty()||vendT.isEmpty()) {
            AlertUtils.mostrarError("Rellena todos los campos del producto."); return null;
        }
        try {
            double precio = Double.parseDouble(precioT.replace(",","."));
            int stock = Integer.parseInt(stockT);
            int vendId = Integer.parseInt(vendT);
            Usuario v = DataStore.buscarUsuarioPorId(vendId);
            if (v == null || (!v.esProfesor() && !v.esAdmin())) { AlertUtils.mostrarError("El vendedor_id debe ser de un profesor o administrador."); return null; }
            if (precio < 0 || stock < 0) { AlertUtils.mostrarError("El precio y el stock no pueden ser negativos."); return null; }
            return new Producto(id, nombre, desc, precio, stock, vendId, v.getNombreCompleto(), "");
        } catch (NumberFormatException e) { AlertUtils.mostrarError("precio, stock y vendedor_id deben ser números válidos."); return null; }
    }

    @FXML private void onLimpiarProductoClick() { limpiarProducto(); productosListView.getSelectionModel().clearSelection(); }
    private void limpiarProducto() {
        productoNombreField.clear(); productoDescripcionArea.clear(); productoPrecioField.clear(); productoStockField.clear(); productoVendedorIdField.clear();
    }

    @FXML private void onNuevoUsuarioClick() {
        Usuario u = crearUsuarioDesdeFormulario(0); if (u == null) return;
        if (DataStore.correoExiste(u.getEmail()) || DataStore.nickExiste(u.getNick())) { AlertUtils.mostrarError("El email o nick ya existe."); return; }
        try { DataStore.registrarUsuario(u); limpiarUsuario(); cargarListas(); AlertUtils.mostrarInfo("Usuario creado", "El usuario se ha creado correctamente."); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al crear usuario: " + e.getMessage()); }
    }

    @FXML private void onActualizarUsuarioClick() {
        int pos = usuariosListView.getSelectionModel().getSelectedIndex();
        if (pos < 0) { AlertUtils.mostrarError("Selecciona un usuario."); return; }
        Usuario u = crearUsuarioDesdeFormulario(DataStore.getUsuarios().get(pos).getId()); if (u == null) return;
        try { DataStore.actualizarUsuario(pos, u); cargarListas(); usuariosListView.getSelectionModel().select(pos); AlertUtils.mostrarInfo("Usuario actualizado", "Los datos del usuario se han actualizado."); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al actualizar usuario: " + e.getMessage()); }
    }

    @FXML private void onEliminarUsuarioClick() {
        int pos = usuariosListView.getSelectionModel().getSelectedIndex();
        if (pos < 0) { AlertUtils.mostrarError("Selecciona un usuario."); return; }
        Usuario sel = DataStore.getUsuarios().get(pos);
        Usuario actual = Session.getUsuarioActual();
        if (actual != null && sel.getId() == actual.getId()) { AlertUtils.mostrarError("No puedes eliminar tu propia cuenta de administrador."); return; }
        try { DataStore.eliminarUsuario(pos); limpiarUsuario(); cargarListas(); }
        catch (RuntimeException e) { AlertUtils.mostrarError("Error al eliminar usuario: " + e.getMessage()); }
    }

    private Usuario crearUsuarioDesdeFormulario(int id) {
        String nombre = usuarioNombreField.getText().trim();
        String apell  = usuarioApellidoField.getText().trim();
        String nick   = usuarioNickField.getText().trim();
        String email  = usuarioEmailField.getText().trim();
        String pass   = usuarioPasswordField.getText().trim();
        String rol    = usuarioRolCombo.getValue();
        if (nombre.isEmpty()||apell.isEmpty()||nick.isEmpty()||email.isEmpty()||pass.isEmpty()||rol==null) {
            AlertUtils.mostrarError("Rellena todos los campos del usuario."); return null;
        }
        int rolId = rol.equalsIgnoreCase("Admin") ? 1 : rol.equalsIgnoreCase("Profesor") ? 2 : 3;
        return new Usuario(id, nombre, apell, nick, email, pass, rolId, rol, "");
    }

    @FXML private void onLimpiarUsuarioClick() { limpiarUsuario(); usuariosListView.getSelectionModel().clearSelection(); }
    private void limpiarUsuario() {
        usuarioNombreField.clear(); usuarioApellidoField.clear(); usuarioNickField.clear();
        usuarioEmailField.clear(); usuarioPasswordField.clear(); usuarioRolCombo.setValue("Alumno");
    }

    @FXML private void onVolverClick() { SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio"); }
}
