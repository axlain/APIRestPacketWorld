package pojo;

public class Colonia {
    private Integer idColonia;
    private String nombre;
    private String ubicacion;
    private String asentamiento;
    private Integer idMunicipio;
    private Integer codigoMunicipio;
    private String codigoPostal;

    public Colonia() {
    }

    public Colonia(Integer idColonia, String nombre, String ubicacion, String asentamiento, Integer idMunicipio, Integer codigoMunicipio, String codigoPostal) {
        this.idColonia = idColonia;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.asentamiento = asentamiento;
        this.idMunicipio = idMunicipio;
        this.codigoMunicipio = codigoMunicipio;
        this.codigoPostal = codigoPostal;
    }

    public Integer getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(String asentamiento) {
        this.asentamiento = asentamiento;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Integer getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Integer codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    
}