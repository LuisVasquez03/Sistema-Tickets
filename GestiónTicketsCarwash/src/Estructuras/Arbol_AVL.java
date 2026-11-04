package Estructuras;

public class Arbol_AVL {
    private NodoAVL raiz;

    private int altura(NodoAVL n) {
        return (n == null) ? 0 : n.altura;
    }

    private int obtenerBalance(NodoAVL n) {
        return (n == null) ? 0 : altura(n.izquierdo) - altura(n.derecho);
    }

    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.izquierdo;
        NodoAVL T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;

        return x;
    }

    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.derecho;
        NodoAVL T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;

        return y;
    }

    public void insertar(Ticket ticket) {
        raiz = insertarRec(raiz, ticket);
    }

    private NodoAVL insertarRec(NodoAVL nodo, Ticket ticket) {
        if (nodo == null) return new NodoAVL(ticket);

        int comparacion = ticket.getCodigo().compareTo(nodo.ticket.getCodigo());

        if (comparacion < 0)
            nodo.izquierdo = insertarRec(nodo.izquierdo, ticket);
        else if (comparacion > 0)
            nodo.derecho = insertarRec(nodo.derecho, ticket);
        else
            return nodo;

        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        int balance = obtenerBalance(nodo);

        // Casos de rotación AVL
        if (balance > 1 && ticket.getCodigo().compareTo(nodo.izquierdo.ticket.getCodigo()) < 0)
            return rotacionDerecha(nodo);

        if (balance < -1 && ticket.getCodigo().compareTo(nodo.derecho.ticket.getCodigo()) > 0)
            return rotacionIzquierda(nodo);

        if (balance > 1 && ticket.getCodigo().compareTo(nodo.izquierdo.ticket.getCodigo()) > 0) {
            nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }

        if (balance < -1 && ticket.getCodigo().compareTo(nodo.derecho.ticket.getCodigo()) < 0) {
            nodo.derecho = rotacionDerecha(nodo.derecho);
            return rotacionIzquierda(nodo); // <-- CORREGIDO AQUÍ (era nodo.izquierdo)
        }

        return nodo;
    }

    public Ticket buscar(String codigo) {
        return buscarRec(raiz, codigo);
    }

    private Ticket buscarRec(NodoAVL nodo, String codigo) {
        if (nodo == null) return null;
        
        int comparacion = codigo.compareTo(nodo.ticket.getCodigo());
        if (comparacion == 0) return nodo.ticket;
        
        if (comparacion < 0)
            return buscarRec(nodo.izquierdo, codigo);
        else
            return buscarRec(nodo.derecho, codigo);
    }
}