package com.example.api.Model;
import jakarta.persistence.*;

@Entity
@Table(name = "clases")
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titulo;
    private String descripcion;
    private String deporte;
    @Column(name = "profesor_id")
    private int profesorId;
    @Column(name = "es_premium")
    private int esPremium;
    private double precio;

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getDeporte() { return deporte; }
    public int getProfesorId() { return profesorId; }
    public int getEsPremium() { return esPremium; }
    public double getPrecio() { return precio; }

    // Setters
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setDeporte(String deporte) { this.deporte = deporte; }
    public void setProfesorId(int profesorId) { this.profesorId = profesorId; }
    public void setEsPremium(int esPremium) { this.esPremium = esPremium; }
    public void setPrecio(double precio) { this.precio = precio; }
}