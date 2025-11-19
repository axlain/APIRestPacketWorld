package pojo;

public class Sucursal {
    private int idSucursal;
    private String codigo;
    private String nombreCorto;
    private String calle;
    private String numero;

    private int idPais;
    private String pais;
    private int idEstado;
    private String estado;
    private int idMunicipio;
    private String municipio;
    private int idColonia;
    private String colonia;

    private String codigoPostal;

    private int idEstatusSucursal;
    private String estatusSucursal;

    public Sucursal() {
    }

    public Sucursal(int idSucursal, String codigo, String nombreCorto, String calle, String numero, int idPais, String pais, int idEstado, String estado, int idMunicipio, String municipio, int idColonia, String colonia, String codigoPostal, int idEstatusSucursal, String estatusSucursal) {
        this.idSucursal = idSucursal;
        this.codigo = codigo;
        this.nombreCorto = nombreCorto;
        this.calle = calle;
        this.numero = numero;
        this.idPais = idPais;
        this.pais = pais;
        this.idEstado = idEstado;
        this.estado = estado;
        this.idMunicipio = idMunicipio;
        this.municipio = municipio;
        this.idColonia = idColonia;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.idEstatusSucursal = idEstatusSucursal;
        this.estatusSucursal = estatusSucursal;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public int getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(int idColonia) {
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

    public int getIdEstatusSucursal() {
        return idEstatusSucursal;
    }

    public void setIdEstatusSucursal(int idEstatusSucursal) {
        this.idEstatusSucursal = idEstatusSucursal;
    }

    public String getEstatusSucursal() {
        return estatusSucursal;
    }

    public void setEstatusSucursal(String estatusSucursal) {
        this.estatusSucursal = estatusSucursal;
    }
}
