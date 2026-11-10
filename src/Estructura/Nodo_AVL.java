package Estructura;

import Modelo.Ticket;

public class Nodo_AVL {
    public Ticket ticket;
   public Nodo_AVL izquierdo;
   public Nodo_AVL derecho;
   public int altura;

   public Nodo_AVL(Ticket ticket) {
      this.ticket = ticket;
      this.altura = 1;
   }
}
