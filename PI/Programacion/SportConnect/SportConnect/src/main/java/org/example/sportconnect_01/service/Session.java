package org.example.sportconnect_01.service;

import org.example.sportconnect_01.model.Usuario;

public class Session {

    private static Usuario usuarioActual;

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuarioActual) {
        Session.usuarioActual = usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
