package Estructura;
//Favor de no borrar oh modificar alguna parte, consultar con Piero o Fernando Primero//
/**
 * Pila manual para almacenar los eventos recientes del sistema.
 */
public class Pila_historial {

    /**
     * Nodo interno de la pila.
     */
    private static class NodoHistorial {

        private String evento;
        private NodoHistorial siguiente;

        public NodoHistorial(String evento) {
            this.evento = evento;
            this.siguiente = null;
        }
    }

    private NodoHistorial cima;
    private int tamano;

    /**
     * Crea una pila vacía.
     */
    public Pila_historial() {
        cima = null;
        tamano = 0;
    }

    /**
     * Agrega un evento en la parte superior de la pila.
     */
    public void apilar(String evento) {
        if (evento == null || evento.isBlank()) {
            throw new IllegalArgumentException(
                    "El evento no puede estar vacío."
            );
        }

        NodoHistorial nuevo = new NodoHistorial(evento);

        nuevo.siguiente = cima;
        cima = nuevo;
        tamano++;
    }

    /**
     * Consulta el evento más reciente sin eliminarlo.
     */
    public String verUltimo() {
        if (estaVacia()) {
            return null;
        }

        return cima.evento;
    }

    /**
     * Retira y devuelve el evento más reciente.
     */
    public String desapilar() {
        if (estaVacia()) {
            return null;
        }

        String eventoRetirado = cima.evento;
        cima = cima.siguiente;
        tamano--;

        return eventoRetirado;
    }

    /**
     * Verifica si la pila está vacía.
     */
    public boolean estaVacia() {
        return cima == null;
    }

    /**
     * Devuelve la cantidad de eventos registrados.
     */
    public int getTamano() {
        return tamano;
    }
}
