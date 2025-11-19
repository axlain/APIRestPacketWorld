package pojo;

public class Envio {
    private int idEnvio;
    private String numeroGuia;
    private int idDestinatario;
    private int idCliente;
    private int idSucursal;
    private Integer idConductor;
    private int idEstatusActual;
    private double costoTotal;

    public Envio() {}

    public Envio(int idEnvio, String numeroGuia, int idDestinatario, int idCliente, int idSucursal, Integer idConductor, int idEstatusActual, double costoTotal) {
        this.idEnvio = idEnvio;
        this.numeroGuia = numeroGuia;
        this.idDestinatario = idDestinatario;
        this.idCliente = idCliente;
        this.idSucursal = idSucursal;
        this.idConductor = idConductor;
        this.idEstatusActual = idEstatusActual;
        this.costoTotal = costoTotal;
    }

    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public int getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(int idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    public int getIdEstatusActual() {
        return idEstatusActual;
    }

    public void setIdEstatusActual(int idEstatusActual) {
        this.idEstatusActual = idEstatusActual;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }
    
    
    
}
