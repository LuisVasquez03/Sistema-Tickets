package Modelo;

public class Trabajador {
    private int idTrabajador;
    private String primerNombre;
    private String primerApellido;
    private String codigoPais;
    private String celular;
    private String cargo;
    private String usuario;
    private String claveHash; //Esta es basicamente la contrasena
    private boolean activo;

    public Trabajador(int idTrabajador, String primerNombre, String primerApellido, String codigoPais, String celular, String cargo, String usuario, String claveHash, boolean activo) {
        this.idTrabajador = idTrabajador;
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
        this.codigoPais = codigoPais;
        this.celular = celular;
        this.cargo = cargo;
        this.usuario = usuario;
        this.claveHash = claveHash;
        this.activo = activo;
    }

    public Trabajador() {
    }
    
    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClaveHash() {
        return claveHash;
    }

    public void setClaveHash(String claveHash) {
        this.claveHash = claveHash;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
}
