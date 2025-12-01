package pojo;

public class Estado {
    private Integer idEstado;
    private String nombre;
    private Integer idPais;
    private String pais;
    
    public Estado() {
    }

    public Estado(Integer idEstado, String nombre, Integer idPais, String pais) {
        this.idEstado = idEstado;
        this.nombre = nombre;
        this.idPais = idPais;
        this.pais = pais;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    
    
}
