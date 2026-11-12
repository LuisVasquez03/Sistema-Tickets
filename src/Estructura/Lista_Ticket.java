package Estructura;

import Modelo.Ticket;

public class Lista_Ticket {
    
    private Nodo_Ticket cabeza;
    private int tamano;

    public Lista_Ticket() {
        this.cabeza = null;
        this.tamano = 0;
    }
    
    public boolean agregar(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException(
                    "El ticket no puede ser nulo."
            );
        }

        if (ticket.getNroTicket() == null
                || ticket.getNroTicket().isBlank()) {

            throw new IllegalArgumentException(
                    "El ticket debe tener un código."
            );
        }

        if (buscarPorCodigo(ticket.getNroTicket()) != null) {
            return false; // Ya existe
        }

        Nodo_Ticket nuevo = new Nodo_Ticket(ticket);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo_Ticket actual = cabeza;

            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }

            actual.siguiente = nuevo;
        }

        tamano++;
        return true;
    }

    public Ticket buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        
        Nodo_Ticket actual = cabeza;
        while (actual != null) {
            if (actual.ticket.getNroTicket().equalsIgnoreCase(codigo.trim())) {
                return actual.ticket;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean eliminar(String codigo) {
        if (codigo == null
                || codigo.isBlank()
                || cabeza == null) {

            return false;
        }

        String codigoBuscado = codigo.trim();

        if (cabeza.ticket.getNroTicket()
                .equalsIgnoreCase(codigoBuscado)) {

            cabeza = cabeza.siguiente;
            tamano--;
            return true;
        }

        Nodo_Ticket actual = cabeza;

        while (actual.siguiente != null
                && !actual.siguiente.ticket.getNroTicket()
                        .equalsIgnoreCase(codigoBuscado)) {

            actual = actual.siguiente;
        }

        if (actual.siguiente == null) {
            return false;
        }

        actual.siguiente = actual.siguiente.siguiente;
        tamano--;

        return true;
    }

    public Ticket[] obtenerTodos() {
        Ticket[] arreglo = new Ticket[tamano];
        Nodo_Ticket actual = cabeza;
        int i = 0;
        while (actual != null) {
            arreglo[i++] = actual.ticket;
            actual = actual.siguiente;
        }
        return arreglo;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamano() {
        return tamano;
    }
}