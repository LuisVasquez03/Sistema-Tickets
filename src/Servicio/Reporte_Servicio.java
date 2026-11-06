/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

import Modelo.Servicio;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Reporte_Servicio {

   private static final DateTimeFormatter fmt_fecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   private static final DateTimeFormatter fmt_mes = DateTimeFormatter.ofPattern("yyyy-MM");
   private static final BigDecimal cien = new BigDecimal("100");

    public enum TipoReporte {
        ingresos_dia("Ingresos por dia", true),
        ingresos_metodo("Ingresos por dia y metodo de pago", true),
        servicios_vendidos("Servicios mas vendidos", true),
        atenciones_trabajador("Atenciones por trabajador mensual", true),
        retrasos("Retrasos del servicio", true),
        descuentos_recargos("Descuentos y recargos aplicados", true),
        por_cobrar("Tickets por cobrar", false),
        autos_en_local("Autos en el local", false),
        catalogo("Catalogo de servicios", false);
        private final String titulo;
        private final boolean usaFechas;

        TipoReporte(String titulo, boolean usaFechas) {
            this.titulo = titulo;
            this.usaFechas = usaFechas;
       }

        public String getTitulo() { return titulo; }
        public boolean usaFechas() { return usaFechas; }
        @Override
        public String toString() { return titulo; }  }

public static class Resultado {
        private final String titulo;
        private final String[] columnas;
        private final List<Object[]> filas;
        private final Map<String, String> metricas;

        Resultado(String titulo, String[] columnas, List<Object[]> filas, Map<String, String> metricas) {
            this.titulo = titulo;
            this.columnas = columnas;
            this.filas = filas;
            this.metricas = metricas;
        }

        public String getTitulo() { return titulo; }
        public String[] getColumnas() { return columnas; }
        public List<Object[]> getFilas() { return filas; }
        public Map<String, String> getMetricas() { return metricas; }
    }

/*    private final Connection con;

    public Reporte_Servicio(Connection con) {
        if (con == null) {
            throw new IllegalArgumentException("cuando sepa la variable de conexion :v");
        }
        this.con = con;
Falta aca las operaciones internas de cada tipo        

*/
    }












