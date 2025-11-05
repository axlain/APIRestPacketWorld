package dto;

import pojo.Colaborador;

public class RSAutentificarAdmin {
    private boolean error; 
    private String mensaje; 
    private Colaborador colaborador; 

    public RSAutentificarAdmin() {
    }

    public RSAutentificarAdmin(boolean error, String mensaje, Colaborador colaborador) {
        this.error = error;
        this.mensaje = mensaje;
        this.colaborador = colaborador;
    }

    public boolean isError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }
}
