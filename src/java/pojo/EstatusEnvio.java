package pojo;

public class EstatusEnvio {
    private Integer idEstatus;
    private String nombre;

    public EstatusEnvio() {
    }

    public EstatusEnvio(Integer idEstatus, String nombre) {
        this.idEstatus = idEstatus;
        this.nombre = nombre;
    }

    public Integer getIdEstatus() {
        return idEstatus;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
