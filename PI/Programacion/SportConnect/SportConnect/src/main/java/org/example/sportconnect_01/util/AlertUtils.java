package org.example.sportconnect_01.util;

import javafx.scene.control.Alert;

public class AlertUtils {

    /**
     * Muestra un diálogo de información. Usar solo para confirmaciones importantes,
     * no para feedback de acciones rutinarias (añadir al carrito, etc.).
     */
    public static void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de error. Solo para errores reales que el usuario debe conocer.
     */
    public static void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra información sin bloquear (no showAndWait). Para notificaciones secundarias.
     */
    public static void mostrarInfoSilenciosa(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show(); // no bloquea
    }
}
