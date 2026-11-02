package Modelo;

import java.math.BigDecimal;

public class DetalleOrden {
    private int idDetalle;
    private int idOrden;
    private int idServicio;
    private BigDecimal precioAplicado;
    private BigDecimal recargo;
    private String motivoRecargo;

    public DetalleOrden(int idDetalle, int idOrden, int idServicio, BigDecimal precioAplicado, BigDecimal recargo, String motivoRecargo) {
        this.idDetalle = idDetalle;
        this.idOrden = idOrden;
        this.idServicio = idServicio;
        this.precioAplicado = precioAplicado;
        this.recargo = recargo;
        this.motivoRecargo = motivoRecargo;
    }
    
    public DetalleOrden() {
    }

    public String getMotivoRecargo() {
        return motivoRecargo;
    }

    public void setMotivoRecargo(String motivoRecargo) {
        this.motivoRecargo = motivoRecargo;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public BigDecimal getPrecioAplicado() {
        return precioAplicado;
    }

    public void setPrecioAplicado(BigDecimal precioAplicado) {
        this.precioAplicado = precioAplicado;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }
    
}
