package Modelo;

public class Cliente {
    private int idCliente;
    private String primerNombre;
    private String primerApellido;
    private String codigoPais; //Hace referencia al codigo del pais del numero del celular (e.g PE = +51)
    private String celular;
    private String dni;
    private String ruc;
    private String razonSocial;

    public Cliente(int idCliente, String primerNombre, String primerApellido, String codigoPais, String celular, String dni, String ruc, String razonSocial) {
        this.idCliente = idCliente;
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
        this.codigoPais = codigoPais;
        this.celular = celular;
        this.dni = dni;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
    }

    public Cliente() {
    }
    
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
}
