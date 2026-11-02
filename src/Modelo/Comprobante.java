package Modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Comprobante {
    private int idComprobante;
    private int idOrden;
    private String tipo;
    private String serie;
    private String correlativo;
    private LocalDateTime fechaEmision;
    private BigDecimal importeTotal;

    public Comprobante(int idComprobante, int idOrden, String tipo, String serie, String correlativo, LocalDateTime fechaEmision, BigDecimal importeTotal) {
        this.idComprobante = idComprobante;
        this.idOrden = idOrden;
        this.tipo = tipo;
        this.serie = serie;
        this.correlativo = correlativo;
        this.fechaEmision = fechaEmision;
        this.importeTotal = importeTotal;
    }

    public Comprobante() {
    }

    public int getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(int idComprobante) {
        this.idComprobante = idComprobante;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(String correlativo) {
        this.correlativo = correlativo;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }
    
    
    
}
