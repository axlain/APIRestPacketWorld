package pojo;

public class Envio {
    private Integer idEnvio;
    private String numeroGuia;
    private Integer idDestinatario;
    private String nombreDestinatario;
    private String apellidoPaternoDestinatario;
    private Integer idCliente;
    private String nombreCliente;
    private String apellidoPaternoCliente;
    private Integer idSucursal;
    private String codigoSucursal;
    private Integer idConductor;
    private String numeroPersonalConductor; 
    private Integer idEstatusActual;
    private double costoTotal;
    private Integer idCreadoPor;
    private String numeroPersonalColaborador; 
    private String estatusActual;

    public Envio() {
    }

    public Envio(Integer idEnvio, String numeroGuia, Integer idDestinatario, String nombreDestinatario, String apellidoPaternoDestinatario, Integer idCliente, String nombreCliente, String apellidoPaternoCliente, Integer idSucursal, String codigoSucursal, Integer idConductor, String numeroPersonalConductor, Integer idEstatusActual, double costoTotal, Integer idCreadoPor, String numeroPersonalColaborador, String estatusActual) {
        this.idEnvio = idEnvio;
        this.numeroGuia = numeroGuia;
        this.idDestinatario = idDestinatario;
        this.nombreDestinatario = nombreDestinatario;
        this.apellidoPaternoDestinatario = apellidoPaternoDestinatario;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoPaternoCliente = apellidoPaternoCliente;
        this.idSucursal = idSucursal;
        this.codigoSucursal = codigoSucursal;
        this.idConductor = idConductor;
        this.numeroPersonalConductor = numeroPersonalConductor;
        this.idEstatusActual = idEstatusActual;
        this.costoTotal = costoTotal;
        this.idCreadoPor = idCreadoPor;
        this.numeroPersonalColaborador = numeroPersonalColaborador;
        this.estatusActual = estatusActual;
    }

    

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public Integer getIdDestinatario() {
        return idDestinatario;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public String getApellidoPaternoDestinatario() {
        return apellidoPaternoDestinatario;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getApellidoPaternoCliente() {
        return apellidoPaternoCliente;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public String getCodigoSucursal() {
        return codigoSucursal;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public String getNumeroPersonalConductor() {
        return numeroPersonalConductor;
    }

    public Integer getIdEstatusActual() {
        return idEstatusActual;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public Integer getIdCreadoPor() {
        return idCreadoPor;
    }

    public String getNumeroPersonalColaborador() {
        return numeroPersonalColaborador;
    }

    public String getEstatusActual() {
        return estatusActual;
    }
    
    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public void setIdDestinatario(Integer idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public void setApellidoPaternoDestinatario(String apellidoPaternoDestinatario) {
        this.apellidoPaternoDestinatario = apellidoPaternoDestinatario;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setApellidoPaternoCliente(String apellidoPaternoCliente) {
        this.apellidoPaternoCliente = apellidoPaternoCliente;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public void setCodigoSucursal(String codigoSucursal) {
        this.codigoSucursal = codigoSucursal;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    public void setNumeroPersonalConductor(String numeroPersonalConductor) {
        this.numeroPersonalConductor = numeroPersonalConductor;
    }

    public void setIdEstatusActual(Integer idEstatusActual) {
        this.idEstatusActual = idEstatusActual;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public void setIdCreadoPor(Integer idCreadoPor) {
        this.idCreadoPor = idCreadoPor;
    }

    public void setNumeroPersonalColaborador(String numeroPersonalColaborador) {
        this.numeroPersonalColaborador = numeroPersonalColaborador;
    }

    public void setEstatusActual(String estatusActual) {
        this.estatusActual = estatusActual;
    }

}
