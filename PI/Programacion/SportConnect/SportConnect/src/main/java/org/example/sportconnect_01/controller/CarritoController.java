package org.example.sportconnect_01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.sportconnect_01.model.ItemCarrito;
import org.example.sportconnect_01.service.Carrito;
import org.example.sportconnect_01.service.DataStore;
import org.example.sportconnect_01.service.Session;
import org.example.sportconnect_01.util.AlertUtils;
import org.example.sportconnect_01.util.SceneManager;

public class CarritoController {

    @FXML private VBox listaCarritoBox;
    @FXML private Label totalLabel;
    @FXML private Label vacioLabel;

    @FXML
    private void initialize() { cargarCarrito(); }

    private void cargarCarrito() {
        listaCarritoBox.getChildren().clear();
        boolean vacio = Carrito.estaVacio();
        vacioLabel.setVisible(vacio);
        vacioLabel.setManaged(vacio);

        for (ItemCarrito item : Carrito.getItems()) {
            VBox tarjeta = new VBox(6);
            tarjeta.getStyleClass().add("list-card");

            Label nombre = new Label(item.getProducto().getNombre() + " x" + item.getCantidad());
            nombre.getStyleClass().add("item-title");

            Label precio = new Label(item.getProducto().getPrecioTexto() + " c/u  ·  Subtotal: " + item.getSubtotalTexto());
            precio.getStyleClass().add("normal-text");

            Button quitarBtn = new Button("Quitar");
            quitarBtn.getStyleClass().add("danger-button");
            quitarBtn.setMaxWidth(120.0);
            quitarBtn.setOnAction(e -> { Carrito.eliminar(item.getProducto()); cargarCarrito(); });

            tarjeta.getChildren().addAll(nombre, precio, quitarBtn);
            listaCarritoBox.getChildren().add(tarjeta);
        }
        totalLabel.setText(Carrito.getTotalTexto());
    }

    @FXML
    private void onComprarClick() {
        if (Carrito.estaVacio()) { AlertUtils.mostrarError("El carrito está vacío."); return; }

        var usuario = Session.getUsuarioActual();
        if (usuario == null) { AlertUtils.mostrarError("Debes iniciar sesión para comprar."); return; }

        // Verificar stock antes de intentar la compra
        for (ItemCarrito item : Carrito.getItems()) {
            if (item.getProducto().getStock() < item.getCantidad()) {
                AlertUtils.mostrarError("Stock insuficiente de «" + item.getProducto().getNombre()
                        + "». Solo quedan " + item.getProducto().getStock() + " unidades.");
                return;
            }
        }

        try {
            boolean ok = DataStore.procesarCompraCompleta(usuario.getId(), Carrito.getItems());
            if (!ok) {
                AlertUtils.mostrarError("No se pudo realizar la compra. Revisa el stock disponible.");
                return;
            }
            Carrito.vaciar();
            AlertUtils.mostrarInfo("¡Compra realizada!",
                    DataStore.isUsandoBaseDeDatos()
                    ? "Compra guardada en MySQL. El stock se ha actualizado."
                    : "Compra registrada en modo local.");
            SceneManager.cambiarVista("productos-view.fxml", "SportConnect - Tienda");
        } catch (RuntimeException e) {
            AlertUtils.mostrarError("Error al procesar la compra: " + e.getMessage());
        }
    }

    @FXML private void onVaciarClick()  { Carrito.vaciar(); cargarCarrito(); }
    @FXML private void onVolverClick()  { SceneManager.cambiarVista("productos-view.fxml", "SportConnect - Tienda"); }
}
