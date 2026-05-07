package org.example.sportconnect_01.model;

import java.time.LocalDate;

public class CompraHistorial {
    private int id;
    private int usuarioId;
    private int productoId;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private String fecha;

    public CompraHistorial(int id, int usuarioId, int productoId, String nombreProducto, int cantidad, double precioUnitario, String fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public int getProductoId() { return productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public String getFecha() { return fecha; }
    public double getTotal() { return precioUnitario * cantidad; }
    public String getTotalTexto() { return String.format("%.2f €", getTotal()); }
    public String getPrecioUnitarioTexto() { return String.format("%.2f €", precioUnitario); }
}
