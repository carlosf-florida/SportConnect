package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.sportconnect_01.model.AppConfig;
import org.example.sportconnect_01.model.ClaseDeportiva;
import org.example.sportconnect_01.model.Producto;
import org.example.sportconnect_01.model.Usuario;
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
        clasesListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> cargarClaseSeleccionada(newValue.intValue()));
        productosListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> cargarProductoSeleccionado(newValue.intValue()));
        usuariosListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> cargarUsuarioSeleccionado(newValue.intValue()));
    }

    private void cargarListas() {
        cargarClases();
        cargarProductos();
        cargarUsuarios();
        actualizarResumen();
    }

    private void cargarClases() {
        clasesListView.getItems().clear();
        for (ClaseDeportiva clase : DataStore.getClases()) {
            clasesListView.getItems().add(clase.getId() + " · " + clase.getTitulo() + " · prof_id: " + clase.getProfesorId() + " · " + clase.getPrecioTexto());
        }
    }

    private void cargarProductos() {
        productosListView.getItems().clear();
        for (Producto producto : DataStore.getProductos()) {
            productosListView.getItems().add(producto.getId() + " · " + producto.getNombre() + " · Stock: " + producto.getStock() + " · " + producto.getPrecioTexto());
        }
    }

    private void cargarUsuarios() {
        usuariosListView.getItems().clear();
        for (Usuario usuario : DataStore.getUsuarios()) {
            usuariosListView.getItems().add(usuario.getId() + " · " + usuario.getNick() + " · " + usuario.getEmail() + " · " + usuario.getTipo());
        }
    }

    private void actualizarResumen() {
        resumenLabel.setText("Usuarios: " + DataStore.getUsuarios().size()
                + "  |  Alumnos: " + DataStore.contarAlumnos()
                + "  |  Profesores: " + DataStore.contarProfesoresUsuarios()
                + "  |  Clases: " + DataStore.getClases().size()
                + "  |  Productos: " + DataStore.getProductos().size()
                + "  |  Suscripciones: " + DataStore.contarReservasTotales());
    }

    private void cargarClaseSeleccionada(int posicion) {
        if (posicion >= 0 && posicion < DataStore.getClases().size()) {
            ClaseDeportiva clase = DataStore.getClases().get(posicion);
            claseTituloField.setText(clase.getTitulo());
            claseDescripcionArea.setText(clase.getDescripcion());
            claseDeporteField.setText(clase.getDeporte());
            claseProfesorIdField.setText(String.valueOf(clase.getProfesorId()));
            claseHorarioField.setText(clase.getHorario());
            claseNivelField.setText(clase.getNivel());
            clasePlazasField.setText(String.valueOf(clase.getPlazas()));
            clasePrecioField.setText(String.valueOf(clase.getPrecio()));
            clasePremiumCheck.setSelected(clase.isPremium());
        }
    }

    private void cargarProductoSeleccionado(int posicion) {
        if (posicion >= 0 && posicion < DataStore.getProductos().size()) {
            Producto producto = DataStore.getProductos().get(posicion);
            productoNombreField.setText(producto.getNombre());
            productoDescripcionArea.setText(producto.getDescripcion());
            productoPrecioField.setText(String.valueOf(producto.getPrecio()));
            productoStockField.setText(String.valueOf(producto.getStock()));
            productoVendedorIdField.setText(String.valueOf(producto.getVendedorId()));
        }
    }

    private void cargarUsuarioSeleccionado(int posicion) {
        if (posicion >= 0 && posicion < DataStore.getUsuarios().size()) {
            Usuario usuario = DataStore.getUsuarios().get(posicion);
            usuarioNombreField.setText(usuario.getNombre());
            usuarioApellidoField.setText(usuario.getApellido());
            usuarioNickField.setText(usuario.getNick());
            usuarioEmailField.setText(usuario.getEmail());
            usuarioPasswordField.setText(usuario.getPassword());
            usuarioRolCombo.setValue(usuario.getTipo());
        }
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
        ClaseDeportiva clase = crearClaseDesdeFormulario(0);
        if (clase == null) return;
        DataStore.agregarClase(clase);
        limpiarClase();
        cargarListas();
        AlertUtils.mostrarInfo("Clase añadida", "La clase se ha añadido correctamente.");
    }

    @FXML private void onActualizarClaseClick() {
        int posicion = clasesListView.getSelectionModel().getSelectedIndex();
        if (posicion < 0) { AlertUtils.mostrarError("Selecciona una clase."); return; }
        ClaseDeportiva clase = crearClaseDesdeFormulario(DataStore.getClases().get(posicion).getId());
        if (clase == null) return;
        DataStore.actualizarClase(posicion, clase);
        cargarListas();
        clasesListView.getSelectionModel().select(posicion);
        AlertUtils.mostrarInfo("Clase actualizada", "Los datos de la clase se han actualizado.");
    }

    @FXML private void onEliminarClaseClick() {
        int posicion = clasesListView.getSelectionModel().getSelectedIndex();
        if (posicion < 0) { AlertUtils.mostrarError("Selecciona una clase."); return; }
        DataStore.eliminarClase(posicion);
        limpiarClase();
        cargarListas();
    }

    private ClaseDeportiva crearClaseDesdeFormulario(int id) {
        String titulo = claseTituloField.getText().trim();
        String descripcion = claseDescripcionArea.getText().trim();
        String deporte = claseDeporteField.getText().trim();
        String profesorIdTexto = claseProfesorIdField.getText().trim();
        String horario = claseHorarioField.getText().trim();
        String nivel = claseNivelField.getText().trim();
        String plazasTexto = clasePlazasField.getText().trim();
        String precioTexto = clasePrecioField.getText().trim();

        if (titulo.isEmpty() || descripcion.isEmpty() || deporte.isEmpty() || profesorIdTexto.isEmpty() || plazasTexto.isEmpty() || precioTexto.isEmpty()) {
            AlertUtils.mostrarError("Rellena todos los campos de la clase.");
            return null;
        }

        try {
            int profesorId = Integer.parseInt(profesorIdTexto);
            int plazas = Integer.parseInt(plazasTexto);
            double precio = Double.parseDouble(precioTexto.replace(",", "."));
            Usuario profesor = DataStore.buscarUsuarioPorId(profesorId);
            if (profesor == null || !profesor.esProfesor()) {
                AlertUtils.mostrarError("El profesor_id debe pertenecer a un usuario con rol Profesor.");
                return null;
            }
            if (plazas < 0 || precio < 0) {
                AlertUtils.mostrarError("Las plazas y el precio no pueden ser negativos.");
                return null;
            }
            boolean premium = clasePremiumCheck.isSelected() || precio > 0;
            return new ClaseDeportiva(id, titulo, descripcion, deporte, profesorId, profesor.getNombreCompleto(), premium, precio, plazas, horario, nivel, "Nuevo registro");
        } catch (NumberFormatException e) {
            AlertUtils.mostrarError("profesor_id, plazas y precio deben ser números válidos.");
            return null;
        }
    }

    @FXML private void onLimpiarClaseClick() { limpiarClase(); clasesListView.getSelectionModel().clearSelection(); }
    private void limpiarClase() {
        claseTituloField.clear(); claseDescripcionArea.clear(); claseDeporteField.clear(); claseProfesorIdField.clear();
        claseHorarioField.clear(); claseNivelField.clear(); clasePlazasField.clear(); clasePrecioField.clear(); clasePremiumCheck.setSelected(false);
    }

    @FXML private void onNuevoProductoClick() {
        Producto producto = crearProductoDesdeFormulario(0);
        if (producto == null) return;
        DataStore.agregarProducto(producto);
        limpiarProducto(); cargarListas();
        AlertUtils.mostrarInfo("Producto añadido", "El producto se ha añadido correctamente.");
    }

    @FXML private void onActualizarProductoClick() {
        int posicion = productosListView.getSelectionModel().getSelectedIndex();
        if (posicion < 0) { AlertUtils.mostrarError("Selecciona un producto."); return; }
        Producto producto = crearProductoDesdeFormulario(DataStore.getProductos().get(posicion).getId());
        if (producto == null) return;
        DataStore.actualizarProducto(posicion, producto);
        cargarListas(); productosListView.getSelectionModel().select(posicion);
        AlertUtils.mostrarInfo("Producto actualizado", "Los datos del producto se han actualizado.");
    }

    @FXML private void onEliminarProductoClick() {
        int posicion = productosListView.getSelectionModel().getSelectedIndex();
        if (posicion < 0) { AlertUtils.mostrarError("Selecciona un producto."); return; }
        DataStore.eliminarProducto(posicion);
        limpiarProducto(); cargarListas();
    }

    private Producto crearProductoDesdeFormulario(int id) {
        String nombre = productoNombreField.getText().trim();
        String descripcion = productoDescripcionArea.getText().trim();
        String precioTexto = productoPrecioField.getText().trim();
        String stockTexto = productoStockField.getText().trim();
        String vendedorIdTexto = productoVendedorIdField.getText().trim();
        if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty() || stockTexto.isEmpty() || vendedorIdTexto.isEmpty()) {
            AlertUtils.mostrarError("Rellena todos los campos del producto.");
            return null;
        }
        try {
            double precio = Double.parseDouble(precioTexto.replace(",", "."));
            int stock = Integer.parseInt(stockTexto);
            int vendedorId = Integer.parseInt(vendedorIdTexto);
            Usuario vendedor = DataStore.buscarUsuarioPorId(vendedorId);
            if (vendedor == null || (!vendedor.esProfesor() && !vendedor.esAdmin())) {
                AlertUtils.mostrarError("El vendedor_id debe ser de un profesor o administrador.");
                return null;
            }
            if (precio < 0 || stock < 0) {
                AlertUtils.mostrarError("El precio y el stock no pueden ser negativos.");
                return null;
            }
            return new Producto(id, nombre, descripcion, precio, stock, vendedorId, vendedor.getNombreCompleto(), "Nuevo registro");
        } catch (NumberFormatException e) {
            AlertUtils.mostrarError("precio, stock y vendedor_id deben ser números válidos.");
            return null;
        }
    }

    @FXML private void onLimpiarProductoClick() { limpiarProducto(); productosListView.getSelectionModel().clearSelection(); }
    private void limpiarProducto() {
        productoNombreField.clear(); productoDescripcionArea.clear(); productoPrecioField.clear(); productoStockField.clear(); productoVendedorIdField.clear();
    }

    @FXML private void onNuevoUsuarioClick() {
        Usuario usuario = crearUsuarioDesdeFormulario(0);
        if (usuario == null) return;
        if (DataStore.correoExiste(usuario.getEmail()) || DataStore.nickExiste(usuario.getNick())) {
            AlertUtils.mostrarError("El email o nick ya existe.");
            return;
        }
        DataStore.registrarUsuario(usuario);
        limpiarUsuario(); cargarListas();
        AlertUtils.mostrarInfo("Usuario creado", "El usuario se ha creado correctamente.");
    }

    @FXML private void onActualizarUsuarioClick() {
        int posicion = usuariosListView.getSelectionModel().getSelectedIndex();
        if (posicion < 0) { AlertUtils.mostrarError("Selecciona un usuario."); return; }
        Usuario usuario = crearUsuarioDesdeFormulario(DataStore.getUsuarios().get(posicion).getId());
        if (usuario == null) return;
        DataStore.actualizarUsuario(posicion, usuario);
        cargarListas(); usuariosListView.getSelectionModel().select(posicion);
        AlertUtils.mostrarInfo("Usuario actualizado", "Los datos del usuario se han actualizado.");
    }

    @FXML private void onEliminarUsuarioClick() {
        int posicion = usuariosListView.getSelectionModel().getSelectedIndex();
        if (posicion < 0) { AlertUtils.mostrarError("Selecciona un usuario."); return; }
        Usuario seleccionado = DataStore.getUsuarios().get(posicion);
        Usuario actual = Session.getUsuarioActual();
        if (actual != null && seleccionado.getId() == actual.getId()) {
            AlertUtils.mostrarError("No puedes eliminar tu propia cuenta de administrador.");
            return;
        }
        DataStore.eliminarUsuario(posicion);
        limpiarUsuario(); cargarListas();
    }

    private Usuario crearUsuarioDesdeFormulario(int id) {
        String nombre = usuarioNombreField.getText().trim();
        String apellido = usuarioApellidoField.getText().trim();
        String nick = usuarioNickField.getText().trim();
        String email = usuarioEmailField.getText().trim();
        String password = usuarioPasswordField.getText().trim();
        String rol = usuarioRolCombo.getValue();
        if (nombre.isEmpty() || apellido.isEmpty() || nick.isEmpty() || email.isEmpty() || password.isEmpty() || rol == null) {
            AlertUtils.mostrarError("Rellena todos los campos del usuario.");
            return null;
        }
        int rolId = rol.equalsIgnoreCase("Admin") ? 1 : rol.equalsIgnoreCase("Profesor") ? 2 : 3;
        return new Usuario(id, nombre, apellido, nick, email, password, rolId, rol, "Nuevo registro");
    }

    @FXML private void onLimpiarUsuarioClick() { limpiarUsuario(); usuariosListView.getSelectionModel().clearSelection(); }
    private void limpiarUsuario() {
        usuarioNombreField.clear(); usuarioApellidoField.clear(); usuarioNickField.clear(); usuarioEmailField.clear(); usuarioPasswordField.clear(); usuarioRolCombo.setValue("Alumno");
    }

    @FXML private void onVolverClick() { SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio"); }
}
