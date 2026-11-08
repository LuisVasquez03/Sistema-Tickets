package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionBD {
    public static Connection conn(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url= "jdbc:mysql://localhost:3306/carwash";
            String username = "root";
            String password = "";
            Connection conn = DriverManager.getConnection(url, username, password);
            JOptionPane.showMessageDialog(null,"¡Conexion exitosa con MySQL!");
            return conn;
        } catch (ClassNotFoundException|SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        
        return null;
    }
}
