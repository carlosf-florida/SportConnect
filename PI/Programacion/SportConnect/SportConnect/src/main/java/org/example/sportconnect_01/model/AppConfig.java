package org.example.sportconnect_01.model;

public class AppConfig {

    private String nombreAplicacion;
    private String mensajeInicio;
    private String textoFooter;

    public AppConfig(String nombreAplicacion, String mensajeInicio, String textoFooter) {
        this.nombreAplicacion = nombreAplicacion;
        this.mensajeInicio = mensajeInicio;
        this.textoFooter = textoFooter;
    }

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public void setNombreAplicacion(String nombreAplicacion) {
        this.nombreAplicacion = nombreAplicacion;
    }

    public String getMensajeInicio() {
        return mensajeInicio;
    }

    public void setMensajeInicio(String mensajeInicio) {
        this.mensajeInicio = mensajeInicio;
    }

    public String getTextoFooter() {
        return textoFooter;
    }

    public void setTextoFooter(String textoFooter) {
        this.textoFooter = textoFooter;
    }
}
