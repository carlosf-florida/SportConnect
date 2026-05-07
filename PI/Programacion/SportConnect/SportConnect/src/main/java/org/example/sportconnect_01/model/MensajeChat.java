package org.example.sportconnect_01.model;

public class MensajeChat {
    private int id;
    private int remitenteId;
    private int claseId;
    private String remitenteNombre;
    private String claseTitulo;
    private String contenido;
    private String fechaEnvio;

    public MensajeChat(int id, int remitenteId, int claseId, String remitenteNombre, String claseTitulo, String contenido, String fechaEnvio) {
        this.id = id;
        this.remitenteId = remitenteId;
        this.claseId = claseId;
        this.remitenteNombre = remitenteNombre;
        this.claseTitulo = claseTitulo;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getRemitenteId() { return remitenteId; }
    public void setRemitenteId(int remitenteId) { this.remitenteId = remitenteId; }
    public int getClaseId() { return claseId; }
    public void setClaseId(int claseId) { this.claseId = claseId; }
    public String getRemitenteNombre() { return remitenteNombre; }
    public void setRemitenteNombre(String remitenteNombre) { this.remitenteNombre = remitenteNombre; }
    public String getClaseTitulo() { return claseTitulo; }
    public void setClaseTitulo(String claseTitulo) { this.claseTitulo = claseTitulo; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public String getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(String fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getVistaCorta() {
        String autor = remitenteNombre == null || remitenteNombre.isBlank() ? "Usuario" : remitenteNombre;
        String clase = claseTitulo == null || claseTitulo.isBlank() ? "Clase" : claseTitulo;
        String texto = contenido == null ? "" : contenido;
        return autor + " · " + clase + "\n" + texto;
    }
}
