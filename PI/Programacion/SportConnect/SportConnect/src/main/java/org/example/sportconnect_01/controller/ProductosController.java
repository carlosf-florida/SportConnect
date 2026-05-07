package org.example.sportconnect_01.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.sportconnect_01.model.Producto;
import org.example.sportconnect_01.service.Carrito;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ProductosController {

    @FXML private VBox listaProductosBox;
    @FXML private TextField busquedaField;
    @FXML private ComboBox<String> filtroCombo;
    @FXML private Label carritoInfoLabel;
    @FXML private Label sidebarUserLabel;

    @FXML
    private void initialize() {
        // Llenar filtro de tipos
        LinkedHashSet<String> tipos = new LinkedHashSet<>();
        tipos.add("Todos");
        for (Producto p : DataStore.getProductos()) tipos.add(p.getDescripcion().split(" ")[0]);
        filtroCombo.setItems(FXCollections.observableArrayList(new ArrayList<>(tipos)));
        filtroCombo.setValue("Todos");

        cargarProductos(DataStore.getProductos());
        actualizarCarritoInfo();
        org.example.sportconnect_01.model.Usuario u = org.example.sportconnect_01.service.Session.getUsuarioActual();
        if (sidebarUserLabel != null && u != null) sidebarUserLabel.setText("Hola, " + u.getNombreCompleto());
    }

    private void actualizarCarritoInfo() {
        carritoInfoLabel.setText("🛒 " + Carrito.contarItems() + " items  |  " + Carrito.getTotalTexto());
    }

    private void cargarProductos(ArrayList<Producto> lista) {
        listaProductosBox.getChildren().clear();
        if (lista.isEmpty()) {
            Label vacio = new Label("No se encontraron productos.");
            vacio.getStyleClass().add("muted-text");
            listaProductosBox.getChildren().add(vacio);
            return;
        }

        for (Producto producto : lista) {
            VBox tarjeta = new VBox(6);
            tarjeta.getStyleClass().add("list-card");

            Label nombreLabel = new Label("🛒 " + producto.getNombre());
            nombreLabel.getStyleClass().add("item-title");

            Label infoLabel = new Label(producto.getDescripcion()
                    + "  ·  " + producto.getPrecioTexto()
                    + "  ·  Stock: " + producto.getStock()
                    + "  ·  Vendedor: " + producto.getVendedor());
            infoLabel.getStyleClass().add("normal-text");
            infoLabel.setWrapText(true);

            // Selector de cantidad
            javafx.scene.control.Spinner<Integer> cantidadSpinner = new javafx.scene.control.Spinner<>(1, Math.max(1, producto.getStock()), 1);
            cantidadSpinner.setEditable(true);
            cantidadSpinner.setPrefWidth(90);
            cantidadSpinner.setStyle("-fx-font-size: 14px;");

            Button addBtn = new Button("+ Añadir al carrito");
            addBtn.getStyleClass().add("primary-button");
            addBtn.setMaxWidth(220.0);
            addBtn.setOnAction(e -> {
                int cantidad = cantidadSpinner.getValue();
                int agregados = 0;
                for (int i = 0; i < cantidad; i++) {
                    if (!Carrito.agregar(producto)) break;
                    agregados++;
                }
                actualizarCarritoInfo();
                if (agregados == 0) {
                    carritoInfoLabel.setText("⚠ Sin stock: " + producto.getNombre() + "  |  " + Carrito.contarItems() + " items");
                } else if (agregados < cantidad) {
                    carritoInfoLabel.setText("✓ " + agregados + " de " + cantidad + " añadidos (stock limitado)  |  " + Carrito.getTotalTexto());
                }
                // sin AlertUtils para evitar popups constantes
            });

            HBox addBox = new HBox(10, cantidadSpinner, addBtn);
            addBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            tarjeta.getChildren().addAll(nombreLabel, infoLabel, addBox);
            listaProductosBox.getChildren().add(tarjeta);
        }
    }

    @FXML
    private void onBuscarClick() {
        filtrar();
    }

    @FXML
    private void onBuscarKeyReleased() {
        filtrar();
    }

    private void filtrar() {
        String texto = busquedaField.getText() == null ? "" : busquedaField.getText().trim();
        String tipoFiltro = filtroCombo.getValue();

        ArrayList<Producto> resultado = new ArrayList<>();
        for (Producto p : DataStore.buscarProductos(texto)) {
            if (tipoFiltro == null || tipoFiltro.equals("Todos") || p.getDescripcion().toLowerCase().contains(tipoFiltro.toLowerCase()))
                resultado.add(p);
        }
        cargarProductos(resultado);
    }

    @FXML
    private void onVerCarritoClick() {
        SceneManager.cambiarVista("carrito-view.fxml", "SportConnect - Carrito");
    }

    @FXML private void onVolverClick()        { SceneManager.cambiarVista("home-view.fxml",     "SportConnect - Inicio"); }
    @FXML private void onMiPerfilClick()       { SceneManager.cambiarVista("perfil-view.fxml",    "SportConnect - Perfil"); }
    @FXML private void onClasesClick()         { SceneManager.cambiarVista("clases-view.fxml",    "SportConnect - Clases"); }
    @FXML private void onMisClasesClick()      { SceneManager.cambiarVista("mis-clases-view.fxml","SportConnect - Mis clases"); }
    @FXML private void onCerrarSesionClick()   { org.example.sportconnect_01.service.Session.cerrarSesion(); SceneManager.cambiarVista("login-view.fxml", "SportConnect - Login"); }
}
