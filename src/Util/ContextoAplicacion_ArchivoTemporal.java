package Util;

import Estructura.Cola_Prioridad;
import Estructura.Pila_historial;
import Servicio.Ticket_Servicio;

/**
 * Mantiene las estructuras compartidas mientras
 * el programa se encuentra abierto.
 */
public final class ContextoAplicacion_ArchivoTemporal {

    private static final Ticket_Servicio TICKET_SERVICIO
            = new Ticket_Servicio();

    private static final Cola_Prioridad COLA_ATENCION
            = new Cola_Prioridad();

    private static final Pila_historial PILA_HISTORIAL
            = new Pila_historial();

    private ContextoAplicacion_ArchivoTemporal() {
        // Impide crear instancias de esta clase.
    }

    public static Ticket_Servicio getTicketServicio() {
        return TICKET_SERVICIO;
    }

    public static Cola_Prioridad getColaAtencion() {
        return COLA_ATENCION;
    }

    public static Pila_historial getPilaHistorial() {
        return PILA_HISTORIAL;
    }
}