package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/carwash";
    private static final String USER = "root";
    private static final String PASSWORD = "60574283"; 

    public static Connection conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión BD: " + e.getMessage());
            return null;
        }
    }
}