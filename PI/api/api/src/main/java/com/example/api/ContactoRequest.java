package com.example.api;
public class ContactoRequest {
    private String nombre;
    private String email;
    private String tipo;
    private String mensaje;

    // Getters y Setters
    public String getNombre()  { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail()   { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipo()    { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}