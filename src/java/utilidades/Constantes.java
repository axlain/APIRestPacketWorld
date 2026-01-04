package utilidades;

public class Constantes {

    public static final String MSJ_ERROR_BD = "Lo sentimos, no hay conexión con la base de datos.";

    // --- Mensajes genéricos de acción ---
    public static final String MSJ_EXITO_REGISTRO = "Se registró correctamente";
    public static final String MSJ_EXITO_ACTUALIZAR = "Se actualizó correctamente";
    public static final String MSJ_EXITO_BAJA = "Se dio de baja correctamente";
    public static final String MSJ_ERROR_REGISTRO = "No se pudo registrar la";
    public static final String MSJ_ERROR_ACTUALIZAR = "No se pudo actualizar la";
    public static final String MSJ_ERROR_BAJA = "No se pudo dar de baja la";
    public static final String MSJ_ERROR_INACTIVA = "No se puede editar una";
    public static final String MSJ_ERROR_DEPENDENCIAS = "No se puede dar de baja la";
    public static final String MSJ_ERROR_DEPENDENCIAS_MOTIVO = " porque tiene dependencias activas.";
    public static final String MSJ_ERROR_PAQUETES = "Error al registrar el paquete: ";
    public static final String MSJ_ERROR_COSTO = "Error al actualizar el costo del envío.";

    public static final String SUCURSAL = " sucursal";
    public static final String UNIDAD = " unidad";
    public static final String CONDUCTOR = " conductor";
    public static final String COLABORADOR = " colaborador";

    public static final String FORMATO_DOS_DIGITOS = "%02d";
    public static final String PREFIJO_SUCURSAL = "SUC";

    public static final int ESTATUS_ACTIVO = 1;
    public static final int ESTATUS_INACTIVO = 2;
    public static final int ROL_CONDUCTOR = 3;
    
    /*public static final double COSTO_POR_KG = 25.0;
    public static final double COSTO_POR_CM3 = 0.0002;
    public static final double COSTO_POR_KM = 8.0;*/
    
    //Comentarios de historial de envío
    public static final String HIST_ENVIO_REGISTRO_INICIAL =
            "Registro inicial del envío";
    public static final String HIST_ENVIO_PROCESADO =
            "Envío procesado en sucursal";
    public static final String HIST_ENVIO_EN_TRANSITO =
            "Envío salió a ruta";
    public static final String HIST_ENVIO_DETENIDO =
            "Envío detenido por incidencia";
    public static final String HIST_ENVIO_REANUDADO =
            "Incidencia resuelta, envío reanudado";
    public static final String HIST_ENVIO_ENTREGADO =
            "Envío entregado al destinatario";
    public static final String HIST_ENVIO_CANCELADO =
            "Envío cancelado";
    public static final String MSJ_TRANSICION_INVALIDA =
            "Transición inválida de estatus del envío.";
    
    
    
}
