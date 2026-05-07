package org.example.sportconnect_01.model;

public class Valoracion {
    private int id;
    private int usuarioId;
    private int claseId;
    private int estrellas; // 1-5
    private String comentario;
    private String fecha;

    public Valoracion(int id, int usuarioId, int claseId, int estrellas, String comentario, String fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.claseId = claseId;
        this.estrellas = Math.max(1, Math.min(5, estrellas));
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public int getClaseId() { return claseId; }
    public int getEstrellas() { return estrellas; }
    public String getComentario() { return comentario; }
    public String getFecha() { return fecha; }
    public String getEstrellasTexto() { return "★".repeat(estrellas) + "☆".repeat(5 - estrellas); }
}
