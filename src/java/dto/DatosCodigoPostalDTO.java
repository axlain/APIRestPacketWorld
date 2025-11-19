package dto;

public class DatosCodigoPostalDTO {

    private boolean error;
    private String mensaje;

    private Integer idPais;
    private String pais;

    private Integer idEstado;
    private String estado;

    private Integer idMunicipio;
    private String municipio;

    private String codigoPostal;

    public DatosCodigoPostalDTO() {
    }

    public DatosCodigoPostalDTO(boolean error, String mensaje, Integer idPais, String pais, Integer idEstado, String estado, Integer idMunicipio, String municipio, String codigoPostal) {
        this.error = error;
        this.mensaje = mensaje;
        this.idPais = idPais;
        this.pais = pais;
        this.idEstado = idEstado;
        this.estado = estado;
        this.idMunicipio = idMunicipio;
        this.municipio = municipio;
        this.codigoPostal = codigoPostal;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    
}
