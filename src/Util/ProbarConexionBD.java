package Util;

import java.sql.Connection;
import java.sql.SQLException;

public class ProbarConexionBD {

    public static void main(String[] args) {
        try (Connection con = ConexionBD.conn()) {
            System.out.println("Conexión exitosa");

        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }

}
