package org.example.sportconnect_01.service;

import org.example.sportconnect_01.model.ItemCarrito;
import org.example.sportconnect_01.model.Producto;

import java.util.ArrayList;

public class Carrito {
    private static final ArrayList<ItemCarrito> items = new ArrayList<>();

    public static boolean agregar(Producto producto) {
        if (producto == null || producto.getStock() <= 0) {
            return false;
        }

        for (ItemCarrito item : items) {
            if (item.getProducto().getId() == producto.getId()) {
                if (item.getCantidad() >= producto.getStock()) {
                    return false;
                }
                item.setCantidad(item.getCantidad() + 1);
                return true;
            }
        }

        items.add(new ItemCarrito(producto, 1));
        return true;
    }

    public static void eliminar(Producto producto) {
        items.removeIf(i -> i.getProducto().getId() == producto.getId());
    }

    public static void vaciar() { items.clear(); }

    public static ArrayList<ItemCarrito> getItems() { return items; }

    public static double getTotal() {
        double total = 0;
        for (ItemCarrito item : items) total += item.getSubtotal();
        return total;
    }

    public static String getTotalTexto() { return String.format("%.2f €", getTotal()); }

    public static int contarItems() {
        int total = 0;
        for (ItemCarrito item : items) total += item.getCantidad();
        return total;
    }

    public static boolean estaVacio() { return items.isEmpty(); }
}
