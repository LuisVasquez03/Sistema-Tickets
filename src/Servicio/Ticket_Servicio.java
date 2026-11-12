package Servicio;

import Estructura.Lista_Ticket;
import Modelo.Ticket;
import Util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Ticket_Servicio {
    private Lista_Ticket listaTickets;

    public Ticket_Servicio() {
        this.listaTickets = new Lista_Ticket();
    }

    public boolean registrarTicket(String codigo, int idCliente, int idVehiculo, int idTrabajador, String telefonoCliente, String placaVehiculo, String servicio, String prioridad) {
        // 1. Usamos el constructor vacío para no alterar Ticket.java
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setNroTicket(codigo);
        nuevoTicket.setIdCliente(idCliente);
        nuevoTicket.setIdVehiculo(idVehiculo);
        nuevoTicket.setIdTrabajador(idTrabajador);
        nuevoTicket.setFechaIngreso(LocalDateTime.now());
        nuevoTicket.setEstado("Pendiente");

        // 2. Lo agregamos a la lista enlazada en memoria
        listaTickets.agregar(nuevoTicket);

        // 3. Guardamos en MySQL
        String sql = "INSERT INTO tickets (codigo, telefono, placa, servicio, prioridad, estado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConexionBD.conn();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            pstmt.setString(2, telefonoCliente);
            pstmt.setString(3, placaVehiculo);
            pstmt.setString(4, servicio);
            pstmt.setString(5, prioridad);
            pstmt.setString(6, "Pendiente");
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al guardar el ticket en la BD: " + e.getMessage());
            return false; 
        }
    }

    public Ticket buscarTicket(String codigo) {
        return listaTickets.buscarPorCodigo(codigo);
    }
   
    public boolean iniciarAtencion(String nroTicket) {
        Ticket ticket = buscarTicket(nroTicket);

        if (ticket == null) {
            return false;
        }

        String estadoActual = ticket.getEstado();

        if (!"Pendiente".equalsIgnoreCase(estadoActual)
                && !"REGISTRADO".equalsIgnoreCase(estadoActual)) {
            return false;
        }

        ticket.setEstado("EN_PROCESO");
        return true;
    }

    public boolean eliminarTicket(String codigo) {
        if (listaTickets.buscarPorCodigo(codigo) == null) {
            return false;
        }
        listaTickets.eliminar(codigo);
        return true;
    }

    public Ticket[] listarTodosLosTickets() {
        return listaTickets.obtenerTodos();
    }
}