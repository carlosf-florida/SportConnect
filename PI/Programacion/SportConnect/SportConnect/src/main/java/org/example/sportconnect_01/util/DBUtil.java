package org.example.sportconnect_01.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utilidad unica para conectar SportConnect con MySQL remoto en AWS EC2.
 *
 * La configuracion principal esta en:
 *   src/main/resources/db.properties
 *
 * Tambien permite sobrescribir los datos con variables de entorno o parametros JVM:
 *   DB_HOST, DB_PORT, DB_DATABASE, DB_USER, DB_PASSWORD
 *   -Ddb.host=... -Ddb.port=... -Ddb.database=... -Ddb.user=... -Ddb.password=...
 */
public class DBUtil {

    private static final String DEFAULT_HOST = "34.206.126.154";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DATABASE = "sportconnect";
    private static final String DEFAULT_USER = "sportconnect_user";
    private static final String DEFAULT_PASSWORD = "Sport1234!";

    private static final String HOST;
    private static final String PORT;
    private static final String DATABASE;
    private static final String USER;
    private static final String PASSWORD;
    private static final String URL;

    static {
        cargarDriverMySQL();

        Properties props = cargarPropiedades();

        HOST = leerConfig(props, "db.host", "DB_HOST", DEFAULT_HOST);
        PORT = leerConfig(props, "db.port", "DB_PORT", DEFAULT_PORT);
        DATABASE = leerConfig(props, "db.database", "DB_DATABASE", DEFAULT_DATABASE);
        USER = leerConfig(props, "db.user", "DB_USER", DEFAULT_USER);
        PASSWORD = leerConfig(props, "db.password", "DB_PASSWORD", DEFAULT_PASSWORD);

        URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
                + "?useSSL=false"
                + "&serverTimezone=UTC"
                + "&allowPublicKeyRetrieval=true"
                + "&characterEncoding=UTF-8"
                + "&useUnicode=true"
                + "&connectTimeout=7000"
                + "&socketTimeout=15000";

        System.out.println("[DBUtil] MySQL configurado en " + getResumenConexion());
    }

    private static void cargarDriverMySQL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "No se encontro el driver de MySQL. Revisa que mysql-connector-j este en el pom.xml."
            );
        }
    }

    private static Properties cargarPropiedades() {
        Properties props = new Properties();

        try (InputStream input = DBUtil.class.getResourceAsStream("/db.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                System.err.println("[DBUtil] No se encontro db.properties. Se usaran los valores por defecto.");
            }
        } catch (IOException e) {
            System.err.println("[DBUtil] No se pudo leer db.properties: " + e.getMessage());
        }

        return props;
    }

    private static String leerConfig(Properties props, String clave, String variableEntorno, String valorPorDefecto) {
        String parametroJvm = System.getProperty(clave);
        if (parametroJvm != null && !parametroJvm.isBlank()) {
            return parametroJvm.trim();
        }

        String entorno = System.getenv(variableEntorno);
        if (entorno != null && !entorno.isBlank()) {
            return entorno.trim();
        }

        String valor = props.getProperty(clave);
        if (valor != null && !valor.isBlank()) {
            return valor.trim();
        }

        return valorPorDefecto;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static String probarConexion() {
        try (Connection conn = getConnection()) {
            if (!conn.isValid(5)) {
                return "La conexion se abrio, pero MySQL no la valida correctamente.";
            }
            return null;
        } catch (SQLException e) {
            String mensaje = e.getMessage();
            System.err.println("[DBUtil] Error conectando con MySQL en " + getResumenConexion());
            System.err.println("[DBUtil] Detalle: " + mensaje);
            return mensaje + "\n\nConexion usada: " + getResumenConexion()
                    + "\nComprueba en AWS que el grupo de seguridad permita TCP 3306 desde tu IP,"
                    + " que Docker tenga publicado -p 3306:3306 y que el usuario MySQL tenga permisos remotos.";
        }
    }

    public static String getResumenConexion() {
        return HOST + ":" + PORT + "/" + DATABASE + " usuario=" + USER;
    }

    @Deprecated
    public static boolean testConexionBoolean() {
        return probarConexion() == null;
    }
}
