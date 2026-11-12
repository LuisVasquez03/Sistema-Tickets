
package Estructura;

import Modelo.Ticket;

public class Nodo_Ticket {
    public Ticket ticket;
    public Nodo_Ticket siguiente;

    public Nodo_Ticket(Ticket ticket) {
        this.ticket = ticket;
        this.siguiente = null;
    }
}
    