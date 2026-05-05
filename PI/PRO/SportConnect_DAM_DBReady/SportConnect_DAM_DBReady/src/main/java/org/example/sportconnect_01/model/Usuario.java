package org.example.sportconnect_01.model;

public class Usuario {

    private int id;
    private String nombre;
    private String apellido;
    private String nick;
    private String email;
    private String password;
    private int rolId;
    private String rol;
    private String fechaCreacion;

    public Usuario(String nombre, String correo, String password, String tipo) {
        this(0, nombre, "", nombre.toLowerCase().replace(" ", ""), correo, password, convertirRolId(tipo), tipo, "");
    }

    public Usuario(int id, String nombre, String apellido, String nick, String email, String password, int rolId, String rol, String fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nick = nick;
        this.email = email;
        this.password = password;
        this.rolId = rolId;
        this.rol = normalizarRol(rol);
        this.fechaCreacion = fechaCreacion;
    }

    private static int convertirRolId(String rol) {
        if (rol == null) return 3;
        if (rol.equalsIgnoreCase("Admin") || rol.equalsIgnoreCase("Administrador")) return 1;
        if (rol.equalsIgnoreCase("Profesor")) return 2;
        return 3;
    }

    private static String normalizarRol(String rol) {
        if (rol == null || rol.isBlank()) return "Alumno";
        if (rol.equalsIgnoreCase("administrador") || rol.equalsIgnoreCase("admin")) return "Admin";
        if (rol.equalsIgnoreCase("profesor")) return "Profesor";
        return "Alumno";
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
    public String getCorreo() { return email; }
    public void setCorreo(String correo) { this.email = correo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getRolId() { return rolId; }
    public void setRolId(int rolId) { this.rolId = rolId; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = normalizarRol(rol); this.rolId = convertirRolId(this.rol); }
    public String getTipo() { return rol; }
    public void setTipo(String tipo) { setRol(tipo); }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getNombreCompleto() {
        if (apellido == null || apellido.isBlank()) return nombre;
        return nombre + " " + apellido;
    }

    public boolean esAdmin() { return rol.equalsIgnoreCase("Admin"); }
    public boolean esProfesor() { return rol.equalsIgnoreCase("Profesor"); }
    public boolean esAlumno() { return rol.equalsIgnoreCase("Alumno"); }
}
