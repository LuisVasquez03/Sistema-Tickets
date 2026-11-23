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

    private String claveTicket(Ticket ticket) {
        if (ticket == null || ticket.getNroTicket() == null || ticket.getNroTicket().isBlank()) {
            throw new IllegalArgumentException("El ticket y su número de ticket son obligatorios.");
        }
        return ticket.getNroTicket().trim();
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
        String clave = claveTicket(ticket);
        raiz = insertarRec(raiz, ticket, clave);
    }

    private Nodo_AVL insertarRec(Nodo_AVL nodo, Ticket ticket, String claveNueva) {
        if (nodo == null) {
            return new Nodo_AVL(ticket);
        }

        String claveActual = claveTicket(nodo.ticket);
        int comparacion = claveNueva.compareTo(claveActual);

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

        if (balance > 1 && claveNueva.compareTo(claveTicket(nodo.izquierdo.ticket)) < 0) {
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && claveNueva.compareTo(claveTicket(nodo.derecho.ticket)) > 0) {
            return rotacionIzquierda(nodo);
        }
        if (balance > 1 && claveNueva.compareTo(claveTicket(nodo.izquierdo.ticket)) > 0) {
            nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && claveNueva.compareTo(claveTicket(nodo.derecho.ticket)) < 0) {
            nodo.derecho = rotacionDerecha(nodo);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    public Ticket buscar(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        return buscarRec(raiz, codigo.trim());
    }

    private Ticket buscarRec(Nodo_AVL nodo, String codigo) {
        if (nodo == null || nodo.ticket == null || nodo.ticket.getNroTicket() == null) {
            return null;
        }

        int comparacion = codigo.compareTo(nodo.ticket.getNroTicket().trim());
        if (comparacion == 0) {
            return nodo.ticket;
        }
        return comparacion < 0
                ? buscarRec(nodo.izquierdo, codigo)
                : buscarRec(nodo.derecho, codigo);
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

    public void limpiar() {
        raiz = null;
    }
}