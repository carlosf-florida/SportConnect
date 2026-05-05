package com.example.api.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String apellido;
    private String nick;
    private String email;
    private String password;
    @Column(name = "rol_id")
    private int rolId;

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getNick() { return nick; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getRolId() { return rolId; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setNick(String nick) { this.nick = nick; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRolId(int rolId) { this.rolId = rolId; }
}
