
package Estructura;

import Modelo.Ticket;

public class Lista_Ticket {
    
    private Nodo_Ticket cabeza;
    private int tamano;

    public Lista_Ticket() {
        this.cabeza = null;
        this.tamano = 0;
    }
    
    public void agregar(Ticket ticket) {
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
    }

    public Ticket buscarPorCodigo(String codigo) {
        Nodo_Ticket actual = cabeza;
        while (actual != null) {
            if (actual.ticket.getCodigo().equalsIgnoreCase(codigo)) {
                return actual.ticket;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public void eliminar(String codigo) {
        if (cabeza == null) {
            System.out.println("La lista esta vacia.");
            return;
        }

        if (cabeza.ticket.getCodigo().equalsIgnoreCase(codigo)) {
            cabeza = cabeza.siguiente;
            System.out.println("Ticket eliminado.");
            return;
        }

        Nodo_Ticket actual = cabeza;

        while (actual.siguiente != null && !actual.siguiente.ticket.getCodigo().equalsIgnoreCase(codigo)) {
            actual = actual.siguiente;
        }

        if (actual.siguiente == null) {
            System.out.println("Ticket no encontrado.");
        } else {
            actual.siguiente = actual.siguiente.siguiente;
            System.out.println("Ticket eliminado.");
        }
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
