package org.example.sportconnect_01.model;

public class Profesor {

    private int id;
    private String nombre;
    private String apellido;
    private String nick;
    private String email;
    private String especialidad;
    private String experiencia;

    public Profesor(String nombre, String especialidad, String experiencia) {
        this(0, nombre, "", nombre.toLowerCase().replace(" ", ""), "", especialidad, experiencia);
    }

    public Profesor(int id, String nombre, String apellido, String nick, String email, String especialidad, String experiencia) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nick = nick;
        this.email = email;
        this.especialidad = especialidad;
        this.experiencia = experiencia;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }

    public String getNombreCompleto() {
        if (apellido == null || apellido.isBlank()) return nombre;
        return nombre + " " + apellido;
    }
}
