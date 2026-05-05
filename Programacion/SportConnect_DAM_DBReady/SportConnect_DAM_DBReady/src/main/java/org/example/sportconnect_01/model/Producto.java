package org.example.sportconnect_01.model;

public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private int vendedorId;
    private String vendedor;
    private String fechaPublicacion;

    public Producto(int id, String nombre, String descripcion, double precio, int stock, int vendedorId, String vendedor, String fechaPublicacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.vendedorId = vendedorId;
        this.vendedor = vendedor;
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getVendedorId() { return vendedorId; }
    public void setVendedorId(int vendedorId) { this.vendedorId = vendedorId; }
    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }
    public String getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(String fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public String getPrecioTexto() { return String.format("%.2f €", precio); }
}
