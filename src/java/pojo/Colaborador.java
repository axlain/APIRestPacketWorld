package pojo;

public class Colaborador {
    private int id_colaborador;
    private String nombre; 
    private String apellido_materno; 
    private String curp; 
    private String correo; 
    private String numero_personal; 
    private String contrasena; 
    private String numero_licencia; 
    
    private int id_rol;
    private String rol; 
    private int id_sucursal;
    private String sucursal; 
    private int id_unidad;
    private String unidad; 
    
    private byte[] foto; 
    private String fotoBase64;

    public Colaborador() {
    }

    public Colaborador(int id_colaborador, String nombre, String apellido_materno, String curp, String correo, String numero_personal, String contrasena, String numero_licencia, int id_rol, String rol, int id_sucursal, String sucursal, int id_unidad, String unidad) {
        this.id_colaborador = id_colaborador;
        this.nombre = nombre;
        this.apellido_materno = apellido_materno;
        this.curp = curp;
        this.correo = correo;
        this.numero_personal = numero_personal;
        this.contrasena = contrasena;
        this.numero_licencia = numero_licencia;
        this.id_rol = id_rol;
        this.rol = rol;
        this.id_sucursal = id_sucursal;
        this.sucursal = sucursal;
        this.id_unidad = id_unidad;
        this.unidad = unidad;
    }

    public int getId_colaborador() {
        return id_colaborador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public String getCurp() {
        return curp;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNumero_personal() {
        return numero_personal;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNumero_licencia() {
        return numero_licencia;
    }

    public int getId_rol() {
        return id_rol;
    }

    public String getRol() {
        return rol;
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public String getSucursal() {
        return sucursal;
    }

    public int getId_unidad() {
        return id_unidad;
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

    public void setId_colaborador(int id_colaborador) {
        this.id_colaborador = id_colaborador;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNumero_personal(String numero_personal) {
        this.numero_personal = numero_personal;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setNumero_licencia(String numero_licencia) {
        this.numero_licencia = numero_licencia;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public void setId_unidad(int id_unidad) {
        this.id_unidad = id_unidad;
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
