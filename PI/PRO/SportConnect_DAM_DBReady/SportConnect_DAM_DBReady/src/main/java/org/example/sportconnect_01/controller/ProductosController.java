package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.sportconnect_01.model.Producto;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.util.SceneManager;

public class ProductosController {

    @FXML private VBox listaProductosBox;

    @FXML
    private void initialize() {
        for (Producto producto : DataStore.getProductos()) {
            VBox tarjeta = new VBox(6);
            tarjeta.getStyleClass().add("list-card");

            Label nombreLabel = new Label(producto.getNombre());
            nombreLabel.getStyleClass().add("item-title");

            Label infoLabel = new Label("Descripción: " + producto.getDescripcion()
                    + "\nPrecio: " + producto.getPrecioTexto()
                    + "\nStock: " + producto.getStock()
                    + "\nVendedor: " + producto.getVendedor() + " (ID: " + producto.getVendedorId() + ")");
            infoLabel.getStyleClass().add("normal-text");
            infoLabel.setWrapText(true);

            tarjeta.getChildren().addAll(nombreLabel, infoLabel);
            listaProductosBox.getChildren().add(tarjeta);
        }
    }

    @FXML private void onVolverClick() { SceneManager.cambiarVista("home-view.fxml", "SportConnect - Inicio"); }
}
