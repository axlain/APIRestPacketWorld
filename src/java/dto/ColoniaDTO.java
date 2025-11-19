package dto;

public class ColoniaDTO {

    private boolean error;
    private String mensaje;

    private Integer idColonia;
    private String colonia;
    private String codigoPostal;

    public ColoniaDTO() {
    }

    public ColoniaDTO(boolean error, String mensaje, Integer idColonia, String colonia, String codigoPostal) {
        this.error = error;
        this.mensaje = mensaje;
        this.idColonia = idColonia;
        this.colonia = colonia;
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

    public Integer getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    
}
