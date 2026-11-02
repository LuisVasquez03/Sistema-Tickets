package Modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ticket {
    private int idOrden;
    private String nroTicket;
    private int idCliente;
    private int idVehiculo;
    private int idTrabajador;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaEstimada;
    private LocalDateTime fechaTermino;
    private LocalDateTime fechaSalida;
    private String estado;
    private String motivoRetraso;
    private String notaCorreccion;
    private String motivoCancelacion;
    private BigDecimal subtotal;
    private BigDecimal descuentoPorcentaje;
    private BigDecimal descuentoMonto;
    private String motivoDescuento;
    private BigDecimal recargoTarjeta;
    private BigDecimal montoIgv;
    private BigDecimal total;

    public Ticket(int idOrden, String nroTicket, int idCliente, int idVehiculo, int idTrabajador, LocalDateTime fechaIngreso, LocalDateTime fechaEstimada, LocalDateTime fechaTermino, LocalDateTime fechaSalida, String estado, String motivoRetraso, String notaCorreccion, String motivoCancelacion, BigDecimal subtotal, BigDecimal descuentoPorcentaje, BigDecimal descuentoMonto, String motivoDescuento, BigDecimal recargoTarjeta, BigDecimal montoIgv, BigDecimal total) {
        this.idOrden = idOrden;
        this.nroTicket = nroTicket;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.idTrabajador = idTrabajador;
        this.fechaIngreso = fechaIngreso;
        this.fechaEstimada = fechaEstimada;
        this.fechaTermino = fechaTermino;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
        this.motivoRetraso = motivoRetraso;
        this.notaCorreccion = notaCorreccion;
        this.motivoCancelacion = motivoCancelacion;
        this.subtotal = subtotal;
        this.descuentoPorcentaje = descuentoPorcentaje;
        this.descuentoMonto = descuentoMonto;
        this.motivoDescuento = motivoDescuento;
        this.recargoTarjeta = recargoTarjeta;
        this.montoIgv = montoIgv;
        this.total = total;
    }

    public Ticket() {
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public String getNroTicket() {
        return nroTicket;
    }

    public void setNroTicket(String nroTicket) {
        this.nroTicket = nroTicket;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(LocalDateTime fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public LocalDateTime getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(LocalDateTime fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoRetraso() {
        return motivoRetraso;
    }

    public void setMotivoRetraso(String motivoRetraso) {
        this.motivoRetraso = motivoRetraso;
    }

    public String getNotaCorreccion() {
        return notaCorreccion;
    }

    public void setNotaCorreccion(String notaCorreccion) {
        this.notaCorreccion = notaCorreccion;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    public void setDescuentoPorcentaje(BigDecimal descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
    }

    public BigDecimal getDescuentoMonto() {
        return descuentoMonto;
    }

    public void setDescuentoMonto(BigDecimal descuentoMonto) {
        this.descuentoMonto = descuentoMonto;
    }

    public String getMotivoDescuento() {
        return motivoDescuento;
    }

    public void setMotivoDescuento(String motivoDescuento) {
        this.motivoDescuento = motivoDescuento;
    }

    public BigDecimal getRecargoTarjeta() {
        return recargoTarjeta;
    }

    public void setRecargoTarjeta(BigDecimal recargoTarjeta) {
        this.recargoTarjeta = recargoTarjeta;
    }

    public BigDecimal getMontoIgv() {
        return montoIgv;
    }

    public void setMontoIgv(BigDecimal montoIgv) {
        this.montoIgv = montoIgv;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    
}
