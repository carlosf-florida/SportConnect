package org.example.sportconnect_01.model;

public class Suscripcion {

    private int id;
    private int usuarioId;
    private int claseId;
    private String fechaSuscripcion;

    public Suscripcion(int id, int usuarioId, int claseId, String fechaSuscripcion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.claseId = claseId;
        this.fechaSuscripcion = fechaSuscripcion;
    }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public int getClaseId() { return claseId; }
    public String getFechaSuscripcion() { return fechaSuscripcion; }
}
