package Estructura;

import Modelo.Ticket;
import java.util.ArrayList;
import java.util.List;

public class Arbol_AVL {

    private Nodo_AVL raiz;

    public Arbol_AVL() {
        this.raiz = null;
    }

    private int altura(Nodo_AVL n) {
        return n == null ? 0 : n.altura;
    }

    private int obtenerBalance(Nodo_AVL n) {
        return n == null ? 0 : altura(n.izquierdo) - altura(n.derecho);
    }

    private int extraerNumero(String nroTicket) {
        if (nroTicket == null || nroTicket.isBlank()) {
            return 0;
        }
        String digitos = nroTicket.replaceAll("\\D+", "");
        if (digitos.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(digitos);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private int claveTicket(Ticket ticket) {
        if (ticket == null || ticket.getNroTicket() == null || ticket.getNroTicket().isBlank()) {
            throw new IllegalArgumentException("El ticket y su número de ticket son obligatorios.");
        }
        return extraerNumero(ticket.getNroTicket().trim());
    }

    private Nodo_AVL rotacionDerecha(Nodo_AVL y) {
        Nodo_AVL x = y.izquierdo;
        Nodo_AVL t2 = x.derecho;

        x.derecho = y;
        y.izquierdo = t2;

        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;

        return x;
    }

    private Nodo_AVL rotacionIzquierda(Nodo_AVL x) {
        Nodo_AVL y = x.derecho;
        Nodo_AVL t2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = t2;

        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;

        return y;
    }

    public void insertar(Ticket ticket) {
        int clave = claveTicket(ticket);
        raiz = insertarRec(raiz, ticket, clave);
    }

    private Nodo_AVL insertarRec(Nodo_AVL nodo, Ticket ticket, int claveNueva) {
        if (nodo == null) {
            return new Nodo_AVL(ticket);
        }

        int claveActual = claveTicket(nodo.ticket);
        int comparacion = Integer.compare(claveNueva, claveActual);

        if (comparacion < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, ticket, claveNueva);
        } else if (comparacion > 0) {
            nodo.derecho = insertarRec(nodo.derecho, ticket, claveNueva);
        } else {
            nodo.ticket = ticket;
            return nodo;
        }

        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        int balance = obtenerBalance(nodo);

        if (balance > 1 && claveNueva < claveTicket(nodo.izquierdo.ticket)) {
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && claveNueva > claveTicket(nodo.derecho.ticket)) {
            return rotacionIzquierda(nodo);
        }
        if (balance > 1 && claveNueva > claveTicket(nodo.izquierdo.ticket)) {
            nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && claveNueva < claveTicket(nodo.derecho.ticket)) {
            nodo.derecho = rotacionDerecha(nodo);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    public Ticket buscar(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        return buscarRec(raiz, extraerNumero(codigo.trim()));
    }

    private Ticket buscarRec(Nodo_AVL nodo, int numeroBuscado) {
        if (nodo == null || nodo.ticket == null || nodo.ticket.getNroTicket() == null) {
            return null;
        }

        int numeroActual = claveTicket(nodo.ticket);
        int comparacion = Integer.compare(numeroBuscado, numeroActual);
        if (comparacion == 0) {
            return nodo.ticket;
        }
        return comparacion < 0
                ? buscarRec(nodo.izquierdo, numeroBuscado)
                : buscarRec(nodo.derecho, numeroBuscado);
    }

    public Ticket[] obtenerTodosEnOrden() {
        List<Ticket> lista = new ArrayList<>();
        inOrden(raiz, lista);
        return lista.toArray(new Ticket[0]);
    }

    private void inOrden(Nodo_AVL nodo, List<Ticket> lista) {
        if (nodo == null) {
            return;
        }
        inOrden(nodo.izquierdo, lista);
        if (nodo.ticket != null) {
            lista.add(nodo.ticket);
        }
        inOrden(nodo.derecho, lista);
    }

    public Ticket[] obtenerPreOrden() {
        List<Ticket> lista = new ArrayList<>();
        preOrden(raiz, lista);
        return lista.toArray(new Ticket[0]);
    }

    private void preOrden(Nodo_AVL nodo, List<Ticket> lista) {
        if (nodo == null) {
            return;
        }
        if (nodo.ticket != null) {
            lista.add(nodo.ticket);
        }
        preOrden(nodo.izquierdo, lista);
        preOrden(nodo.derecho, lista);
    }

    public Ticket[] obtenerPostOrden() {
        List<Ticket> lista = new ArrayList<>();
        postOrden(raiz, lista);
        return lista.toArray(new Ticket[0]);
    }

    private void postOrden(Nodo_AVL nodo, List<Ticket> lista) {
        if (nodo == null) {
            return;
        }
        postOrden(nodo.izquierdo, lista);
        postOrden(nodo.derecho, lista);
        if (nodo.ticket != null) {
            lista.add(nodo.ticket);
        }
    }

    public void limpiar() {
        raiz = null;
    }
}