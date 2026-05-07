package org.example.sportconnect_01;

import org.example.sportconnect_01.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConexionBD {

    public static void main(String[] args) {
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT DATABASE() AS bd, COUNT(*) AS total FROM productos")) {

            if (rs.next()) {
                System.out.println("Conexión correcta con MySQL remoto.");
                System.out.println("Base de datos: " + rs.getString("bd"));
                System.out.println("Productos encontrados: " + rs.getInt("total"));
            }

        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos MySQL remota.");
            e.printStackTrace();
        }
    }
}
