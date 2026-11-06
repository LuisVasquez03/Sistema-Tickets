
package Modelo;
public class Servicio {
 
public static final int max_nombre = 60;
public static final int max_descripcion = 600;
private int idServicio; 
private String nombre;
private String descripcion;
private boolean activo;
 
public Servicio(String nombre, String descripcion) {
this(0, nombre, descripcion, true);  }
 
public Servicio(int idServicio, String nombre, String descripcion, boolean activo) {
this.idServicio = idServicio;
setNombre(nombre);
setDescripcion(descripcion);
this.activo = activo;   }
 
public int getIdServicio() { return idServicio; }
public void setIdServicio(int idServicio) { this.idServicio = idServicio; }
 
public String getNombre() { return nombre; }
 public void setNombre(String nombre) {
if (nombre == null || nombre.trim().isEmpty()) { throw new IllegalArgumentException("El nombre del servicio es obligatorio.");   }
nombre = nombre.trim();
if (nombre.length() > max_nombre) {
throw new IllegalArgumentException("El nombre no puede superar " + max_nombre + " caracteres.");  }
this.nombre = nombre;
}
 
public String getDescripcion() { return descripcion; }
public void setDescripcion(String descripcion) {
if (descripcion == null || descripcion.trim().isEmpty()) {
throw new IllegalArgumentException("La descripción del servicio es obligatoria.");
}
descripcion = descripcion.trim();
if (descripcion.length() > max_descripcion) {
throw new IllegalArgumentException("La descripcion tiene como limite " + max_descripcion + " caracteres.");
}
this.descripcion = descripcion;
}
 
public boolean isActivo() { return activo; }
public void setActivo(boolean activo) { this.activo = activo; }
 
@Override
public String toString() {
return nombre;}
 
@Override
public boolean equals(Object o) {
if (this == o) return true;
if (!(o instanceof Servicio)) return false;
return nombre.equalsIgnoreCase(((Servicio) o).nombre);
}
 
@Override
public int hashCode() {
return nombre.toLowerCase().hashCode();  }
}

