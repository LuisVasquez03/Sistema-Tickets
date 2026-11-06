package Estructura;

import Modelo.Ticket;

/**
 * Cola manual FIFO para gestionar tickets pendientes.
 *
 * FIFO significa First In, First Out:
 * el primer ticket que entra es el primero en ser atendido.
 *
 * @author luisv
 */
public class Cola_Prioridad {

    /**
     * Nodo interno de la cola.
     */
    private static class NodoCola {

        private Ticket ticket;
        private NodoCola siguiente;

        public NodoCola(Ticket ticket) {
            this.ticket = ticket;
            this.siguiente = null;
        }
    }

    /**
     * Primer nodo de la cola.
     */
    private NodoCola frente;

    /**
     * Último nodo de la cola.
     */
    private NodoCola finalCola;

    /**
     * Cantidad de tickets pendientes.
     */
    private int tamano;

    /**
     * Construye una cola vacía.
     */
    public Cola_Prioridad() {
        frente = null;
        finalCola = null;
        tamano = 0;
    }

    /**
     * Agrega un ticket al final de la cola.
     *
     * @param ticket ticket que ingresará a la cola
     * @return true si fue agregado, false si ya existía
     */
    public boolean encolar(Ticket ticket) {
        validarTicket(ticket);

        if (!esEstadoPendiente(ticket.getEstado())) {
            throw new IllegalArgumentException(
                    "Solo se pueden encolar tickets pendientes."
            );
        }

        if (existeCodigo(ticket.getCodigo())) {
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

    /**
     * Retira el primer ticket según FIFO.
     *
     * @return ticket retirado o null si la cola está vacía
     */
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

    /**
     * Consulta el primer ticket sin retirarlo.
     *
     * @return siguiente ticket o null
     */
    public Ticket verSiguiente() {
        if (estaVacia()) {
            return null;
        }

        return frente.ticket;
    }

    /**
     * Busca un ticket por su código sin alterar la cola.
     *
     * @param codigo código buscado
     * @return ticket encontrado o null
     */
    public Ticket buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        String codigoBuscado = codigo.trim();
        NodoCola actual = frente;

        while (actual != null) {
            String codigoActual = actual.ticket.getCodigo();

            if (codigoActual != null
                    && codigoActual.equalsIgnoreCase(codigoBuscado)) {
                return actual.ticket;
            }

            actual = actual.siguiente;
        }

        return null;
    }

    /**
     * Comprueba si un código ya está en la cola.
     *
     * @param codigo código que será comprobado
     * @return true si el código ya existe
     */
    public boolean existeCodigo(String codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    /**
     * Devuelve todos los tickets respetando el orden FIFO.
     *
     * @return arreglo con los tickets pendientes
     */
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

    /**
     * Reconstruye la cola utilizando un arreglo de tickets.
     *
     * Solo agrega tickets pendientes o registrados.
     *
     * @param tickets tickets que serán cargados
     */
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

    /**
     * Retira un ticket específico.
     *
     * Se utilizará si un ticket pendiente es cancelado desde
     * otro módulo.
     *
     * @param codigo código del ticket
     * @return true si el ticket fue retirado
     */
    public boolean retirarPorCodigo(String codigo) {
        if (codigo == null
                || codigo.isBlank()
                || estaVacia()) {
            return false;
        }

        String codigoBuscado = codigo.trim();

        if (frente.ticket.getCodigo()
                .equalsIgnoreCase(codigoBuscado)) {
            desencolar();
            return true;
        }

        NodoCola anterior = frente;
        NodoCola actual = frente.siguiente;

        while (actual != null) {
            if (actual.ticket.getCodigo()
                    .equalsIgnoreCase(codigoBuscado)) {

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

    /**
     * Indica si la cola está vacía.
     *
     * @return true si no hay tickets
     */
    public boolean estaVacia() {
        return frente == null;
    }

    /**
     * Devuelve el tamaño actual.
     *
     * @return número de tickets
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Vacía completamente la cola.
     */
    public void vaciar() {
        frente = null;
        finalCola = null;
        tamano = 0;
    }

    /**
     * Valida los datos mínimos del ticket.
     */
    private void validarTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException(
                    "El ticket no puede ser nulo."
            );
        }

        if (ticket.getCodigo() == null
                || ticket.getCodigo().isBlank()) {
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

    /**
     * Acepta el estado temporal Pendiente y el estado definitivo
     * REGISTRADO.
     */
    private boolean esEstadoPendiente(String estado) {
        if (estado == null) {
            return false;
        }

        return "Pendiente".equalsIgnoreCase(estado)
                || "REGISTRADO".equalsIgnoreCase(estado);
    }
}