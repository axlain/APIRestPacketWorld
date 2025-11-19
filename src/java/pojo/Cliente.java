package pojo;

public class Cliente {
    private Integer idCliente;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String calle;
    private String numero;
    private String telefono;
    private String correo;
    
    private Integer idPais;
    private String pais;
    private Integer idEstado;
    private String estado;
    private Integer idMunicipio;
    private String municipio;
    private Integer idColonia;
    private String colonia;

    public Cliente() {
    }

    public Cliente(Integer idCliente, String nombre, String apellidoPaterno, String apellidoMaterno, String calle, String numero, String telefono, String correo, Integer idPais, String pais, Integer idEstado, String estado, Integer idMunicipio, String municipio, Integer idColonia, String colonia) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.calle = calle;
        this.numero = numero;
        this.telefono = telefono;
        this.correo = correo;
        this.idPais = idPais;
        this.pais = pais;
        this.idEstado = idEstado;
        this.estado = estado;
        this.idMunicipio = idMunicipio;
        this.municipio = municipio;
        this.idColonia = idColonia;
        this.colonia = colonia;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumero() {
        return numero;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }
    
    public Integer getIdPais() {
        return idPais;
    }

    public String getPais() {
        return pais;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public Integer getIdColonia() {
        return idColonia;
    }

    public String getColonia() {
        return colonia;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    
    
}
