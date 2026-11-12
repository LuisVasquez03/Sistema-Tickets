package Servicio;

import Modelo.Trabajador;
import Util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Trabajador_servicio {

    /**
     * Valida el inicio de sesión de un trabajador buscando su usuario y contraseña.
     * 
     * @param usuario Nombre de usuario o login
     * @param clave Contraseña ingresada
     * @return Objeto Trabajador si las credenciales son correctas, o null si falla.
     */
    public Trabajador iniciarSesion(String usuario, String clave) {
        if (usuario == null || usuario.isBlank() || clave == null || clave.isBlank()) {
            return null;
        }

        String sql = "SELECT idTrabajador, primerNombre, primerApellido, codigoPais, celular, cargo, usuario, claveHash, activo "
                   + "FROM trabajadores WHERE usuario = ? AND claveHash = ? AND activo = 1";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, usuario.trim());
            pstmt.setString(2, clave.trim()); // Si manejas hashes, aquí deberías aplicar la función de encriptación correspondiente

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Trabajador trabajador = new Trabajador();
                    trabajador.setIdTrabajador(rs.getInt("idTrabajador"));
                    trabajador.setPrimerNombre(rs.getString("primerNombre"));
                    trabajador.setPrimerApellido(rs.getString("primerApellido"));
                    trabajador.setCodigoPais(rs.getString("codigoPais"));
                    trabajador.setCelular(rs.getString("celular"));
                    trabajador.setCargo(rs.getString("cargo"));
                    trabajador.setUsuario(rs.getString("usuario"));
                    trabajador.setClaveHash(rs.getString("claveHash"));
                    trabajador.setActivo(rs.getBoolean("activo"));
                    return trabajador;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar trabajador en la BD: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca un trabajador por su ID único.
     */
    public Trabajador buscarPorId(int idTrabajador) {
        String sql = "SELECT idTrabajador, primerNombre, primerApellido, codigoPais, celular, cargo, usuario, claveHash, activo "
                   + "FROM trabajadores WHERE idTrabajador = ?";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, idTrabajador);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Trabajador trabajador = new Trabajador();
                    trabajador.setIdTrabajador(rs.getInt("idTrabajador"));
                    trabajador.setPrimerNombre(rs.getString("primerNombre"));
                    trabajador.setPrimerApellido(rs.getString("primerApellido"));
                    trabajador.setCodigoPais(rs.getString("codigoPais"));
                    trabajador.setCelular(rs.getString("celular"));
                    trabajador.setCargo(rs.getString("cargo"));
                    trabajador.setUsuario(rs.getString("usuario"));
                    trabajador.setClaveHash(rs.getString("claveHash"));
                    trabajador.setActivo(rs.getBoolean("activo"));
                    return trabajador;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar trabajador por ID: " + e.getMessage());
        }

        return null;
    }
}