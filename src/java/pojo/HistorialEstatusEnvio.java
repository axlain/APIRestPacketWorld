package pojo;

import java.util.Date;

public class HistorialEstatusEnvio {
    private Integer idHistorial;
    private Integer idEnvio;
    private Integer idEstatus;
    private Integer idColaborador;
    private Date fechaCambio;
    private String comentario;
    private String nombreEstatus;
    private String nombreColaborador;
    private String apellidoPaternoColaborador;
    public HistorialEstatusEnvio() {
    }

    public HistorialEstatusEnvio(Integer idHistorial, Integer idEnvio, Integer idEstatus, Integer idColaborador, Date fechaCambio, String comentario, String nombreEstatus, String nombreColaborador, String apellidoPaternoColaborador) {
        this.idHistorial = idHistorial;
        this.idEnvio = idEnvio;
        this.idEstatus = idEstatus;
        this.idColaborador = idColaborador;
        this.fechaCambio = fechaCambio;
        this.comentario = comentario;
        this.nombreEstatus = nombreEstatus;
        this.nombreColaborador = nombreColaborador;
        this.apellidoPaternoColaborador = apellidoPaternoColaborador;
    }

    

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public Integer getIdEstatus() {
        return idEstatus;
    }

    public Integer getIdColaborador() {
        return idColaborador;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public String getComentario() {
        return comentario;
    }

    public String getNombreEstatus() {
        return nombreEstatus;
    }

    public String getNombreColaborador() {
        return nombreColaborador;
    }

    public String getApellidoPaternoColaborador() {
        return apellidoPaternoColaborador;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public void setIdColaborador(Integer idColaborador) {
        this.idColaborador = idColaborador;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setNombreEstatus(String nombreEstatus) {
        this.nombreEstatus = nombreEstatus;
    }

    public void setNombreColaborador(String nombreColaborador) {
        this.nombreColaborador = nombreColaborador;
    }

    public void setApellidoPaternoColaborador(String apellidoPaternoColaborador) {
        this.apellidoPaternoColaborador = apellidoPaternoColaborador;
    }
    
    
}
