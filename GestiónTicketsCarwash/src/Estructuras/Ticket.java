package Estructuras;

public class Ticket {
    private String codigo;
    private String fechaHora;
    private String telefonoCliente;
    private String vehiculoInfo;
    private String servicioNombre;
    private double precio;
    private String estado;

    public Ticket(String codigo, String fechaHora, String telefonoCliente, String vehiculoInfo, String servicioNombre, double precio, String estado) {
        this.codigo = codigo;
        this.fechaHora = fechaHora;
        this.telefonoCliente = telefonoCliente;
        this.vehiculoInfo = vehiculoInfo;
        this.servicioNombre = servicioNombre;
        this.precio = precio;
        this.estado = estado;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public String getVehiculoInfo() { return vehiculoInfo; }
    public void setVehiculoInfo(String vehiculoInfo) { this.vehiculoInfo = vehiculoInfo; }

    public String getServicioNombre() { return servicioNombre; }
    public void setServicioNombre(String servicioNombre) { this.servicioNombre = servicioNombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}