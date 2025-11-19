package pojo;

public class Municipio {
    private Integer idMunicipio;
    private Integer codigo;
    private String nombre;
    private Integer idEstado;

    public Municipio() {
    }

    public Municipio(Integer idMunicipio, Integer codigo, String nombre, Integer idEstado) {
        this.idMunicipio = idMunicipio;
        this.codigo = codigo;
        this.nombre = nombre;
        this.idEstado = idEstado;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
    
    
}