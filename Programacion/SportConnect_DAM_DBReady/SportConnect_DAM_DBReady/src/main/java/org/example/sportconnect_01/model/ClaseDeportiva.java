package org.example.sportconnect_01.model;

public class ClaseDeportiva {

    private int id;
    private String titulo;
    private String descripcion;
    private String deporte;
    private int profesorId;
    private String profesor;
    private boolean premium;
    private double precio;
    private int plazas;
    private String horario;
    private String nivel;
    private String fechaCreacion;

    public ClaseDeportiva(String nombre, String profesor, String horario, String nivel, int plazas) {
        this(0, nombre, "Clase deportiva", nombre, 0, profesor, false, 0.0, plazas, horario, nivel, "");
    }

    public ClaseDeportiva(int id, String titulo, String descripcion, String deporte, int profesorId, String profesor,
                          boolean premium, double precio, int plazas, String horario, String nivel, String fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.deporte = deporte;
        this.profesorId = profesorId;
        this.profesor = profesor;
        this.premium = premium;
        this.precio = precio;
        this.plazas = plazas;
        this.horario = horario;
        this.nivel = nivel;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getNombre() { return titulo; }
    public void setNombre(String nombre) { this.titulo = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getDeporte() { return deporte; }
    public void setDeporte(String deporte) { this.deporte = deporte; }
    public int getProfesorId() { return profesorId; }
    public void setProfesorId(int profesorId) { this.profesorId = profesorId; }
    public String getProfesor() { return profesor; }
    public void setProfesor(String profesor) { this.profesor = profesor; }
    public boolean isPremium() { return premium; }
    public boolean getEsPremium() { return premium; }
    public void setPremium(boolean premium) { this.premium = premium; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getPlazas() { return plazas; }
    public void setPlazas(int plazas) { this.plazas = plazas; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public boolean hayPlazas() { return plazas > 0; }
    public void reservarPlaza() { if (hayPlazas()) plazas--; }
    public void liberarPlaza() { plazas++; }
    public String getPrecioTexto() { return (!premium || precio <= 0) ? "Gratis" : String.format("%.2f €", precio); }
    public String getTipoClaseTexto() { return premium ? "Premium" : "Gratis"; }
}
