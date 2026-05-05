package org.example.sportconnect_01.service;

import org.example.sportconnect_01.model.AppConfig;
import org.example.sportconnect_01.model.ClaseDeportiva;
import org.example.sportconnect_01.model.Producto;
import org.example.sportconnect_01.model.Profesor;
import org.example.sportconnect_01.model.Suscripcion;
import org.example.sportconnect_01.model.Usuario;

import java.util.ArrayList;
import java.util.HashMap;

public class DataStore {

    private static final ArrayList<Usuario> usuarios = new ArrayList<>();
    private static final ArrayList<ClaseDeportiva> clases = new ArrayList<>();
    private static final ArrayList<Profesor> profesores = new ArrayList<>();
    private static final ArrayList<Producto> productos = new ArrayList<>();
    private static final ArrayList<Suscripcion> suscripciones = new ArrayList<>();

    private static int siguienteUsuarioId = 17;
    private static int siguienteClaseId = 9;
    private static int siguienteProfesorId = 100;
    private static int siguienteProductoId = 6;
    private static int siguienteSuscripcionId = 21;

    private static final AppConfig appConfig = new AppConfig(
            "SportConnect",
            "Gestiona clases, profesores, productos y reservas desde una sola aplicación.",
            "Proyecto JavaFX 1º DAM · Preparado para la base de datos SportConnect"
    );

    static {
        cargarDatosDeEjemploBasadosEnSQL();
    }

    private static void cargarDatosDeEjemploBasadosEnSQL() {
        usuarios.add(new Usuario(1, "Carlos", "Larrea", "aniri", "aniri@mail.com", "123456", 1, "Admin", "2026-04-29"));
        usuarios.add(new Usuario(2, "Ana", "Lopez", "analopez", "ana@mail.com", "123456", 2, "Profesor", "2026-04-29"));
        usuarios.add(new Usuario(3, "Juan", "Perez", "juanp", "juan@mail.com", "123456", 2, "Profesor", "2026-04-29"));
        usuarios.add(new Usuario(4, "Maria", "Gomez", "mariag", "maria@mail.com", "123456", 2, "Profesor", "2026-04-29"));
        usuarios.add(new Usuario(5, "Carlos", "Ruiz", "carlosr", "carlos@mail.com", "123456", 3, "Alumno", "2026-04-29"));
        usuarios.add(new Usuario(6, "Laura", "Sanchez", "lauras", "laura@mail.com", "123456", 3, "Alumno", "2026-04-29"));
        usuarios.add(new Usuario(7, "Pedro", "Diaz", "pedrod", "pedro@mail.com", "123456", 3, "Alumno", "2026-04-29"));
        usuarios.add(new Usuario(8, "Lucia", "Martinez", "luciam", "lucia@mail.com", "123456", 2, "Profesor", "2026-04-29"));
        usuarios.add(new Usuario(9, "David", "Hernandez", "davidh", "david@mail.com", "123456", 3, "Alumno", "2026-04-29"));
        usuarios.add(new Usuario(10, "Sofia", "Garcia", "sofiag", "sofia@mail.com", "123456", 3, "Alumno", "2026-04-29"));
        usuarios.add(new Usuario(11, "Miguel", "Torres", "miguelt", "miguel@mail.com", "123456", 3, "Alumno", "2026-04-29"));
        usuarios.add(new Usuario(12, "Elena", "Vega", "elenav", "elena@mail.com", "123456", 2, "Profesor", "2026-04-29"));
        usuarios.add(new Usuario(13, "Alberto", "Navarro", "alberton", "alberto@mail.com", "123456", 3, "Alumno", "2026-04-29"));
        usuarios.add(new Usuario(14, "Raquel", "Moreno", "raquelm", "raquel@mail.com", "123456", 3, "Alumno", "2026-04-29"));
        usuarios.add(new Usuario(15, "Jorge", "Castro", "jorgec", "jorge@mail.com", "123456", 3, "Alumno", "2026-04-29"));
        usuarios.add(new Usuario(16, "Sara", "Ortega", "sarao", "sara@mail.com", "123456", 3, "Alumno", "2026-04-29"));

        profesores.add(new Profesor(2, "Ana", "Lopez", "analopez", "ana@mail.com", "Yoga y running", "Profesora de clases suaves y resistencia"));
        profesores.add(new Profesor(3, "Juan", "Perez", "juanp", "juan@mail.com", "Fútbol", "Entrenador técnico de fútbol"));
        profesores.add(new Profesor(4, "Maria", "Gomez", "mariag", "maria@mail.com", "Crossfit y boxeo", "Entrenamientos intensivos"));
        profesores.add(new Profesor(8, "Lucia", "Martinez", "luciam", "lucia@mail.com", "Fútbol y ciclismo", "Clases grupales deportivas"));
        profesores.add(new Profesor(12, "Elena", "Vega", "elenav", "elena@mail.com", "Pilates y fitness", "Control corporal y fuerza"));

        clases.add(new ClaseDeportiva(1, "Yoga para principiantes", "Clase básica de yoga", "Yoga", 2, "Ana Lopez", false, 0.00, 20, "Lunes 18:00", "Principiante", "2026-04-29"));
        clases.add(new ClaseDeportiva(2, "Crossfit intensivo", "Entrenamiento avanzado", "Crossfit", 4, "Maria Gomez", true, 15.00, 12, "Martes 19:00", "Avanzado", "2026-04-29"));
        clases.add(new ClaseDeportiva(3, "Futbol técnico", "Mejora tu técnica", "Futbol", 8, "Lucia Martinez", false, 0.00, 18, "Miércoles 17:30", "Intermedio", "2026-04-29"));
        clases.add(new ClaseDeportiva(4, "Pilates avanzado", "Control corporal", "Pilates", 12, "Elena Vega", true, 12.00, 10, "Jueves 18:30", "Avanzado", "2026-04-29"));
        clases.add(new ClaseDeportiva(5, "Running urbano", "Entrenamiento de resistencia", "Running", 2, "Ana Lopez", false, 0.00, 16, "Sábado 10:00", "Básico", "2026-04-29"));
        clases.add(new ClaseDeportiva(6, "Boxeo básico", "Defensa personal", "Boxeo", 4, "Maria Gomez", true, 20.00, 8, "Viernes 19:30", "Básico", "2026-04-29"));
        clases.add(new ClaseDeportiva(7, "Ciclismo indoor", "Cardio intenso", "Ciclismo", 8, "Lucia Martinez", false, 0.00, 14, "Lunes 20:00", "Todos los niveles", "2026-04-29"));
        clases.add(new ClaseDeportiva(8, "Fitness full body", "Entrenamiento completo", "Fitness", 12, "Elena Vega", true, 18.00, 11, "Miércoles 20:00", "Intermedio", "2026-04-29"));

        productos.add(new Producto(1, "Proteina Whey", "Suplemento muscular", 29.99, 50, 2, "Ana Lopez", "2026-04-29"));
        productos.add(new Producto(2, "Botella deportiva", "Botella 1L", 9.99, 100, 4, "Maria Gomez", "2026-04-29"));
        productos.add(new Producto(3, "Cuerda crossfit", "Alta resistencia", 14.99, 30, 8, "Lucia Martinez", "2026-04-29"));
        productos.add(new Producto(4, "Guantes boxeo", "Profesionales", 24.99, 20, 12, "Elena Vega", "2026-04-29"));
        productos.add(new Producto(5, "Esterilla yoga", "Antideslizante", 19.99, 40, 2, "Ana Lopez", "2026-04-29"));

        int[][] datosSuscripciones = {
                {1, 1, 1}, {2, 1, 2}, {3, 3, 1}, {4, 3, 4}, {5, 5, 2},
                {6, 6, 3}, {7, 7, 1}, {8, 7, 5}, {9, 9, 6}, {10, 10, 2},
                {11, 11, 4}, {12, 13, 7}, {13, 14, 8}, {14, 15, 1}, {15, 2, 3},
                {16, 4, 5}, {17, 8, 6}, {18, 12, 2}, {19, 9, 8}, {20, 5, 4}
        };
        for (int[] dato : datosSuscripciones) {
            suscripciones.add(new Suscripcion(dato[0], dato[1], dato[2], "2026-04-29"));
            ClaseDeportiva clase = buscarClasePorId(dato[2]);
            if (clase != null) {
                clase.reservarPlaza();
            }
        }
    }

    public static Usuario iniciarSesion(String correo, String password) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo) && usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    public static boolean correoExiste(String correo) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo)) return true;
        }
        return false;
    }

    public static boolean nickExiste(String nick) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNick().equalsIgnoreCase(nick)) return true;
        }
        return false;
    }

    public static void registrarUsuario(Usuario usuario) {
        usuario.setId(siguienteUsuarioId++);
        usuario.setFechaCreacion("Nuevo registro");
        usuarios.add(usuario);
        if (usuario.esProfesor()) {
            profesores.add(new Profesor(usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getNick(), usuario.getEmail(), "Sin especialidad", "Nuevo profesor"));
        }
    }

    public static ArrayList<Usuario> getUsuarios() { return usuarios; }
    public static ArrayList<ClaseDeportiva> getClases() { return clases; }
    public static ArrayList<Profesor> getProfesores() { return profesores; }
    public static ArrayList<Producto> getProductos() { return productos; }
    public static AppConfig getAppConfig() { return appConfig; }

    public static void agregarClase(ClaseDeportiva clase) {
        clase.setId(siguienteClaseId++);
        clases.add(clase);
    }

    public static void actualizarClase(int posicion, ClaseDeportiva claseActualizada) {
        if (posicion >= 0 && posicion < clases.size()) {
            int idAnterior = clases.get(posicion).getId();
            claseActualizada.setId(idAnterior);
            clases.set(posicion, claseActualizada);
        }
    }

    public static void eliminarClase(int posicion) {
        if (posicion >= 0 && posicion < clases.size()) {
            int claseId = clases.get(posicion).getId();
            clases.remove(posicion);
            suscripciones.removeIf(s -> s.getClaseId() == claseId);
        }
    }

    public static void agregarProfesor(Profesor profesor) {
        profesor.setId(siguienteProfesorId++);
        profesores.add(profesor);
    }

    public static void actualizarProfesor(int posicion, Profesor profesorActualizado) {
        if (posicion >= 0 && posicion < profesores.size()) {
            profesorActualizado.setId(profesores.get(posicion).getId());
            profesores.set(posicion, profesorActualizado);
        }
    }

    public static void eliminarProfesor(int posicion) {
        if (posicion >= 0 && posicion < profesores.size()) profesores.remove(posicion);
    }

    public static void agregarProducto(Producto producto) {
        producto.setId(siguienteProductoId++);
        productos.add(producto);
    }

    public static void actualizarProducto(int posicion, Producto productoActualizado) {
        if (posicion >= 0 && posicion < productos.size()) {
            productoActualizado.setId(productos.get(posicion).getId());
            productos.set(posicion, productoActualizado);
        }
    }

    public static void eliminarProducto(int posicion) {
        if (posicion >= 0 && posicion < productos.size()) productos.remove(posicion);
    }

    public static void actualizarUsuario(int posicion, Usuario usuarioActualizado) {
        if (posicion >= 0 && posicion < usuarios.size()) {
            usuarioActualizado.setId(usuarios.get(posicion).getId());
            usuarios.set(posicion, usuarioActualizado);
        }
    }

    public static void eliminarUsuario(int posicion) {
        if (posicion >= 0 && posicion < usuarios.size()) {
            int usuarioId = usuarios.get(posicion).getId();
            usuarios.remove(posicion);
            suscripciones.removeIf(s -> s.getUsuarioId() == usuarioId);
        }
    }

    public static boolean apuntarUsuarioAClase(Usuario usuario, ClaseDeportiva clase) {
        if (usuario == null || clase == null || estaApuntado(usuario, clase) || !clase.hayPlazas()) return false;
        suscripciones.add(new Suscripcion(siguienteSuscripcionId++, usuario.getId(), clase.getId(), "Hoy"));
        clase.reservarPlaza();
        return true;
    }

    public static boolean cancelarInscripcion(Usuario usuario, ClaseDeportiva clase) {
        if (usuario == null || clase == null) return false;
        boolean eliminado = suscripciones.removeIf(s -> s.getUsuarioId() == usuario.getId() && s.getClaseId() == clase.getId());
        if (eliminado) clase.liberarPlaza();
        return eliminado;
    }

    public static boolean estaApuntado(Usuario usuario, ClaseDeportiva clase) {
        if (usuario == null || clase == null) return false;
        for (Suscripcion suscripcion : suscripciones) {
            if (suscripcion.getUsuarioId() == usuario.getId() && suscripcion.getClaseId() == clase.getId()) return true;
        }
        return false;
    }

    public static ArrayList<ClaseDeportiva> getClasesApuntadas(Usuario usuario) {
        ArrayList<ClaseDeportiva> resultado = new ArrayList<>();
        if (usuario == null) return resultado;
        for (Suscripcion suscripcion : suscripciones) {
            if (suscripcion.getUsuarioId() == usuario.getId()) {
                ClaseDeportiva clase = buscarClasePorId(suscripcion.getClaseId());
                if (clase != null) resultado.add(clase);
            }
        }
        return resultado;
    }

    public static ArrayList<ClaseDeportiva> getClasesDelProfesor(Usuario profesor) {
        ArrayList<ClaseDeportiva> resultado = new ArrayList<>();
        if (profesor == null) return resultado;
        for (ClaseDeportiva clase : clases) {
            if (clase.getProfesorId() == profesor.getId()) resultado.add(clase);
        }
        return resultado;
    }

    public static int getIndiceClase(ClaseDeportiva claseBuscada) {
        for (int i = 0; i < clases.size(); i++) {
            if (clases.get(i).getId() == claseBuscada.getId()) return i;
        }
        return -1;
    }

    public static ClaseDeportiva buscarClasePorId(int id) {
        for (ClaseDeportiva clase : clases) {
            if (clase.getId() == id) return clase;
        }
        return null;
    }

    public static Usuario buscarUsuarioPorId(int id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) return usuario;
        }
        return null;
    }

    public static String getProfesorNombreById(int id) {
        Usuario usuario = buscarUsuarioPorId(id);
        return usuario == null ? "Profesor no encontrado" : usuario.getNombreCompleto();
    }

    public static int contarReservasTotales() { return suscripciones.size(); }
    public static int contarAlumnos() { return contarPorRol("Alumno"); }
    public static int contarProfesoresUsuarios() { return contarPorRol("Profesor"); }

    private static int contarPorRol(String rol) {
        int contador = 0;
        for (Usuario usuario : usuarios) {
            if (usuario.getTipo().equalsIgnoreCase(rol)) contador++;
        }
        return contador;
    }

    public static HashMap<Integer, Integer> contarInscripcionesPorClase() {
        HashMap<Integer, Integer> resultado = new HashMap<>();
        for (Suscripcion s : suscripciones) {
            resultado.put(s.getClaseId(), resultado.getOrDefault(s.getClaseId(), 0) + 1);
        }
        return resultado;
    }
}
