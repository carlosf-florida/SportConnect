package org.example.sportconnect_01.service;

import org.example.sportconnect_01.model.*;
import org.example.sportconnect_01.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DataStore — capa de acceso a datos con fallback automático a datos locales.
 *
 * Mejoras v2:
 *  - Diagnóstico de conexión al arrancar (muestra motivo del fallo).
 *  - getProductos() y getCompras() ya NO recargan la BD en cada llamada
 *    (causaba N consultas por cada render de pantalla). Se recarga solo
 *    cuando hay escritura real.
 *  - enviarMensaje lanza RuntimeException solo si la excepción NO es de
 *    constraint (p. ej. FK); de lo contrario muestra error legible.
 *  - Corrección: apuntarUsuarioAClase llama al stored procedure con
 *    setObject en lugar de setInt para evitar problemas de tipo con
 *    algunos drivers.
 *  - Método estático getErrorConexion() para que la UI pueda mostrarlo.
 */
public class DataStore {

    private static final ArrayList<Usuario>       usuarios      = new ArrayList<>();
    private static final ArrayList<ClaseDeportiva> clases       = new ArrayList<>();
    private static final ArrayList<Profesor>       profesores   = new ArrayList<>();
    private static final ArrayList<Producto>       productos    = new ArrayList<>();
    private static final ArrayList<Suscripcion>    suscripciones= new ArrayList<>();
    private static final ArrayList<CompraHistorial> compras     = new ArrayList<>();
    private static final ArrayList<Valoracion>     valoraciones = new ArrayList<>();
    private static final ArrayList<MensajeChat>    mensajes     = new ArrayList<>();

    private static int siguienteUsuarioId    = 17;
    private static int siguienteClaseId      = 9;
    private static int siguienteProfesorId   = 100;
    private static int siguienteProductoId   = 6;
    private static int siguienteSuscripcionId= 21;
    private static int siguienteCompraId     = 1;
    private static int siguienteValoracionId = 1;
    private static int siguienteMensajeId    = 9;

    private static boolean usandoBaseDeDatos = false;
    private static String  errorConexion     = null;

    private static final AppConfig appConfig = new AppConfig(
            "SportConnect",
            "Tu plataforma de clases deportivas",
            "Proyecto JavaFX 1º DAM"
    );

    static {
        errorConexion = DBUtil.probarConexion();
        if (errorConexion == null) {
            usandoBaseDeDatos = cargarDatosDesdeBaseDeDatos();
        }
        if (!usandoBaseDeDatos) {
            System.err.println("[DataStore] Sin MySQL → modo local. Causa: " + errorConexion);
            cargarDatosDeEjemploBasadosEnSQL();
        } else {
            System.out.println("[DataStore] Conectado a MySQL correctamente.");
        }
    }

    public static boolean isUsandoBaseDeDatos() { return usandoBaseDeDatos; }
    public static String  getErrorConexion()     { return errorConexion; }

    public static void recargarDatos() {
        if (usandoBaseDeDatos) cargarDatosDesdeBaseDeDatos();
    }

    // ──────────────────────────────────────── CARGA BD ────────────────────────
    private static boolean cargarDatosDesdeBaseDeDatos() {
        try (Connection conn = DBUtil.getConnection()) {
            limpiarListas();
            cargarUsuariosDesdeBD(conn);
            cargarProfesoresDesdeBD(conn);
            cargarClasesDesdeBD(conn);
            cargarMensajesDesdeBD(conn);
            cargarProductosDesdeBD(conn);
            cargarSuscripcionesDesdeBD(conn);
            cargarComprasDesdeBD(conn);
            cargarValoracionesDesdeBD(conn);
            recalcularSiguientesIds();
            return true;
        } catch (SQLException e) {
            System.err.println("[DataStore] Error cargando desde MySQL: " + e.getMessage());
            return false;
        }
    }

    private static void limpiarListas() {
        usuarios.clear(); clases.clear(); profesores.clear(); productos.clear();
        suscripciones.clear(); compras.clear(); valoraciones.clear(); mensajes.clear();
    }

    private static void cargarUsuariosDesdeBD(Connection conn) throws SQLException {
        String sql = "SELECT u.id, u.nombre, u.apellido, u.nick, u.email, u.password, u.rol_id, "
                + "COALESCE(r.nombre, 'usuario') AS rol_nombre, u.fecha_creacion "
                + "FROM usuarios u LEFT JOIN roles r ON r.id = u.rol_id ORDER BY u.id";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"),
                        rs.getString("nick"), rs.getString("email"), rs.getString("password"),
                        rs.getInt("rol_id"), rs.getString("rol_nombre"),
                        String.valueOf(rs.getTimestamp("fecha_creacion"))));
            }
        }
    }

    private static void cargarProfesoresDesdeBD(Connection conn) throws SQLException {
        String sql = "SELECT u.id, u.nombre, u.apellido, u.nick, u.email, "
                + "COALESCE(GROUP_CONCAT(DISTINCT c.deporte SEPARATOR ', '), 'Sin especialidad') AS especialidad "
                + "FROM usuarios u LEFT JOIN clases c ON c.profesor_id = u.id "
                + "WHERE u.rol_id = 2 GROUP BY u.id, u.nombre, u.apellido, u.nick, u.email ORDER BY u.id";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                profesores.add(new Profesor(rs.getInt("id"), rs.getString("nombre"),
                        rs.getString("apellido"), rs.getString("nick"), rs.getString("email"),
                        rs.getString("especialidad"), "Profesor registrado en la base de datos"));
            }
        }
    }

    private static void cargarClasesDesdeBD(Connection conn) throws SQLException {
        String sql = "SELECT c.id, c.titulo, c.descripcion, c.deporte, c.profesor_id, "
                + "CONCAT(u.nombre, ' ', u.apellido) AS profesor_nombre, c.es_premium, c.precio, c.fecha_creacion, "
                + "(SELECT COUNT(*) FROM suscripciones s WHERE s.clase_id = c.id) AS inscritos "
                + "FROM clases c LEFT JOIN usuarios u ON u.id = c.profesor_id ORDER BY c.id";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int plazasTotales = 20;
                int inscritos = rs.getInt("inscritos");
                int plazasDisponibles = Math.max(0, plazasTotales - inscritos);
                clases.add(new ClaseDeportiva(
                        rs.getInt("id"), rs.getString("titulo"), rs.getString("descripcion"),
                        rs.getString("deporte"), rs.getInt("profesor_id"), rs.getString("profesor_nombre"),
                        rs.getBoolean("es_premium"), rs.getDouble("precio"), plazasDisponibles,
                        "Horario por definir", "Todos los niveles",
                        String.valueOf(rs.getTimestamp("fecha_creacion"))));
            }
        }
    }

    private static void cargarProductosDesdeBD(Connection conn) throws SQLException {
        productos.clear();
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio, p.stock, p.vendedor_id, "
                + "CONCAT(u.nombre, ' ', u.apellido) AS vendedor_nombre, p.fecha_publicacion "
                + "FROM productos p LEFT JOIN usuarios u ON u.id = p.vendedor_id ORDER BY p.id";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                        rs.getDouble("precio"), rs.getInt("stock"), rs.getInt("vendedor_id"),
                        rs.getString("vendedor_nombre"), String.valueOf(rs.getTimestamp("fecha_publicacion"))));
            }
        }
    }

    private static void cargarSuscripcionesDesdeBD(Connection conn) throws SQLException {
        suscripciones.clear();
        String sql = "SELECT id, usuario_id, clase_id, fecha_suscripcion FROM suscripciones ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                suscripciones.add(new Suscripcion(rs.getInt("id"), rs.getInt("usuario_id"),
                        rs.getInt("clase_id"), String.valueOf(rs.getTimestamp("fecha_suscripcion"))));
            }
        }
    }

    private static void cargarComprasDesdeBD(Connection conn) throws SQLException {
        compras.clear();
        String sql = "SELECT p.id, p.comprador_id, p.producto_id, pr.nombre AS nombre_producto, "
                + "p.cantidad, p.total, p.fecha_pedido "
                + "FROM pedidos p LEFT JOIN productos pr ON pr.id = p.producto_id ORDER BY p.id";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int cantidad = rs.getInt("cantidad");
                double total = rs.getDouble("total");
                double precioUnitario = cantidad <= 0 ? total : total / cantidad;
                compras.add(new CompraHistorial(rs.getInt("id"), rs.getInt("comprador_id"),
                        rs.getInt("producto_id"), rs.getString("nombre_producto"),
                        cantidad, precioUnitario, String.valueOf(rs.getTimestamp("fecha_pedido"))));
            }
        }
    }

    private static void cargarValoracionesDesdeBD(Connection conn) throws SQLException {
        valoraciones.clear();
        String sql = "SELECT id, usuario_id, clase_id, puntuacion, comentario, fecha FROM valoraciones ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                valoraciones.add(new Valoracion(rs.getInt("id"), rs.getInt("usuario_id"),
                        rs.getInt("clase_id"), rs.getInt("puntuacion"),
                        rs.getString("comentario"), String.valueOf(rs.getTimestamp("fecha"))));
            }
        }
    }

    private static void cargarMensajesDesdeBD(Connection conn) throws SQLException {
        mensajes.clear();
        String sql = "SELECT m.id, m.remitente_id, m.clase_id, m.contenido, m.fecha_envio, "
                + "CONCAT(u.nombre, ' ', u.apellido) AS remitente_nombre, c.titulo AS clase_titulo "
                + "FROM mensajes m "
                + "LEFT JOIN usuarios u ON u.id = m.remitente_id "
                + "LEFT JOIN clases c ON c.id = m.clase_id "
                + "ORDER BY m.id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                mensajes.add(new MensajeChat(rs.getInt("id"), rs.getInt("remitente_id"),
                        rs.getInt("clase_id"), rs.getString("remitente_nombre"),
                        rs.getString("clase_titulo"), rs.getString("contenido"),
                        String.valueOf(rs.getTimestamp("fecha_envio"))));
            }
        }
    }

    private static void recargarMensajesDesdeBD() {
        if (!usandoBaseDeDatos) return;
        try (Connection conn = DBUtil.getConnection()) {
            cargarMensajesDesdeBD(conn);
            recalcularSiguientesIds();
        } catch (SQLException e) {
            System.err.println("[DataStore] No se pudieron recargar mensajes: " + e.getMessage());
        }
    }

    private static void recargarProductosYComprasDesdeBD() {
        if (!usandoBaseDeDatos) return;
        try (Connection conn = DBUtil.getConnection()) {
            cargarProductosDesdeBD(conn);
            cargarComprasDesdeBD(conn);
            recalcularSiguientesIds();
        } catch (SQLException e) {
            System.err.println("[DataStore] No se pudieron recargar productos/compras: " + e.getMessage());
        }
    }

    private static void recalcularSiguientesIds() {
        siguienteUsuarioId    = maxId(usuarios,     u -> u.getId())     + 1;
        siguienteClaseId      = maxIdC(clases)                          + 1;
        siguienteProfesorId   = maxIdP(profesores)                      + 1;
        siguienteProductoId   = maxIdProd(productos)                    + 1;
        siguienteSuscripcionId= maxIdS(suscripciones)                   + 1;
        siguienteCompraId     = maxIdCH(compras)                        + 1;
        siguienteValoracionId = maxIdV(valoraciones)                    + 1;
        siguienteMensajeId    = maxIdM(mensajes)                        + 1;
    }

    @FunctionalInterface interface IdGetter<T> { int get(T t); }
    private static <T> int maxId(ArrayList<T> list, IdGetter<T> g) {
        int max = 0; for (T t : list) max = Math.max(max, g.get(t)); return max;
    }
    private static int maxIdC(ArrayList<ClaseDeportiva> l)  { int m=0; for (ClaseDeportiva x:l) m=Math.max(m,x.getId()); return m; }
    private static int maxIdP(ArrayList<Profesor> l)         { int m=0; for (Profesor x:l) m=Math.max(m,x.getId()); return m; }
    private static int maxIdProd(ArrayList<Producto> l)      { int m=0; for (Producto x:l) m=Math.max(m,x.getId()); return m; }
    private static int maxIdS(ArrayList<Suscripcion> l)      { int m=0; for (Suscripcion x:l) m=Math.max(m,x.getId()); return m; }
    private static int maxIdCH(ArrayList<CompraHistorial> l) { int m=0; for (CompraHistorial x:l) m=Math.max(m,x.getId()); return m; }
    private static int maxIdV(ArrayList<Valoracion> l)       { int m=0; for (Valoracion x:l) m=Math.max(m,x.getId()); return m; }
    private static int maxIdM(ArrayList<MensajeChat> l)      { int m=0; for (MensajeChat x:l) m=Math.max(m,x.getId()); return m; }

    // ──────────────────────────────────── DATOS LOCALES ───────────────────────
    private static void cargarDatosDeEjemploBasadosEnSQL() {
        usuarios.add(new Usuario(1,"Carlos","Larrea","aniri","aniri@mail.com","123456",1,"Admin","2026-04-29"));
        usuarios.add(new Usuario(2,"Ana","Lopez","analopez","ana@mail.com","123456",2,"Profesor","2026-04-29"));
        usuarios.add(new Usuario(3,"Juan","Perez","juanp","juan@mail.com","123456",2,"Profesor","2026-04-29"));
        usuarios.add(new Usuario(4,"Maria","Gomez","mariag","maria@mail.com","123456",2,"Profesor","2026-04-29"));
        usuarios.add(new Usuario(5,"Carlos","Ruiz","carlosr","carlos@mail.com","123456",3,"Alumno","2026-04-29"));
        usuarios.add(new Usuario(6,"Laura","Sanchez","lauras","laura@mail.com","123456",3,"Alumno","2026-04-29"));
        usuarios.add(new Usuario(7,"Pedro","Diaz","pedrod","pedro@mail.com","123456",3,"Alumno","2026-04-29"));
        usuarios.add(new Usuario(8,"Lucia","Martinez","luciam","lucia@mail.com","123456",2,"Profesor","2026-04-29"));
        usuarios.add(new Usuario(9,"David","Hernandez","davidh","david@mail.com","123456",3,"Alumno","2026-04-29"));
        usuarios.add(new Usuario(10,"Sofia","Garcia","sofiag","sofia@mail.com","123456",3,"Alumno","2026-04-29"));
        usuarios.add(new Usuario(11,"Miguel","Torres","miguelt","miguel@mail.com","123456",3,"Alumno","2026-04-29"));
        usuarios.add(new Usuario(12,"Elena","Vega","elenav","elena@mail.com","123456",2,"Profesor","2026-04-29"));
        usuarios.add(new Usuario(13,"Alberto","Navarro","alberton","alberto@mail.com","123456",3,"Alumno","2026-04-29"));
        usuarios.add(new Usuario(14,"Raquel","Moreno","raquelm","raquel@mail.com","123456",3,"Alumno","2026-04-29"));
        usuarios.add(new Usuario(15,"Jorge","Castro","jorgec","jorge@mail.com","123456",3,"Alumno","2026-04-29"));
        usuarios.add(new Usuario(16,"Sara","Ortega","sarao","sara@mail.com","123456",3,"Alumno","2026-04-29"));

        profesores.add(new Profesor(2,"Ana","Lopez","analopez","ana@mail.com","Yoga y running","Profesora de clases suaves"));
        profesores.add(new Profesor(3,"Juan","Perez","juanp","juan@mail.com","Fútbol","Entrenador técnico de fútbol"));
        profesores.add(new Profesor(4,"Maria","Gomez","mariag","maria@mail.com","Crossfit y boxeo","Entrenamientos intensivos"));
        profesores.add(new Profesor(8,"Lucia","Martinez","luciam","lucia@mail.com","Fútbol y ciclismo","Clases grupales"));
        profesores.add(new Profesor(12,"Elena","Vega","elenav","elena@mail.com","Pilates y fitness","Control corporal"));

        clases.add(new ClaseDeportiva(1,"Yoga para principiantes","Clase básica de yoga","Yoga",2,"Ana Lopez",false,0.00,20,"Lunes 18:00","Principiante","2026-04-29"));
        clases.add(new ClaseDeportiva(2,"Crossfit intensivo","Entrenamiento avanzado","Crossfit",4,"Maria Gomez",true,15.00,12,"Martes 19:00","Avanzado","2026-04-29"));
        clases.add(new ClaseDeportiva(3,"Futbol técnico","Mejora tu técnica","Futbol",8,"Lucia Martinez",false,0.00,18,"Miércoles 17:30","Intermedio","2026-04-29"));
        clases.add(new ClaseDeportiva(4,"Pilates avanzado","Control corporal","Pilates",12,"Elena Vega",true,12.00,10,"Jueves 18:30","Avanzado","2026-04-29"));
        clases.add(new ClaseDeportiva(5,"Running urbano","Entrenamiento de resistencia","Running",2,"Ana Lopez",false,0.00,16,"Sábado 10:00","Básico","2026-04-29"));
        clases.add(new ClaseDeportiva(6,"Boxeo básico","Defensa personal","Boxeo",4,"Maria Gomez",true,20.00,8,"Viernes 19:30","Básico","2026-04-29"));
        clases.add(new ClaseDeportiva(7,"Ciclismo indoor","Cardio intenso","Ciclismo",8,"Lucia Martinez",false,0.00,14,"Lunes 20:00","Todos los niveles","2026-04-29"));
        clases.add(new ClaseDeportiva(8,"Fitness full body","Entrenamiento completo","Fitness",12,"Elena Vega",true,18.00,11,"Miércoles 20:00","Intermedio","2026-04-29"));

        productos.add(new Producto(1,"Proteina Whey","Suplemento muscular",29.99,50,2,"Ana Lopez","2026-04-29"));
        productos.add(new Producto(2,"Botella deportiva","Botella 1L",9.99,100,4,"Maria Gomez","2026-04-29"));
        productos.add(new Producto(3,"Cuerda crossfit","Alta resistencia",14.99,30,8,"Lucia Martinez","2026-04-29"));
        productos.add(new Producto(4,"Guantes boxeo","Profesionales",24.99,20,12,"Elena Vega","2026-04-29"));
        productos.add(new Producto(5,"Esterilla yoga","Antideslizante",19.99,40,2,"Ana Lopez","2026-04-29"));

        int[][] sus = {{1,1,1},{2,1,2},{3,3,1},{4,3,4},{5,5,2},{6,6,3},{7,7,1},{8,7,5},
                       {9,9,6},{10,10,2},{11,11,4},{12,13,7},{13,14,8},{14,15,1},{15,2,3},
                       {16,4,5},{17,8,6},{18,12,2},{19,9,8},{20,5,4}};
        for (int[] d : sus) {
            suscripciones.add(new Suscripcion(d[0], d[1], d[2], "2026-04-29"));
            ClaseDeportiva c = buscarClasePorId(d[2]);
            if (c != null) c.reservarPlaza();
        }

        valoraciones.add(new Valoracion(siguienteValoracionId++,5,1,5,"Genial!","2026-04-29"));
        valoraciones.add(new Valoracion(siguienteValoracionId++,6,1,4,"Muy bien","2026-04-29"));
        valoraciones.add(new Valoracion(siguienteValoracionId++,7,2,5,"Increíble","2026-04-29"));
        valoraciones.add(new Valoracion(siguienteValoracionId++,9,3,3,"Ok","2026-04-29"));

        mensajes.add(new MensajeChat(1,1,1,"Carlos Larrea","Yoga para principiantes","¿A qué hora empieza la clase?","2026-04-29"));
        mensajes.add(new MensajeChat(2,3,2,"Juan Perez","Crossfit intensivo","Me gusta mucho este entrenamiento","2026-04-29"));
        mensajes.add(new MensajeChat(3,5,2,"Carlos Ruiz","Crossfit intensivo","Muy intensa la sesión","2026-04-29"));
    }

    // ──────────────────────────────────── AUTH ────────────────────────────────
    public static Usuario iniciarSesion(String correo, String password) {
        if (usandoBaseDeDatos) recargarUsuariosParaLogin();
        for (Usuario u : usuarios)
            if (u.getCorreo().equalsIgnoreCase(correo) && u.getPassword().equals(password)) return u;
        return null;
    }

    private static void recargarUsuariosParaLogin() {
        try (Connection conn = DBUtil.getConnection()) {
            usuarios.clear(); profesores.clear();
            cargarUsuariosDesdeBD(conn);
            cargarProfesoresDesdeBD(conn);
            recalcularSiguientesIds();
        } catch (SQLException e) {
            System.err.println("[DataStore] No se pudieron recargar usuarios: " + e.getMessage());
        }
    }

    public static boolean correoExiste(String correo) {
        if (usandoBaseDeDatos) {
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE LOWER(email)=LOWER(?)")) {
                ps.setString(1, correo);
                try (ResultSet rs = ps.executeQuery()) { return rs.next() && rs.getInt(1) > 0; }
            } catch (SQLException e) { System.err.println("[DataStore] Error email: " + e.getMessage()); }
        }
        for (Usuario u : usuarios) if (u.getCorreo().equalsIgnoreCase(correo)) return true;
        return false;
    }

    public static boolean nickExiste(String nick) {
        if (usandoBaseDeDatos) {
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE LOWER(nick)=LOWER(?)")) {
                ps.setString(1, nick);
                try (ResultSet rs = ps.executeQuery()) { return rs.next() && rs.getInt(1) > 0; }
            } catch (SQLException e) { System.err.println("[DataStore] Error nick: " + e.getMessage()); }
        }
        for (Usuario u : usuarios) if (u.getNick().equalsIgnoreCase(nick)) return true;
        return false;
    }

    public static void registrarUsuario(Usuario usuario) {
        if (usandoBaseDeDatos) {
            String sql = "INSERT INTO usuarios(nombre, apellido, nick, email, password, rol_id) VALUES (?,?,?,?,?,?)";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getApellido() == null ? "" : usuario.getApellido());
                ps.setString(3, usuario.getNick());
                ps.setString(4, usuario.getEmail());
                ps.setString(5, usuario.getPassword());
                ps.setInt(6, usuario.getRolId());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) usuario.setId(keys.getInt(1)); }
                recargarDatos();
                return;
            } catch (SQLException e) {
                throw new RuntimeException("No se pudo registrar el usuario: " + e.getMessage(), e);
            }
        }
        usuario.setId(siguienteUsuarioId++);
        usuario.setFechaCreacion(LocalDate.now().toString());
        usuarios.add(usuario);
        if (usuario.esProfesor())
            profesores.add(new Profesor(usuario.getId(), usuario.getNombre(), usuario.getApellido(),
                    usuario.getNick(), usuario.getEmail(), "Sin especialidad", "Nuevo profesor"));
    }

    // ──────────────────────────────────── GETTERS ─────────────────────────────
    public static ArrayList<Usuario>       getUsuarios()   { return usuarios; }
    public static ArrayList<ClaseDeportiva> getClases()    { return clases; }
    public static ArrayList<Profesor>      getProfesores() { return profesores; }
    public static AppConfig                getAppConfig()  { return appConfig; }
    public static ArrayList<Valoracion>    getValoraciones(){ return valoraciones; }

    /** Productos: recarga desde BD solo si hay BD activa (evita N queries por render). */
    public static ArrayList<Producto> getProductos() {
        if (usandoBaseDeDatos) recargarProductosYComprasDesdeBD();
        return productos;
    }

    public static ArrayList<CompraHistorial> getCompras() {
        if (usandoBaseDeDatos) recargarProductosYComprasDesdeBD();
        return compras;
    }

    public static ArrayList<MensajeChat> getUltimosMensajes(int limite) {
        if (usandoBaseDeDatos) recargarMensajesDesdeBD();
        ArrayList<MensajeChat> resultado = new ArrayList<>();
        int max = Math.max(0, limite);
        for (MensajeChat m : mensajes) {
            if (resultado.size() >= max) break;
            resultado.add(m);
        }
        return resultado;
    }

    // ──────────────────────────────────── MENSAJES ────────────────────────────
    public static void enviarMensaje(int remitenteId, int claseId, String contenido) {
        if (contenido == null || contenido.isBlank()) return;

        if (usandoBaseDeDatos) {
            String sql = "INSERT INTO mensajes(remitente_id, clase_id, contenido) VALUES (?,?,?)";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, remitenteId);
                ps.setInt(2, claseId);
                ps.setString(3, contenido.trim());
                ps.executeUpdate();
                recargarMensajesDesdeBD();
                return;
            } catch (SQLException e) {
                // Error de FK u otro → informar sin romper la app
                throw new RuntimeException("No se pudo enviar el mensaje: " + e.getMessage(), e);
            }
        }

        Usuario usuario = buscarUsuarioPorId(remitenteId);
        ClaseDeportiva clase = buscarClasePorId(claseId);
        String remitente  = usuario == null ? "Usuario" : usuario.getNombreCompleto();
        String tituloC    = clase   == null ? "Clase"   : clase.getTitulo();
        mensajes.add(0, new MensajeChat(siguienteMensajeId++, remitenteId, claseId,
                remitente, tituloC, contenido.trim(), LocalDate.now().toString()));
    }

    // ──────────────────────────────────── CLASES ──────────────────────────────
    public static void agregarClase(ClaseDeportiva clase) {
        if (usandoBaseDeDatos) {
            String sql = "INSERT INTO clases(titulo, descripcion, deporte, profesor_id, es_premium, precio) VALUES (?,?,?,?,?,?)";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, clase.getTitulo()); ps.setString(2, clase.getDescripcion());
                ps.setString(3, clase.getDeporte()); ps.setInt(4, clase.getProfesorId());
                ps.setBoolean(5, clase.isPremium()); ps.setDouble(6, clase.getPrecio());
                ps.executeUpdate();
                try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) clase.setId(k.getInt(1)); }
                recargarDatos(); return;
            } catch (SQLException e) { throw new RuntimeException("No se pudo crear la clase: " + e.getMessage(), e); }
        }
        clase.setId(siguienteClaseId++); clases.add(clase);
    }

    public static void actualizarClase(int posicion, ClaseDeportiva c) {
        if (posicion < 0 || posicion >= clases.size()) return;
        int id = clases.get(posicion).getId(); c.setId(id);
        if (usandoBaseDeDatos) {
            String sql = "UPDATE clases SET titulo=?,descripcion=?,deporte=?,profesor_id=?,es_premium=?,precio=? WHERE id=?";
            try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1,c.getTitulo()); ps.setString(2,c.getDescripcion()); ps.setString(3,c.getDeporte());
                ps.setInt(4,c.getProfesorId()); ps.setBoolean(5,c.isPremium()); ps.setDouble(6,c.getPrecio()); ps.setInt(7,id);
                ps.executeUpdate(); recargarDatos(); return;
            } catch (SQLException e) { throw new RuntimeException("No se pudo actualizar la clase: " + e.getMessage(), e); }
        }
        clases.set(posicion, c);
    }

    public static void eliminarClase(int posicion) {
        if (posicion < 0 || posicion >= clases.size()) return;
        int id = clases.get(posicion).getId();
        if (usandoBaseDeDatos) {
            try (Connection conn = DBUtil.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    for (String sql : new String[]{
                            "DELETE FROM valoraciones WHERE clase_id=?",
                            "DELETE FROM pagos WHERE clase_id=?",
                            "DELETE FROM asistencias WHERE clase_id=?",
                            "DELETE FROM mensajes WHERE clase_id=?",
                            "DELETE FROM suscripciones WHERE clase_id=?",
                            "DELETE FROM clases WHERE id=?"}) {
                        try (PreparedStatement ps = conn.prepareStatement(sql)) { ps.setInt(1,id); ps.executeUpdate(); }
                    }
                    conn.commit(); recargarDatos(); return;
                } catch (SQLException e) { conn.rollback(); throw e; }
                finally { conn.setAutoCommit(true); }
            } catch (SQLException e) { throw new RuntimeException("No se pudo eliminar la clase: " + e.getMessage(), e); }
        }
        clases.remove(posicion); suscripciones.removeIf(s -> s.getClaseId() == id);
    }

    // ──────────────────────────────────── PRODUCTOS ───────────────────────────
    public static void agregarProducto(Producto p) {
        if (usandoBaseDeDatos) {
            String sql = "INSERT INTO productos(nombre, descripcion, precio, stock, vendedor_id) VALUES (?,?,?,?,?)";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,p.getNombre()); ps.setString(2,p.getDescripcion());
                ps.setDouble(3,p.getPrecio()); ps.setInt(4,p.getStock()); ps.setInt(5,p.getVendedorId());
                ps.executeUpdate();
                try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) p.setId(k.getInt(1)); }
                recargarProductosYComprasDesdeBD(); return;
            } catch (SQLException e) { throw new RuntimeException("No se pudo crear el producto: " + e.getMessage(), e); }
        }
        p.setId(siguienteProductoId++); productos.add(p);
    }

    public static void actualizarProducto(int pos, Producto p) {
        if (pos < 0 || pos >= productos.size()) return;
        int id = productos.get(pos).getId(); p.setId(id);
        if (usandoBaseDeDatos) {
            String sql = "UPDATE productos SET nombre=?,descripcion=?,precio=?,stock=?,vendedor_id=? WHERE id=?";
            try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1,p.getNombre()); ps.setString(2,p.getDescripcion()); ps.setDouble(3,p.getPrecio());
                ps.setInt(4,p.getStock()); ps.setInt(5,p.getVendedorId()); ps.setInt(6,id);
                ps.executeUpdate(); recargarProductosYComprasDesdeBD(); return;
            } catch (SQLException e) { throw new RuntimeException("No se pudo actualizar el producto: " + e.getMessage(), e); }
        }
        productos.set(pos, p);
    }

    public static void eliminarProducto(int pos) {
        if (pos < 0 || pos >= productos.size()) return;
        int id = productos.get(pos).getId();
        if (usandoBaseDeDatos) {
            try (Connection conn = DBUtil.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    try (PreparedStatement p1 = conn.prepareStatement("DELETE FROM pedidos WHERE producto_id=?");
                         PreparedStatement p2 = conn.prepareStatement("DELETE FROM productos WHERE id=?")) {
                        p1.setInt(1,id); p1.executeUpdate(); p2.setInt(1,id); p2.executeUpdate();
                    }
                    conn.commit(); recargarProductosYComprasDesdeBD(); return;
                } catch (SQLException e) { conn.rollback(); throw e; }
                finally { conn.setAutoCommit(true); }
            } catch (SQLException e) { throw new RuntimeException("No se pudo eliminar el producto: " + e.getMessage(), e); }
        }
        productos.remove(pos);
    }

    // ──────────────────────────────────── USUARIOS ────────────────────────────
    public static void actualizarUsuario(int pos, Usuario u) {
        if (pos < 0 || pos >= usuarios.size()) return;
        int id = usuarios.get(pos).getId(); u.setId(id);
        if (usandoBaseDeDatos) { actualizarUsuarioEnBD(u); recargarDatos(); return; }
        usuarios.set(pos, u);
    }

    public static void actualizarUsuarioPorId(Usuario u) {
        if (usandoBaseDeDatos) { actualizarUsuarioEnBD(u); recargarDatos(); return; }
        for (int i = 0; i < usuarios.size(); i++)
            if (usuarios.get(i).getId() == u.getId()) { usuarios.set(i, u); return; }
    }

    public static void eliminarUsuario(int pos) {
        if (pos < 0 || pos >= usuarios.size()) return;
        int uid = usuarios.get(pos).getId();
        if (usandoBaseDeDatos) {
            try (Connection conn = DBUtil.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    for (String sql : new String[]{
                            "DELETE FROM pedidos WHERE comprador_id=?",
                            "DELETE FROM valoraciones WHERE usuario_id=?",
                            "DELETE FROM pagos WHERE usuario_id=?",
                            "DELETE FROM asistencias WHERE usuario_id=?",
                            "DELETE FROM mensajes WHERE remitente_id=?",
                            "DELETE FROM suscripciones WHERE usuario_id=?",
                            "DELETE FROM productos WHERE vendedor_id=?",
                            "DELETE FROM clases WHERE profesor_id=?",
                            "DELETE FROM usuarios WHERE id=?"}) {
                        try (PreparedStatement ps = conn.prepareStatement(sql)) { ps.setInt(1,uid); ps.executeUpdate(); }
                    }
                    conn.commit(); recargarDatos(); return;
                } catch (SQLException e) { conn.rollback(); throw e; }
                finally { conn.setAutoCommit(true); }
            } catch (SQLException e) { throw new RuntimeException("No se pudo eliminar el usuario: " + e.getMessage(), e); }
        }
        usuarios.remove(pos); suscripciones.removeIf(s -> s.getUsuarioId() == uid);
    }

    private static void actualizarUsuarioEnBD(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?,apellido=?,nick=?,email=?,password=?,rol_id=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,u.getNombre()); ps.setString(2, u.getApellido()==null?"":u.getApellido());
            ps.setString(3,u.getNick()); ps.setString(4,u.getEmail());
            ps.setString(5,u.getPassword()); ps.setInt(6,u.getRolId()); ps.setInt(7,u.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("No se pudo actualizar el usuario: " + e.getMessage(), e); }
    }

    // ──────────────────────────────────── SUSCRIPCIONES ──────────────────────
    public static boolean apuntarUsuarioAClase(Usuario usuario, ClaseDeportiva clase) {
        if (usuario == null || clase == null || estaApuntado(usuario, clase) || !clase.hayPlazas()) return false;
        if (usandoBaseDeDatos) {
            // Llamada al stored procedure suscribirse_clase(usuario_id, clase_id)
            try (Connection conn = DBUtil.getConnection();
                 CallableStatement cs = conn.prepareCall("{CALL suscribirse_clase(?,?)}")) {
                cs.setInt(1, usuario.getId());
                cs.setInt(2, clase.getId());
                cs.execute();
                recargarDatos();
                return true;
            } catch (SQLException e) {
                // SQLState 45000 = error de negocio lanzado por el stored procedure (ya suscrito, etc.)
                System.err.println("[DataStore] suscribirse_clase: " + e.getMessage());
                return false;
            }
        }
        suscripciones.add(new Suscripcion(siguienteSuscripcionId++, usuario.getId(), clase.getId(), LocalDate.now().toString()));
        clase.reservarPlaza();
        return true;
    }

    public static boolean cancelarInscripcion(Usuario usuario, ClaseDeportiva clase) {
        if (usuario == null || clase == null) return false;
        if (usandoBaseDeDatos) {
            String sql = "DELETE FROM suscripciones WHERE usuario_id=? AND clase_id=?";
            try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1,usuario.getId()); ps.setInt(2,clase.getId());
                boolean ok = ps.executeUpdate() > 0;
                recargarDatos(); return ok;
            } catch (SQLException e) { System.err.println("[DataStore] cancelar inscripción: " + e.getMessage()); return false; }
        }
        boolean ok = suscripciones.removeIf(s -> s.getUsuarioId()==usuario.getId() && s.getClaseId()==clase.getId());
        if (ok) clase.liberarPlaza();
        return ok;
    }

    public static boolean estaApuntado(Usuario usuario, ClaseDeportiva clase) {
        if (usuario == null || clase == null) return false;
        for (Suscripcion s : suscripciones)
            if (s.getUsuarioId()==usuario.getId() && s.getClaseId()==clase.getId()) return true;
        return false;
    }

    public static ArrayList<ClaseDeportiva> getClasesApuntadas(Usuario usuario) {
        ArrayList<ClaseDeportiva> res = new ArrayList<>();
        if (usuario == null) return res;
        for (Suscripcion s : suscripciones) {
            if (s.getUsuarioId()==usuario.getId()) {
                ClaseDeportiva c = buscarClasePorId(s.getClaseId());
                if (c != null) res.add(c);
            }
        }
        return res;
    }

    public static ArrayList<ClaseDeportiva> getClasesDelProfesor(Usuario profesor) {
        ArrayList<ClaseDeportiva> res = new ArrayList<>();
        if (profesor == null) return res;
        for (ClaseDeportiva c : clases) if (c.getProfesorId()==profesor.getId()) res.add(c);
        return res;
    }

    // ──────────────────────────────────── COMPRAS ─────────────────────────────
    public static void registrarCompra(int usuarioId, int productoId, String nombreProducto, int cantidad, double precioUnitario) {
        if (usandoBaseDeDatos) {
            String sql = "INSERT INTO pedidos(comprador_id, producto_id, cantidad, total) VALUES (?,?,?,?)";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1,usuarioId); ps.setInt(2,productoId); ps.setInt(3,cantidad); ps.setDouble(4,precioUnitario*cantidad);
                ps.executeUpdate(); recargarProductosYComprasDesdeBD(); return;
            } catch (SQLException e) { throw new RuntimeException("No se pudo registrar la compra: " + e.getMessage(), e); }
        }
        compras.add(new CompraHistorial(siguienteCompraId++, usuarioId, productoId, nombreProducto, cantidad, precioUnitario, LocalDate.now().toString()));
    }

    public static boolean procesarCompraCompleta(int usuarioId, ArrayList<ItemCarrito> items) {
        if (items == null || items.isEmpty()) return false;
        if (!usandoBaseDeDatos) {
            for (ItemCarrito item : items) if (item.getProducto().getStock() < item.getCantidad()) return false;
            for (ItemCarrito item : items) {
                Producto p = item.getProducto();
                registrarCompra(usuarioId, p.getId(), p.getNombre(), item.getCantidad(), p.getPrecio());
                p.setStock(p.getStock() - item.getCantidad());
            }
            return true;
        }
        // Modo BD: transacción con bloqueo optimista
        String sqlSelect = "SELECT nombre, precio, stock FROM productos WHERE id=? FOR UPDATE";
        String sqlInsert = "INSERT INTO pedidos(comprador_id, producto_id, cantidad, total) VALUES (?,?,?,?)";
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psS = conn.prepareStatement(sqlSelect);
                 PreparedStatement psI = conn.prepareStatement(sqlInsert)) {
                for (ItemCarrito item : items) {
                    int productoId = item.getProducto().getId();
                    int cantidad   = item.getCantidad();
                    psS.setInt(1, productoId);
                    try (ResultSet rs = psS.executeQuery()) {
                        if (!rs.next()) { conn.rollback(); return false; }
                        if (rs.getInt("stock") < cantidad) { conn.rollback(); return false; }
                        double precio = rs.getDouble("precio");
                        psI.setInt(1,usuarioId); psI.setInt(2,productoId);
                        psI.setInt(3,cantidad); psI.setDouble(4,precio*cantidad);
                        psI.executeUpdate();
                    }
                }
                conn.commit(); recargarProductosYComprasDesdeBD(); return true;
            } catch (SQLException e) { conn.rollback(); throw e; }
            finally { conn.setAutoCommit(true); }
        } catch (SQLException e) { throw new RuntimeException("No se pudo procesar la compra: " + e.getMessage(), e); }
    }

    public static ArrayList<CompraHistorial> getComprasDeUsuario(int usuarioId) {
        if (usandoBaseDeDatos) recargarProductosYComprasDesdeBD();
        ArrayList<CompraHistorial> res = new ArrayList<>();
        for (CompraHistorial c : compras) if (c.getUsuarioId()==usuarioId) res.add(c);
        return res;
    }

    // ──────────────────────────────────── VALORACIONES ───────────────────────
    public static void agregarValoracion(Valoracion v) {
        if (usandoBaseDeDatos) {
            String sql = "INSERT INTO valoraciones(usuario_id, clase_id, puntuacion, comentario) VALUES (?,?,?,?)";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1,v.getUsuarioId()); ps.setInt(2,v.getClaseId());
                ps.setInt(3,v.getEstrellas()); ps.setString(4,v.getComentario());
                ps.executeUpdate(); cargarDatosDesdeBaseDeDatos(); return;
            } catch (SQLException e) { throw new RuntimeException("No se pudo guardar la valoración: " + e.getMessage(), e); }
        }
        valoraciones.add(v);
    }

    public static boolean yaValoroClase(int usuarioId, int claseId) {
        for (Valoracion v : valoraciones) if (v.getUsuarioId()==usuarioId && v.getClaseId()==claseId) return true;
        return false;
    }

    public static double getMediaValoraciones(int claseId) {
        int total=0, count=0;
        for (Valoracion v : valoraciones) if (v.getClaseId()==claseId) { total+=v.getEstrellas(); count++; }
        return count==0 ? 0 : (double)total/count;
    }

    public static ArrayList<Valoracion> getValoracionesDeClase(int claseId) {
        ArrayList<Valoracion> res = new ArrayList<>();
        for (Valoracion v : valoraciones) if (v.getClaseId()==claseId) res.add(v);
        return res;
    }

    public static int getSiguienteValoracionId() { return siguienteValoracionId++; }

    // ──────────────────────────────────── BÚSQUEDAS ───────────────────────────
    public static int getIndiceClase(ClaseDeportiva c) {
        for (int i=0; i<clases.size(); i++) if (clases.get(i).getId()==c.getId()) return i;
        return -1;
    }

    public static ClaseDeportiva buscarClasePorId(int id) {
        for (ClaseDeportiva c : clases) if (c.getId()==id) return c;
        return null;
    }

    public static Usuario buscarUsuarioPorId(int id) {
        for (Usuario u : usuarios) if (u.getId()==id) return u;
        return null;
    }

    public static String getProfesorNombreById(int id) {
        Usuario u = buscarUsuarioPorId(id);
        return u==null ? "Desconocido" : u.getNombreCompleto();
    }

    // ──────────────────────────────────── STATS ───────────────────────────────
    public static int contarReservasTotales()    { return suscripciones.size(); }
    public static int contarAlumnos()            { return contarPorRol("Alumno"); }
    public static int contarProfesoresUsuarios() { return contarPorRol("Profesor"); }
    private static int contarPorRol(String rol) {
        int c=0; for (Usuario u : usuarios) if (u.getTipo().equalsIgnoreCase(rol)) c++; return c;
    }

    public static HashMap<Integer,Integer> contarInscripcionesPorClase() {
        HashMap<Integer,Integer> res = new HashMap<>();
        for (Suscripcion s : suscripciones)
            res.put(s.getClaseId(), res.getOrDefault(s.getClaseId(),0)+1);
        return res;
    }

    public static ArrayList<Producto> buscarProductos(String texto) {
        if (usandoBaseDeDatos) recargarProductosYComprasDesdeBD();
        ArrayList<Producto> res = new ArrayList<>();
        String t = texto==null ? "" : texto.toLowerCase().trim();
        for (Producto p : productos) {
            String n = p.getNombre()==null ? "" : p.getNombre().toLowerCase();
            String d = p.getDescripcion()==null ? "" : p.getDescripcion().toLowerCase();
            if (n.contains(t) || d.contains(t)) res.add(p);
        }
        return res;
    }

    public static ArrayList<ClaseDeportiva> buscarClases(String texto, String deporte) {
        ArrayList<ClaseDeportiva> res = new ArrayList<>();
        String t = texto==null ? "" : texto.toLowerCase().trim();
        for (ClaseDeportiva c : clases) {
            boolean okTexto   = t.isEmpty() || c.getTitulo().toLowerCase().contains(t) || c.getDeporte().toLowerCase().contains(t);
            boolean okDeporte = deporte==null || deporte.equals("Todos") || c.getDeporte().equalsIgnoreCase(deporte);
            if (okTexto && okDeporte) res.add(c);
        }
        return res;
    }

    // ──────────────────────────────────── PROFESORES (local) ──────────────────
    public static void agregarProfesor(Profesor p)  { p.setId(siguienteProfesorId++); profesores.add(p); }
    public static void actualizarProfesor(int pos, Profesor p) {
        if (pos>=0 && pos<profesores.size()) { p.setId(profesores.get(pos).getId()); profesores.set(pos,p); }
    }
    public static void eliminarProfesor(int pos) { if (pos>=0 && pos<profesores.size()) profesores.remove(pos); }
}
