package Estructura;

import Modelo.Ticket;

public class Cola_Prioridad {

    private static class NodoCola {

        private Ticket ticket;
        private NodoCola siguiente;

        public NodoCola(Ticket ticket) {
            this.ticket = ticket;
            this.siguiente = null;
        }
    }

    private NodoCola frente;
    private NodoCola finalCola;
    private int tamano;

    public Cola_Prioridad() {
        frente = null;
        finalCola = null;
        tamano = 0;
    }

    public boolean encolar(Ticket ticket) {
        validarTicket(ticket);

        if (!esEstadoPendiente(ticket.getEstado())) {
            throw new IllegalArgumentException(
                    "Solo se pueden encolar tickets pendientes."
            );
        }

        if (existeCodigo(ticket.getNroTicket())) {
            return false;
        }

        NodoCola nuevoNodo = new NodoCola(ticket);

        if (estaVacia()) {
            frente = nuevoNodo;
            finalCola = nuevoNodo;
        } else {
            finalCola.siguiente = nuevoNodo;
            finalCola = nuevoNodo;
        }

        tamano++;
        return true;
    }

    public Ticket desencolar() {
        if (estaVacia()) {
            return null;
        }

        Ticket ticketRetirado = frente.ticket;
        frente = frente.siguiente;
        tamano--;

        if (frente == null) {
            finalCola = null;
        }

        return ticketRetirado;
    }

    public Ticket verSiguiente() {
        if (estaVacia()) {
            return null;
        }

        return frente.ticket;
    }

    public Ticket buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        String codigoBuscado = codigo.trim();
        NodoCola actual = frente;

        while (actual != null) {
            if (actual.ticket != null && actual.ticket.getNroTicket() != null) {
                if (actual.ticket.getNroTicket().trim().equalsIgnoreCase(codigoBuscado)) {
                    return actual.ticket;
                }
            }

            actual = actual.siguiente;
        }

        return null;
    }

    public boolean existeCodigo(String codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public Ticket[] obtenerTodos() {
        Ticket[] tickets = new Ticket[tamano];

        NodoCola actual = frente;
        int posicion = 0;

        while (actual != null) {
            tickets[posicion] = actual.ticket;
            posicion++;
            actual = actual.siguiente;
        }

        return tickets;
    }

    public void cargarTickets(Ticket[] tickets) {
        vaciar();

        if (tickets == null) {
            return;
        }

        for (Ticket ticket : tickets) {
            if (ticket != null
                    && esEstadoPendiente(ticket.getEstado())) {
                encolar(ticket);
            }
        }
    }

    public boolean retirarPorCodigo(String codigo) {
        if (codigo == null
                || codigo.isBlank()
                || estaVacia()) {
            return false;
        }

        String codigoBuscado = codigo.trim();

        if (frente.ticket != null
                && frente.ticket.getNroTicket() != null
                && frente.ticket.getNroTicket().trim().equalsIgnoreCase(codigoBuscado)) {
            desencolar();
            return true;
        }

        NodoCola anterior = frente;
        NodoCola actual = frente.siguiente;

        while (actual != null) {
            if (actual.ticket != null
                    && actual.ticket.getNroTicket() != null
                    && actual.ticket.getNroTicket().trim().equalsIgnoreCase(codigoBuscado)) {

                anterior.siguiente = actual.siguiente;

                if (actual == finalCola) {
                    finalCola = anterior;
                }

                tamano--;
                return true;
            }

            anterior = actual;
            actual = actual.siguiente;
        }

        return false;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamano() {
        return tamano;
    }

    public void vaciar() {
        frente = null;
        finalCola = null;
        tamano = 0;
    }

    private void validarTicket(Ticket ticket) {
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

        if (ticket.getEstado() == null
                || ticket.getEstado().isBlank()) {
            throw new IllegalArgumentException(
                    "El ticket debe tener un estado."
            );
        }
    }

    private boolean esEstadoPendiente(String estado) {
        if (estado == null) {
            return false;
        }

        return "Pendiente".equalsIgnoreCase(estado.trim())
                || "REGISTRADO".equalsIgnoreCase(estado.trim());
    }
}