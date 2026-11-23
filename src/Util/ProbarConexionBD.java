package Util;

import java.sql.Connection;
import java.sql.SQLException;

public class ProbarConexionBD {

    public static void main(String[] args) {
        try (Connection con = ConexionBD.conn()) {
            if (con != null && !con.isClosed()) {
                System.out.println("Conexion exitosa a la base de datos carwash");
            } else {
                System.out.println("No se pudo establecer la conexion (revisa el mensaje de error).");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar o validar la conexión: " + e.getMessage());
        }
    }
}