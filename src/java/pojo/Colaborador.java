package pojo;

public class Colaborador {
    private int idColaborador;
    private String nombre; 
    private String apellidoPaterno; 
    private String apellidoMaterno; 
    private String curp; 
    private String correo; 
    private String numeroPersonal; 
    private String contrasena; 
    private String numeroLicencia; 
    
    private int idRol;
    private String rol; 
    private int idSucursal;
    private String sucursal; 
    private int idUnidad;
    private String unidad; 
    
    private byte[] foto; 
    private String fotoBase64;

    public Colaborador() {
    }

    public Colaborador(int idColaborador, String nombre, String apellidoPaterno, String apellidoMaterno, String curp, String correo, String numeroPersonal, String contrasena, String numeroLicencia, int idRol, String rol, int idSucursal, String sucursal, int idUnidad, String unidad, byte[] foto, String fotoBase64) {
        this.idColaborador = idColaborador;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.curp = curp;
        this.correo = correo;
        this.numeroPersonal = numeroPersonal;
        this.contrasena = contrasena;
        this.numeroLicencia = numeroLicencia;
        this.idRol = idRol;
        this.rol = rol;
        this.idSucursal = idSucursal;
        this.sucursal = sucursal;
        this.idUnidad = idUnidad;
        this.unidad = unidad;
        this.foto = foto;
        this.fotoBase64 = fotoBase64;
    }

    public int getIdColaborador() {
        return idColaborador;
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

    public String getCurp() {
        return curp;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getRol() {
        return rol;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public String getSucursal() {
        return sucursal;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public byte[] getFoto() {
        return foto;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
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

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNumeroPersonal(String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    
    
}
