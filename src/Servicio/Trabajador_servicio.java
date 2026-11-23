package Servicio;

import Modelo.Trabajador;
import Util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Trabajador_servicio {

    public Trabajador iniciarSesion(String usuario, String clave) {
        if (usuario == null || usuario.isBlank() || clave == null || clave.isBlank()) {
            return null;
        }

        String sql = "SELECT ID_TRABAJADOR, PRIMER_NOMBRE, PRIMER_APELLIDO, CODIGO_PAIS, CELULAR, "
                   + "CARGO, USUARIO, CLAVE_HASH, ACTIVO "
                   + "FROM TRABAJADOR WHERE USUARIO = ? AND CLAVE_HASH = ? AND ACTIVO = 1";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return null;
            }

            pstmt.setString(1, usuario.trim());
            pstmt.setString(2, clave.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTrabajador(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar trabajador en la BD: " + e.getMessage());
        }

        return null;
    }

    public List<Trabajador> listarTodos() {
        List<Trabajador> lista = new ArrayList<>();
        String sql = "SELECT ID_TRABAJADOR, PRIMER_NOMBRE, PRIMER_APELLIDO, CODIGO_PAIS, CELULAR, "
                   + "CARGO, USUARIO, CLAVE_HASH, ACTIVO "
                   + "FROM TRABAJADOR ORDER BY ID_TRABAJADOR ASC";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null;
             ResultSet rs = pstmt != null ? pstmt.executeQuery() : null) {

            while (rs != null && rs.next()) {
                lista.add(mapearTrabajador(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar trabajadores: " + e.getMessage());
        }

        return lista;
    }

    public Trabajador buscarPorId(int idTrabajador) {
        if (idTrabajador <= 0) {
            return null;
        }

        String sql = "SELECT ID_TRABAJADOR, PRIMER_NOMBRE, PRIMER_APELLIDO, CODIGO_PAIS, CELULAR, "
                   + "CARGO, USUARIO, CLAVE_HASH, ACTIVO "
                   + "FROM TRABAJADOR WHERE ID_TRABAJADOR = ?";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return null;
            }

            pstmt.setInt(1, idTrabajador);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTrabajador(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar trabajador por ID: " + e.getMessage());
        }

        return null;
    }

    public List<Trabajador> buscarPorNombre(String nombre) {
        List<Trabajador> lista = new ArrayList<>();
        if (nombre == null || nombre.isBlank()) {
            return lista;
        }

        String sql = "SELECT ID_TRABAJADOR, PRIMER_NOMBRE, PRIMER_APELLIDO, CODIGO_PAIS, CELULAR, "
                   + "CARGO, USUARIO, CLAVE_HASH, ACTIVO "
                   + "FROM TRABAJADOR WHERE PRIMER_NOMBRE LIKE ? ORDER BY ID_TRABAJADOR ASC";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return lista;
            }

            pstmt.setString(1, "%" + nombre.trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearTrabajador(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar trabajador por nombre: " + e.getMessage());
        }

        return lista;
    }

    public List<Trabajador> buscarPorApellido(String apellido) {
        List<Trabajador> lista = new ArrayList<>();
        if (apellido == null || apellido.isBlank()) {
            return lista;
        }

        String sql = "SELECT ID_TRABAJADOR, PRIMER_NOMBRE, PRIMER_APELLIDO, CODIGO_PAIS, CELULAR, "
                   + "CARGO, USUARIO, CLAVE_HASH, ACTIVO "
                   + "FROM TRABAJADOR WHERE PRIMER_APELLIDO LIKE ? ORDER BY ID_TRABAJADOR ASC";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return lista;
            }

            pstmt.setString(1, "%" + apellido.trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearTrabajador(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar trabajador por apellido: " + e.getMessage());
        }

        return lista;
    }

    public boolean insertar(Trabajador t) {
        if (t == null) {
            return false;
        }

        String sql = "INSERT INTO TRABAJADOR (PRIMER_NOMBRE, PRIMER_APELLIDO, CODIGO_PAIS, CELULAR, "
                   + "CARGO, USUARIO, CLAVE_HASH, ACTIVO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return false;
            }

            pstmt.setString(1, t.getPrimerNombre());
            pstmt.setString(2, t.getPrimerApellido());
            pstmt.setString(3, t.getCodigoPais());
            pstmt.setString(4, t.getCelular());
            pstmt.setString(5, t.getCargo());
            pstmt.setString(6, t.getUsuario());
            pstmt.setString(7, t.getClaveHash());
            pstmt.setBoolean(8, t.isActivo());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar trabajador: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Trabajador t) {
        if (t == null || t.getIdTrabajador() <= 0) {
            return false;
        }

        boolean actualizarClave = t.getClaveHash() != null && !t.getClaveHash().isBlank();
        String sql = actualizarClave
                ? "UPDATE TRABAJADOR SET PRIMER_NOMBRE = ?, PRIMER_APELLIDO = ?, CODIGO_PAIS = ?, "
                  + "CELULAR = ?, CARGO = ?, USUARIO = ?, CLAVE_HASH = ?, ACTIVO = ? WHERE ID_TRABAJADOR = ?"
                : "UPDATE TRABAJADOR SET PRIMER_NOMBRE = ?, PRIMER_APELLIDO = ?, CODIGO_PAIS = ?, "
                  + "CELULAR = ?, CARGO = ?, USUARIO = ?, ACTIVO = ? WHERE ID_TRABAJADOR = ?";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return false;
            }

            int idx = 1;
            pstmt.setString(idx++, t.getPrimerNombre());
            pstmt.setString(idx++, t.getPrimerApellido());
            pstmt.setString(idx++, t.getCodigoPais());
            pstmt.setString(idx++, t.getCelular());
            pstmt.setString(idx++, t.getCargo());
            pstmt.setString(idx++, t.getUsuario());
            if (actualizarClave) {
                pstmt.setString(idx++, t.getClaveHash());
            }
            pstmt.setBoolean(idx++, t.isActivo());
            pstmt.setInt(idx, t.getIdTrabajador());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar trabajador: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idTrabajador) {
        if (idTrabajador <= 0) {
            return false;
        }

        String sql = "DELETE FROM TRABAJADOR WHERE ID_TRABAJADOR = ?";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return false;
            }

            pstmt.setInt(1, idTrabajador);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar trabajador: " + e.getMessage());
            return false;
        }
    }

    private Trabajador mapearTrabajador(ResultSet rs) throws SQLException {
        Trabajador trabajador = new Trabajador();
        trabajador.setIdTrabajador(rs.getInt("ID_TRABAJADOR"));
        trabajador.setPrimerNombre(rs.getString("PRIMER_NOMBRE"));
        trabajador.setPrimerApellido(rs.getString("PRIMER_APELLIDO"));
        trabajador.setCodigoPais(rs.getString("CODIGO_PAIS"));
        trabajador.setCelular(rs.getString("CELULAR"));
        trabajador.setCargo(rs.getString("CARGO"));
        trabajador.setUsuario(rs.getString("USUARIO"));
        trabajador.setClaveHash(rs.getString("CLAVE_HASH"));
        trabajador.setActivo(rs.getBoolean("ACTIVO"));
        return trabajador;
    }
}