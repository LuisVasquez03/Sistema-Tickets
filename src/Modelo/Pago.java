package Modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {
    private int idPago;
    private int idOrden;
    private BigDecimal monto;
    private String metodoPago;
    private String detalleMetodo;
    private LocalDateTime fechaPago;

    public Pago(int idPago, int idOrden, BigDecimal monto, String metodoPago, String detalleMetodo, LocalDateTime fechaPago) {
        this.idPago = idPago;
        this.idOrden = idOrden;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.detalleMetodo = detalleMetodo;
        this.fechaPago = fechaPago;
    }

    public Pago() {
    }
    
    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getDetalleMetodo() {
        return detalleMetodo;
    }

    public void setDetalleMetodo(String detalleMetodo) {
        this.detalleMetodo = detalleMetodo;
    }
    
}
