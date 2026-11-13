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

    private static final DateTimeFormatter FMT_fecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_MES = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final BigDecimal CIEN = new BigDecimal("100");

    public enum TipoReporte {
        ingresos_dia("Ingresos por dia", true),
        ingresos_metodo("Ingresos por dia y metodo de pago", true),
        servicios_vendidos("Servicios mas vendidos", true),
        atenciones_trabajador("Atenciones por trabajador", true),
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
        public String toString() { return titulo; }
    }

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
        public Map<String, String> getMetricas() { return metricas; }}

    private final Connection conn;
    public Reporte_Servicio(Connection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("Necesita conectar a la BD");}
        this.conn = conn;}

    public Resultado generar(TipoReporte tipo, LocalDate desde, LocalDate hasta) throws SQLException {
        if (tipo == null) {
            throw new IllegalArgumentException("Seleccione un tipo");}
        if (tipo.usaFechas() && desde != null && hasta != null && desde.isAfter(hasta)) { throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la final");
 }
        switch (tipo) {
            case ingresos_dia:return ingresosPorDia(desde, hasta);
            case ingresos_metodo:return ingresosPorMetodo(desde, hasta);
            case servicios_vendidos:return serviciosMasVendidos(desde, hasta);
            case atenciones_trabajador:return atencionesPorTrabajador(desde, hasta);
            case retrasos:return retrasos(desde, hasta);
            case descuentos_recargos:return descuentosYRecargos(desde, hasta);
            case por_cobrar:return ticketsPorCobrar();
            case autos_en_local:return autosEnLocal();
            case catalogo:return catalogo();
            default: throw new IllegalArgumentException("Reporte no soportado: " + tipo);
        }}

    private Resultado ingresosPorDia(LocalDate desde, LocalDate hasta) throws SQLException {
        List<Object> p = new ArrayList<>(); String sql = "select fecha, cantidad_pagos, total_cobrado from v_rep_ingresos_dia" + filtroFecha
        ("fecha", desde, hasta, p, " where ")+ " order by fecha desc";
        List<Object[]> filas = consultar(sql, p);
        Map<String, String> m = new LinkedHashMap<>();
        if (filas.isEmpty()) return sinDatos("Ingresos por dia", cols("Fecha", "Pagos", "Total cobrado (S/)"), m);

        long pagos = 0;
        BigDecimal total = BigDecimal.ZERO;
        Object[] mejor = null;
        for (Object[] f : filas) {
            pagos += lng(f[1]);
            BigDecimal t = bd(f[2]);
            total = total.add(t);
            if (mejor == null || t.compareTo(bd(mejor[2])) > 0) mejor = f;}
        
        
        
        m.put("Dias con cobros", String.valueOf(filas.size()));
        m.put("Pagos registrados", String.valueOf(pagos));
        m.put("Total cobrado", soles(total));
        
        m.put("Promedio por ticket", soles(dividir(total, pagos)));
        m.put("Promedio por dia", soles(dividir(total, filas.size())));
        m.put("Mejor dia", ((LocalDate) mejor[0]).format(FMT_fecha) + " (" + soles(bd(mejor[2])) + ")");
        return new Resultado("Ingresos por dia", cols("Fecha", "Pagos", "Total cobrado (S/)"), filas, m);
    }
    private Resultado ingresosPorMetodo(LocalDate desde, LocalDate hasta) throws SQLException {
        List<Object> p = new ArrayList<>();
        String sql = "select fecha, metodo_pago, detalle_metodo, cantidad_pagos, total_cobrado"+" from v_rep_ingresos_dia_metodo"+filtroFecha("fecha", desde, hasta, p, " where ")
        + " order by fecha desc, metodo_pago";
        List<Object[]> filas = consultar(sql, p); String[] columnas = cols("Fecha", "Metodo", "Detalle", "Pagos", "Total cobrado (S/)");

        Map<String, String> m = new LinkedHashMap<>();
        if (filas.isEmpty()) return sinDatos("Ingresos por dia y metodo de pago", columnas, m);

        Map<String, BigDecimal> porMetodo = new LinkedHashMap<>(); BigDecimal total = BigDecimal.ZERO; for (Object[] f : filas) {
            BigDecimal t = bd(f[4]);
            total = total.add(t);
            porMetodo.merge(String.valueOf(f[1]), t, BigDecimal::add);
        }
        m.put("Total cobrado", soles(total));
        for (Map.Entry<String, BigDecimal> e : porMetodo.entrySet()) {
            m.put(e.getKey(), soles(e.getValue()) + "  (" + porcentaje(e.getValue(), total) + ")");
        }
        return new Resultado("Ingresos por dia y metodo de pago", columnas, filas, m);
    }

 
    private Resultado serviciosMasVendidos(LocalDate desde, LocalDate hasta) throws SQLException {
        List<Object> p = new ArrayList<>();
        String sql = "select S.NOMBRE, COUNT(*) as VECES_VENDIDO, SUM(D.PRECIO_APLICADO + D.RECARGO) as INGRESO_SERVICIO"
           + " from detalle_orden D"
                + " join ORDEN_SERVICIO O ON O.ID_ORDEN = D.ID_ORDEN"
                + " join SERVICIO S ON S.id_servicio = D.id_servicio"
               + " join pago P ON P.ID_ORDEN = O.ID_ORDEN"
                + " where O.ESTADO = 'COBRADO'"
             + filtroFecha("P.fecha_pago", desde, hasta, p, " AND ")
           + " group by S.id_servicio, S.NOMBRE"
               + " order by VECES_VENDIDO desc, S.NOMBRE";
        List<Object[]> base = consultar(sql, p);
        String[] columnas = cols("Servicio", "Veces vendido", "Ingreso (S/)", "% del ingreso");

        Map<String, String> m = new LinkedHashMap<>();
        if (base.isEmpty()) return sinDatos("Servicios mas vendidos", columnas, m);

        BigDecimal total = BigDecimal.ZERO;
        long unidades = 0;
        for (Object[] f : base) {
            total = total.add(bd(f[2]));
            unidades += lng(f[1]);
        }
        List<Object[]> filas = new ArrayList<>();
        Object[] mayorIngreso = null;
        for (Object[] f : base) { BigDecimal ingreso = bd(f[2]); BigDecimal pct = total.signum() == 0 ? BigDecimal.ZERO
                    : ingreso.multiply(CIEN).divide(total, 2, RoundingMode.HALF_UP);
            Object[] fila = { f[0], lng(f[1]), ingreso.setScale(2, RoundingMode.HALF_UP), pct };
            filas.add(fila);
            if (mayorIngreso == null || ingreso.compareTo(bd(mayorIngreso[2])) > 0) mayorIngreso = fila;
        }
        Object[] masVendido = filas.get(0); 
        m.put("Servicios vendidos", String.valueOf(unidades));
        m.put("Ingreso por servicios", soles(total) + "  (sin descuento ni IGV)");
        m.put("Mas vendido", masVendido[0] + " (" + masVendido[1] + " veces)");
        m.put("Mayor ingreso", mayorIngreso[0] + " (" + soles(bd(mayorIngreso[2])) + ")");
        return new Resultado("Servicios mas vendidos", columnas, filas, m);
    }


    private Resultado atencionesPorTrabajador(LocalDate desde, LocalDate hasta) throws SQLException {
        List<Object> p = new ArrayList<>();
        List<String> cond = new ArrayList<>();
        if (desde != null) { cond.add("ANIO_MES >= ?"); p.add(desde.format(FMT_MES)); }
        if (hasta != null) { cond.add("ANIO_MES <= ?"); p.add(hasta.format(FMT_MES)); }
        String sql = "select ANIO_MES, TRABAJADOR, TICKETS_ATENDIDOS, TICKETS_COBRADOS, TICKETS_CANCELADOS, total_cobrado"
                + " from V_REP_ATENCIONES_TRABAJADOR"
                + (cond.isEmpty() ? "" : " where " + String.join(" AND ", cond))
                + " order by ANIO_MES desc, TICKETS_ATENDIDOS desc";
        List<Object[]> base = consultar(sql, p);
        String[] columnas = cols("Mes", "Trabajador", "Atendidos", "Cobrados", "Cancelados", "Total cobrado (S/)", "% cancelados");

        Map<String, String> m = new LinkedHashMap<>();
        if (base.isEmpty()) return sinDatos("Atenciones por trabajador", columnas, m);

        List<Object[]> filas = new ArrayList<>();
        Map<String, Long> atendidosPorTrab = new LinkedHashMap<>();
        Map<String, BigDecimal> cobradoPorTrab = new LinkedHashMap<>();
        long totalTickets = 0, totalCancelados = 0;
        BigDecimal totalCobrado = BigDecimal.ZERO;
        for (Object[] f : base) { long atendidos = lng(f[2]); long cancelados = lng(f[4]);BigDecimal cobrado = bd(f[5]);
            BigDecimal pctCanc = atendidos == 0 ? BigDecimal.ZERO
                    : BigDecimal.valueOf(cancelados).multiply(CIEN).divide(BigDecimal.valueOf(atendidos), 2, RoundingMode.HALF_UP);
            filas.add(new Object[] { f[0], f[1], atendidos, lng(f[3]), cancelados, cobrado, pctCanc });

            
            String trabajador = String.valueOf(f[1]);
            atendidosPorTrab.merge(trabajador, atendidos, Long::sum);
            cobradoPorTrab.merge(trabajador, cobrado, BigDecimal::add);
            totalTickets += atendidos;
            totalCancelados += cancelados;
            totalCobrado = totalCobrado.add(cobrado); }
        Map.Entry<String, Long> topAtenciones = null;
        for (Map.Entry<String, Long> e : atendidosPorTrab.entrySet()) {
            if (topAtenciones == null || e.getValue() > topAtenciones.getValue()) topAtenciones = e;
        }
        Map.Entry<String, BigDecimal> topCobro = null;
        for (Map.Entry<String, BigDecimal> e : cobradoPorTrab.entrySet()) {
            if (topCobro == null || e.getValue().compareTo(topCobro.getValue()) > 0) topCobro = e;
        }
        m.put("Tickets atendidos", String.valueOf(totalTickets));
        m.put("Tickets cancelados", totalCancelados + "  (" + porcentaje(BigDecimal.valueOf(totalCancelados), BigDecimal.valueOf(totalTickets)) + ")");
        m.put("Total cobrado", soles(totalCobrado));
        m.put("Mas atenciones", topAtenciones.getKey() + " (" + topAtenciones.getValue() + " tickets)");
        m.put("Mayor monto cobrado", topCobro.getKey() + " (" + soles(topCobro.getValue()) + ")");
        return new Resultado("Atenciones por trabajador", columnas, filas, m);
}

    private Resultado retrasos(LocalDate desde, LocalDate hasta) throws SQLException {
        List<Object> p = new ArrayList<>();
        String sql = "select NRO_TICKET, TRABAJADOR, fecha_ESTIMADA, fecha_TERMINO, MINUTOS_retraso, MOTIVO_retraso"
                + " from V_REP_retrasoS"
                + filtroFecha("fecha_TERMINO", desde, hasta, p, " where ")
                + " order by fecha_TERMINO desc";
        List<Object[]> filas = consultar(sql, p);
        String[] columnas = cols("Ticket", "Trabajador", "Fecha estimada", "Fecha termino", "Minutos de retraso", "Motivo");
        Map<String, String> m = new LinkedHashMap<>();
        if (filas.isEmpty()) return sinDatos("Retrasos del servicio", columnas, m);
        long sumaMin = 0; Object[] peor = null; Map<String, Long> retrasosPorTrab = new LinkedHashMap<>();
        for (Object[] f : filas) {
            long min = lng(f[4]);
            sumaMin += min;
            if (peor == null || min > lng(peor[4])) peor = f;
            retrasosPorTrab.merge(String.valueOf(f[1]), 1L, Long::sum);
        }
        Map.Entry<String, Long> topTrab = null;
        for (Map.Entry<String, Long> e : retrasosPorTrab.entrySet()) {
            if (topTrab == null || e.getValue() > topTrab.getValue()) topTrab = e;
        }
        m.put("Tickets con retraso", String.valueOf(filas.size()));
        m.put("Retraso promedio", dividir(BigDecimal.valueOf(sumaMin), filas.size()) + " min");
        m.put("Mayor retraso", peor[0] + " (" + lng(peor[4]) + " min)");
        m.put("Trabajador con mas retrasos", topTrab.getKey() + " (" + topTrab.getValue() + ")");
        return new Resultado("Retrasos del servicio", columnas, filas, m);
    }

    private Resultado descuentosYRecargos(LocalDate desde, LocalDate hasta) throws SQLException {
        List<Object> p = new ArrayList<>();
        String sql = "select NRO_TICKET, fecha, ESTADO, descuento_PORCENTAJE, descuento_MONTO, MOTIVO_descuento,"
        + " RECARGO_SUCIEDAD, RECARGO_TARJETA from V_REP_descuentoS_recargos"
        + filtroFecha("fecha", desde, hasta, p, " where ")
        + " order by fecha desc";
        List<Object[]> filas = consultar(sql, p);
        String[] columnas = cols("Ticket", "Fecha", "Estado", "% Desc.", "Descuento (S/)", "Motivo descuento",
                "Recargo suciedad (S/)", "Recargo tarjeta (S/)");
        Map<String, String> m = new LinkedHashMap<>();
        if (filas.isEmpty()) return sinDatos("Descuentos y recargos aplicados", columnas, m);

        BigDecimal desc = BigDecimal.ZERO, suciedad = BigDecimal.ZERO, tarjeta = BigDecimal.ZERO;
        for (Object[] f : filas) {
            desc = desc.add(bd(f[4]));
            suciedad = suciedad.add(bd(f[6]));
            tarjeta = tarjeta.add(bd(f[7]));
        }
        m.put("Tickets con ajustes", String.valueOf(filas.size()));
        m.put("Total descuentos", soles(desc));
        m.put("Total recargo por suciedad", soles(suciedad));
        m.put("Total recargo por tarjeta", soles(tarjeta));
        return new Resultado("Descuentos y recargos aplicados", columnas, filas, m);
    }

    private Resultado ticketsPorCobrar() throws SQLException {
        List<Object[]> filas = consultar("select NRO_TICKET, PLACA, CLIENTE, CONTACTO, fecha_TERMINO, horas_ESPERANDO, TOTAL_PROVISIONAL"
        + " from  v_rep_tickets_por_cobrar order by fecha_TERMINO", new ArrayList<>());
        String[] columnas = cols("Ticket", "Placa", "Cliente", "Contacto", "Terminado", "Horas esperando", "Total provisional (S/)");

        
        
        Map<String, String> m = new LinkedHashMap<>();
        if (filas.isEmpty()) return sinDatos("Tickets por cobrar", columnas, m);
        BigDecimal total = BigDecimal.ZERO;
        Object[] masEspera = null;
        for (Object[] f : filas) {
            total = total.add(bd(f[6]));
            if (masEspera == null || lng(f[5]) > lng(masEspera[5])) masEspera = f;
        }
        
        
        
        m.put("Tickets por cobrar", String.valueOf(filas.size()));
        m.put("Total provisional", soles(total) + "  (aun sin IGV ni recargo de tarjeta)");
        m.put("Mayor espera", masEspera[0] + " (" + lng(masEspera[5]) + " h)");
        return new Resultado("Tickets por cobrar", columnas, filas, m);
    }

    private Resultado autosEnLocal() throws SQLException {
        List<Object[]> filas = consultar( "select NRO_TICKET, PLACA, TIPO_VEHICULO, CLIENTE, CONTACTO, ESTADO, fecha_INGRESO"
        + " from v_rep_autos_en_local order by fecha_INGRESO", new ArrayList<>());
        String[] columnas = cols("Ticket", "Placa", "Tipo", "Cliente", "Contacto", "Estado", "Ingreso");
        Map<String, String> m = new LinkedHashMap<>();
        if (filas.isEmpty()) return sinDatos("Autos en el local", columnas, m);
        Map<String, Long> porEstado = new LinkedHashMap<>();
        for (Object[] f : filas) porEstado.merge(String.valueOf(f[5]), 1L, Long::sum);

        
        m.put("Autos en el local", String.valueOf(filas.size()));
        for (Map.Entry<String, Long> e : porEstado.entrySet()) m.put("  " + e.getKey(), String.valueOf(e.getValue()));
        Object[] masAntiguo = filas.get(0); 
        m.put("Mas antiguo", masAntiguo[0] + " (" + masAntiguo[1] + ", desde "
                + ((LocalDateTime) masAntiguo[6]).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ")");
        return new Resultado("Autos en el local", columnas, filas, m);
    }

    private Resultado catalogo() throws SQLException {
        List<Servicio> lista = listarCatalogo(false);
        List<Object[]> filas = new ArrayList<>();
        long activos = 0;
        for (Servicio s : lista) {
            filas.add(new Object[] { (long) s.getIdServicio(), s.getNombre(), s.getDescripcion(), s.isActivo() ? "Si" : "No" });
            if (s.isActivo()) activos++;
        }
        Map<String, String> m = new LinkedHashMap<>();
        m.put("Servicios en catalogo", String.valueOf(lista.size()));
        m.put("Activos", String.valueOf(activos));
        m.put("Inactivos", String.valueOf(lista.size() - activos));
        return new Resultado("Catalogo de servicios", cols("ID", "Servicio", "Descripcion", "Activo"), filas, m);
    }

    public List<Servicio> listarCatalogo(boolean soloActivos) throws SQLException {
        String sql = "select id_servicio, NOMBRE, descRIPCION, ACTIVO from SERVICIO"
                + (soloActivos ? " where ACTIVO = 1" : "") + " order by NOMBRE";
        List<Servicio> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Servicio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4)));
            }
        }
        return lista;
    }

    private static String filtroFecha(String columna, LocalDate desde, LocalDate hasta, List<Object> params, String prefijo) {
        List<String> cond = new ArrayList<>();
        if (desde != null) {
            cond.add("DATE(" + columna + ") >= ?");
            params.add(java.sql.Date.valueOf(desde));
        }
        if (hasta != null) {
            cond.add("DATE(" + columna + ") <= ?");
            params.add(java.sql.Date.valueOf(hasta));
        }
        return cond.isEmpty() ? "" : prefijo + String.join(" AND ", cond);
    }

    private List<Object[]> consultar(String sql, List<Object> params) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                int n = rs.getMetaData().getColumnCount();
                List<Object[]> filas = new ArrayList<>();
                while (rs.next()) {
                Object[] fila = new Object[n];
                for (int i = 0; i < n; i++) fila[i] = convertir(rs.getObject(i + 1));
                 filas.add(fila);
                }
                return filas;
            }
        }
    }

    private static Object convertir(Object v) {
        if (v instanceof Timestamp) return ((Timestamp) v).toLocalDateTime(); if (v instanceof java.sql.Date) return ((java.sql.Date) v).toLocalDate(); return v;
    }

    private static Resultado sinDatos(String titulo, String[] columnas, Map<String, String> m) {
        m.put("Resultado", "Sin registros para el filtro seleccionado");
        return new Resultado(titulo, columnas, new ArrayList<Object[]>(), m);
    }

    private static String[] cols(String... c) { return c; }

    private static BigDecimal bd(Object v) {
        if (v == null) return BigDecimal.ZERO;
        if (v instanceof BigDecimal) return (BigDecimal) v;
        return new BigDecimal(v.toString());
    }

    private static long lng(Object v) {
        if (v == null) return 0L;
        if (v instanceof Number) return ((Number) v).longValue();
        return Long.parseLong(v.toString());
    }

    private static BigDecimal dividir(BigDecimal a, long b) {
        return b == 0 ? BigDecimal.ZERO : a.divide(BigDecimal.valueOf(b), 2, RoundingMode.HALF_UP);
    }

    private static String soles(BigDecimal v) {
        return "S/ " + v.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static String porcentaje(BigDecimal parte, BigDecimal total) {
        if (total.signum() == 0) return "0.00%";
        return parte.multiply(CIEN).divide(total, 2, RoundingMode.HALF_UP).toPlainString() + "%";
    }
}











