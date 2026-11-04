package Estructuras;

public class NodoAVL {
    public Ticket ticket;
    public NodoAVL izquierdo, derecho;
    public int altura;

    public NodoAVL(Ticket ticket) {
        this.ticket = ticket;
        this.altura = 1;
    }
}