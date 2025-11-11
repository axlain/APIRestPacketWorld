package pojo;

public class Sucursal {
    private int idSucursal;
    private String codigo;
    private String nombreCorto;
    private String calle;
    private String numero;

    private int idPais;
    private String Pais;
    private int idEstado;
    private String Estado;
    private int idMunicipio;
    private String Municipio;
    private int idColonia;
    private String Colonia;
    private String codigoPostal;
    private int idEstatusSucursal;
    private String EstatusSucursal;

    public Sucursal() {
    }

    public Sucursal(int idSucursal, String codigo, String nombreCorto, String calle, String numero, int idPais, String Pais, int idEstado, String Estado, int idMunicipio, String Municipio, int idColonia, String Colonia, String codigoPostal, int idEstatusSucursal, String EstatusSucursal) {
        this.idSucursal = idSucursal;
        this.codigo = codigo;
        this.nombreCorto = nombreCorto;
        this.calle = calle;
        this.numero = numero;
        this.idPais = idPais;
        this.Pais = Pais;
        this.idEstado = idEstado;
        this.Estado = Estado;
        this.idMunicipio = idMunicipio;
        this.Municipio = Municipio;
        this.idColonia = idColonia;
        this.Colonia = Colonia;
        this.codigoPostal = codigoPostal;
        this.idEstatusSucursal = idEstatusSucursal;
        this.EstatusSucursal = EstatusSucursal;
    }


    public int getIdSucursal() {
        return idSucursal;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public String getCalle() {
        return calle;
    }

    public String getNumero() {
        return numero;
    }

    public int getIdPais() {
        return idPais;
    }

    public String getPais() {
        return Pais;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public String getEstado() {
        return Estado;
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public String getMunicipio() {
        return Municipio;
    }

    public int getIdColonia() {
        return idColonia;
    }

    public String getColonia() {
        return Colonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public int getIdEstatusSucursal() {
        return idEstatusSucursal;
    }

    public String getEstatusSucursal() {
        return EstatusSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public void setPais(String Pais) {
        this.Pais = Pais;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public void setMunicipio(String Municipio) {
        this.Municipio = Municipio;
    }

    public void setIdColonia(int idColonia) {
        this.idColonia = idColonia;
    }

    public void setColonia(String Colonia) {
        this.Colonia = Colonia;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setIdEstatusSucursal(int idEstatusSucursal) {
        this.idEstatusSucursal = idEstatusSucursal;
    }

    public void setEstatusSucursal(String EstatusSucursal) {
        this.EstatusSucursal = EstatusSucursal;
    }
    
    
    
}
