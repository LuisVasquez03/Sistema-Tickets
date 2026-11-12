package Estructura;

import Modelo.Ticket;

public class Arbol_AVL {
    private Nodo_AVL raiz;

   public Arbol_AVL() {
   }

   private int altura(Nodo_AVL n) {
      return n == null ? 0 : n.altura;
   }

   private int obtenerBalance(Nodo_AVL n) {
      return n == null ? 0 : this.altura(n.izquierdo) - this.altura(n.derecho);
   }

   private Nodo_AVL rotacionDerecha(Nodo_AVL y) {
      Nodo_AVL x = y.izquierdo;
      Nodo_AVL T2 = x.derecho;
      x.derecho = y;
      y.izquierdo = T2;
      y.altura = Math.max(this.altura(y.izquierdo), this.altura(y.derecho)) + 1;
      x.altura = Math.max(this.altura(x.izquierdo), this.altura(x.derecho)) + 1;
      return x;
   }

   private Nodo_AVL rotacionIzquierda(Nodo_AVL x) {
      Nodo_AVL y = x.derecho;
      Nodo_AVL T2 = y.izquierdo;
      y.izquierdo = x;
      x.derecho = T2;
      x.altura = Math.max(this.altura(x.izquierdo), this.altura(x.derecho)) + 1;
      y.altura = Math.max(this.altura(y.izquierdo), this.altura(y.derecho)) + 1;
      return y;
   }

   public void insertar(Ticket ticket) {
      this.raiz = this.insertarRec(this.raiz, ticket);
   }

   private Nodo_AVL insertarRec(Nodo_AVL nodo, Ticket ticket) {
      if (nodo == null) {
         return new Nodo_AVL(ticket);
      } else {
         // CORREGIDO: Usando getnroTicket() en lugar de getCodigo()
         int comparacion = ticket.getNroTicket().compareTo(nodo.ticket.getNroTicket());
         if (comparacion < 0) {
            nodo.izquierdo = this.insertarRec(nodo.izquierdo, ticket);
         } else {
            if (comparacion <= 0) {
               return nodo;
            }

            nodo.derecho = this.insertarRec(nodo.derecho, ticket);
         }

         nodo.altura = 1 + Math.max(this.altura(nodo.izquierdo), this.altura(nodo.derecho));
         int balance = this.obtenerBalance(nodo);
         
         if (balance > 1 && ticket.getNroTicket().compareTo(nodo.izquierdo.ticket.getNroTicket()) < 0) {
            return this.rotacionDerecha(nodo);
         } else if (balance < -1 && ticket.getNroTicket().compareTo(nodo.derecho.ticket.getNroTicket()) > 0) {
            return this.rotacionIzquierda(nodo);
         } else if (balance > 1 && ticket.getNroTicket().compareTo(nodo.izquierdo.ticket.getNroTicket()) > 0) {
            nodo.izquierdo = this.rotacionIzquierda(nodo.izquierdo);
            return this.rotacionDerecha(nodo);
         } else if (balance < -1 && ticket.getNroTicket().compareTo(nodo.derecho.ticket.getNroTicket()) < 0) {
            nodo.derecho = this.rotacionDerecha(nodo.derecho);
            return this.rotacionIzquierda(nodo);
         } else {
            return nodo;
         }
      }
   }

   public Ticket buscar(String codigo) {
      return this.buscarRec(this.raiz, codigo);
   }

   private Ticket buscarRec(Nodo_AVL nodo, String codigo) {
      if (nodo == null) {
         return null;
      } else {
         // CORREGIDO: Usando getNroTicket() en lugar de getCodigo()
         int comparacion = codigo.compareTo(nodo.ticket.getNroTicket());
         if (comparacion == 0) {
            return nodo.ticket;
         } else {
            return comparacion < 0 ? this.buscarRec(nodo.izquierdo, codigo) : this.buscarRec(nodo.derecho, codigo);
         }
      }
   }
}