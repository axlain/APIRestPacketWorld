package pojo;
    
public class Unidad {
    private int idUnidad;
    private String marca;
    private String modelo;
    private int anio;
    private String vin;
    private String numeroInterno;
    private String motivoBaja;
    
    private int idTipoUnidad;
    private String tipoUnidad;
    private int idSucursal;
    private String codigoSucursal;
    private int idEstatusUnidad;
    private String estatusUnidad;
    
    private Integer idConductor;
    private String numeroPersonalConductor;

    public Unidad() {
    }

    public Unidad(int idUnidad, String marca, String modelo, int anio, String vin, String numeroInterno, String motivoBaja, int idTipoUnidad, String tipoUnidad, int idSucursal, String codigoSucursal, int idEstatusUnidad, String estatusUnidad, Integer idConductor, String numeroPersonalConductor) {
        this.idUnidad = idUnidad;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.vin = vin;
        this.numeroInterno = numeroInterno;
        this.motivoBaja = motivoBaja;
        this.idTipoUnidad = idTipoUnidad;
        this.tipoUnidad = tipoUnidad;
        this.idSucursal = idSucursal;
        this.codigoSucursal = codigoSucursal;
        this.idEstatusUnidad = idEstatusUnidad;
        this.estatusUnidad = estatusUnidad;
        this.idConductor = idConductor;
        this.numeroPersonalConductor = numeroPersonalConductor;
    }


    public int getIdUnidad() {
        return idUnidad;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public String getVin() {
        return vin;
    }

    public String getNumeroInterno() {
        return numeroInterno;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public int getIdTipoUnidad() {
        return idTipoUnidad;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public String getCodigoSucursal() {
        return codigoSucursal;
    }

    public int getIdEstatusUnidad() {
        return idEstatusUnidad;
    }

    public String getEstatusUnidad() {
        return estatusUnidad;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public String getNumeroPersonalConductor() {
        return numeroPersonalConductor;
    }
    
    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setNumeroInterno(String numeroInterno) {
        this.numeroInterno = numeroInterno;
    }

    public void setMotivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
    }

    public void setIdTipoUnidad(int idTipoUnidad) {
        this.idTipoUnidad = idTipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public void setCodigoSucursal(String codigoSucursal) {
        this.codigoSucursal = codigoSucursal;
    }

    public void setIdEstatusUnidad(int idEstatusUnidad) {
        this.idEstatusUnidad = idEstatusUnidad;
    }

    public void setEstatusUnidad(String estatusUnidad) {
        this.estatusUnidad = estatusUnidad;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    public void setNumeroPersonalConductor(String numeroPersonalConductor) {
        this.numeroPersonalConductor = numeroPersonalConductor;
    }
    
    
}
