package Servicio;

import Estructura.Lista_Ticket;
import Modelo.Ticket;
import Util.ConexionBD;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ticket_Servicio {

    private final Lista_Ticket listaTickets;

    public Ticket_Servicio() {
        this.listaTickets = new Lista_Ticket();
    }

    public boolean registrarTicket(
            String nroTicket,
            int idCliente,
            int idVehiculo,
            int idTrabajador,
            LocalDateTime fechaEstimada,
            BigDecimal subtotal,
            BigDecimal total) {

        if (nroTicket == null || nroTicket.isBlank()
                || idCliente <= 0
                || idVehiculo <= 0
                || idTrabajador <= 0
                || fechaEstimada == null) {
            return false;
        }

        String codigoLimpio = nroTicket.trim();
        BigDecimal sub = subtotal != null ? subtotal : BigDecimal.ZERO;
        BigDecimal tot = total != null ? total : sub;
        LocalDateTime fechaIngreso = LocalDateTime.now();
        String estadoInicial = "REGISTRADO";

        String sql = "INSERT INTO ORDEN_SERVICIO ("
                   + "NRO_TICKET, ID_CLIENTE, ID_VEHICULO, ID_TRABAJADOR, "
                   + "FECHA_INGRESO, FECHA_ESTIMADA, ESTADO, SUBTOTAL, TOTAL) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null
                     ? con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                     : null) {

            if (pstmt == null) {
                return false;
            }

            pstmt.setString(1, codigoLimpio);
            pstmt.setInt(2, idCliente);
            pstmt.setInt(3, idVehiculo);
            pstmt.setInt(4, idTrabajador);
            pstmt.setTimestamp(5, Timestamp.valueOf(fechaIngreso));
            pstmt.setTimestamp(6, Timestamp.valueOf(fechaEstimada));
            pstmt.setString(7, estadoInicial);
            pstmt.setBigDecimal(8, sub);
            pstmt.setBigDecimal(9, tot);

            int filas = pstmt.executeUpdate();
            if (filas <= 0) {
                return false;
            }

            Ticket nuevoTicket = new Ticket();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    nuevoTicket.setIdOrden(keys.getInt(1));
                }
            }

            nuevoTicket.setNroTicket(codigoLimpio);
            nuevoTicket.setIdCliente(idCliente);
            nuevoTicket.setIdVehiculo(idVehiculo);
            nuevoTicket.setIdTrabajador(idTrabajador);
            nuevoTicket.setFechaIngreso(fechaIngreso);
            nuevoTicket.setFechaEstimada(fechaEstimada);
            nuevoTicket.setEstado(estadoInicial);
            nuevoTicket.setSubtotal(sub);
            nuevoTicket.setTotal(tot);

            listaTickets.agregar(nuevoTicket);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar ticket en ORDEN_SERVICIO: " + e.getMessage());
            return false;
        }
    }

    public Ticket buscarTicket(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        return listaTickets.buscarPorCodigo(codigo.trim());
    }

    public boolean iniciarAtencion(String nroTicket) {
        if (nroTicket == null || nroTicket.isBlank()) {
            return false;
        }

        String codigo = nroTicket.trim();
        Ticket ticket = listaTickets.buscarPorCodigo(codigo);

        String sql = "UPDATE ORDEN_SERVICIO SET ESTADO = 'EN_PROCESO' "
                   + "WHERE NRO_TICKET = ? AND ESTADO IN ('REGISTRADO', 'Pendiente')";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return false;
            }

            pstmt.setString(1, codigo);
            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                if (ticket != null) {
                    ticket.setEstado("EN_PROCESO");
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al iniciar atención del ticket: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarTicket(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return false;
        }

        String limpio = codigo.trim();
        String sql = "DELETE FROM ORDEN_SERVICIO WHERE NRO_TICKET = ?";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return false;
            }

            pstmt.setString(1, limpio);
            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                listaTickets.eliminar(limpio);
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al eliminar ticket en ORDEN_SERVICIO: " + e.getMessage());
            return false;
        }
    }

    public Ticket[] listarTodosLosTickets() {
        return listaTickets.obtenerTodos();
    }

    public List<Ticket> listarTodosDesdeBD() {
        List<Ticket> lista = new ArrayList<>();

        String sql = "SELECT ID_ORDEN, NRO_TICKET, ID_CLIENTE, ID_VEHICULO, ID_TRABAJADOR, "
                   + "FECHA_INGRESO, FECHA_ESTIMADA, FECHA_TERMINO, FECHA_SALIDA, ESTADO, "
                   + "MOTIVO_RETRASO, NOTA_CORRECCION, MOTIVO_CANCELACION, "
                   + "SUBTOTAL, DESCUENTO_PORCENTAJE, DESCUENTO_MONTO, MOTIVO_DESCUENTO, "
                   + "RECARGO_TARJETA, MONTO_IGV, TOTAL "
                   + "FROM ORDEN_SERVICIO ORDER BY ID_ORDEN ASC";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return lista;
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ticket t = new Ticket();
                    t.setIdOrden(rs.getInt("ID_ORDEN"));
                    t.setNroTicket(rs.getString("NRO_TICKET"));
                    t.setIdCliente(rs.getInt("ID_CLIENTE"));
                    t.setIdVehiculo(rs.getInt("ID_VEHICULO"));
                    t.setIdTrabajador(rs.getInt("ID_TRABAJADOR"));

                    Timestamp fIng = rs.getTimestamp("FECHA_INGRESO");
                    Timestamp fEst = rs.getTimestamp("FECHA_ESTIMADA");
                    Timestamp fTer = rs.getTimestamp("FECHA_TERMINO");
                    Timestamp fSal = rs.getTimestamp("FECHA_SALIDA");
                    if (fIng != null) t.setFechaIngreso(fIng.toLocalDateTime());
                    if (fEst != null) t.setFechaEstimada(fEst.toLocalDateTime());
                    if (fTer != null) t.setFechaTermino(fTer.toLocalDateTime());
                    if (fSal != null) t.setFechaSalida(fSal.toLocalDateTime());

                    t.setEstado(rs.getString("ESTADO"));
                    t.setMotivoRetraso(rs.getString("MOTIVO_RETRASO"));
                    t.setNotaCorreccion(rs.getString("NOTA_CORRECCION"));
                    t.setMotivoCancelacion(rs.getString("MOTIVO_CANCELACION"));
                    t.setSubtotal(rs.getBigDecimal("SUBTOTAL"));
                    t.setDescuentoPorcentaje(rs.getBigDecimal("DESCUENTO_PORCENTAJE"));
                    t.setDescuentoMonto(rs.getBigDecimal("DESCUENTO_MONTO"));
                    t.setMotivoDescuento(rs.getString("MOTIVO_DESCUENTO"));
                    t.setRecargoTarjeta(rs.getBigDecimal("RECARGO_TARJETA"));
                    t.setMontoIgv(rs.getBigDecimal("MONTO_IGV"));
                    t.setTotal(rs.getBigDecimal("TOTAL"));

                    lista.add(t);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar todos los tickets desde BD: " + e.getMessage());
        }

        return lista;
    }

    public String generarSiguienteNroTicket() {
        int maxNumero = 0;
        for (Ticket t : listarTodosDesdeBD()) {
            if (t.getNroTicket() != null) {
                String digitos = t.getNroTicket().replaceAll("\\D+", "");
                if (!digitos.isEmpty()) {
                    try {
                        int num = Integer.parseInt(digitos);
                        if (num > maxNumero) {
                            maxNumero = num;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        return String.format("TCK - %03d", maxNumero + 1);
    }

    public List<Ticket> obtenerTicketsPorEstadoDesdeBD(String estado) {
        List<Ticket> lista = new ArrayList<>();
        if (estado == null || estado.isBlank()) {
            return lista;
        }

        String sql = "SELECT ID_ORDEN, NRO_TICKET, ID_CLIENTE, ID_VEHICULO, ID_TRABAJADOR, "
                   + "FECHA_INGRESO, FECHA_ESTIMADA, FECHA_TERMINO, FECHA_SALIDA, ESTADO, "
                   + "SUBTOTAL, TOTAL FROM ORDEN_SERVICIO WHERE ESTADO = ? ORDER BY FECHA_INGRESO ASC";

        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con != null ? con.prepareStatement(sql) : null) {

            if (pstmt == null) {
                return lista;
            }

            pstmt.setString(1, estado.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ticket t = new Ticket();
                    t.setIdOrden(rs.getInt("ID_ORDEN"));
                    t.setNroTicket(rs.getString("NRO_TICKET"));
                    t.setIdCliente(rs.getInt("ID_CLIENTE"));
                    t.setIdVehiculo(rs.getInt("ID_VEHICULO"));
                    t.setIdTrabajador(rs.getInt("ID_TRABAJADOR"));
                    Timestamp fIng = rs.getTimestamp("FECHA_INGRESO");
                    Timestamp fEst = rs.getTimestamp("FECHA_ESTIMADA");
                    Timestamp fTer = rs.getTimestamp("FECHA_TERMINO");
                    Timestamp fSal = rs.getTimestamp("FECHA_SALIDA");
                    if (fIng != null) t.setFechaIngreso(fIng.toLocalDateTime());
                    if (fEst != null) t.setFechaEstimada(fEst.toLocalDateTime());
                    if (fTer != null) t.setFechaTermino(fTer.toLocalDateTime());
                    if (fSal != null) t.setFechaSalida(fSal.toLocalDateTime());
                    t.setEstado(rs.getString("ESTADO"));
                    t.setSubtotal(rs.getBigDecimal("SUBTOTAL"));
                    t.setTotal(rs.getBigDecimal("TOTAL"));
                    lista.add(t);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar tickets por estado: " + e.getMessage());
        }

        return lista;
    }
}