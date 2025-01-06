package alquiler.trajes.constant;

public class ApplicationConstants {
    
    private ApplicationConstants () {}
    
    public static final String DATABASE_NAME_PROPERTY = "db.database.name";
    public static final String DATABASE_USERNAME_PROPERTY = "db.username";
    public static final String DATABASE_PASSWORD_PROPERTY = "db.password";
    public static final String DATABASE_URL_PROPERTY = "db.url";
    public static final String DATABASE_DRIVER_PROPERTY = "db.driver";
       
    public static final String LOCALE_LANGUAGE = "es";
    public static final String LOCALE_COUNTRY = "MX";
    public static final String LANGUAGE_TAG = "es_MX";
    
    public static final String DESCRIPTION_FORMAT_24_HRS = " Hrs.";
    public static final String DATE_PRINT_JASPER = "Fecha de impresión: ";
    public static final String DELETE_CHARS_NUMBER = "$,";
    public static final String EMPTY_STRING_TXT_FIELD = "";
    public static final String START_DAY_HOUR_MINUTES = " 00:01";
    public static final String END_DAY_HOUR_MINUTES = " 23:59";
    
    public static final String DECIMAL_FORMAT = "#,###,###,##0.00" ;
    public static final String DATE_MEDIUM = "dd MMM yyyy";
    public static final String DATE_LARGE = "EEEEE dd MMMM yyyy";
    public static final String DATE_FORMAT_FOR_SQL_QUERY = "yyyy-MM-dd";
    
    public static final String ARIAL = "Arial";
    public static final int ENTER_KEY = 10;
    
    public static final String NO_USER_FOUND_LOGIN = "No se encontro usuario, intenta de nuevo por favor.";
    
    public static final String NO_USER_FOUND = "Usuario no encontrado.";
    
    public static final String NO_FOUND_LIST_REGISTER = "No se obtuvieron registros.";
    
    public static final String KEY_USERS = "generic-user";
    public static final String TITLE_LOGIN_FORM = "Iniciar sesión.";
    public static final String TITLE_USERS_FORM = "Usuarios.";
    public static final String TITLE_UTILITY_FORM = "Utilidades del sistema.";
    public static final String TITLE_CUSTOMERS_FORM = "Clientes.";
    public static final String TITLE_NEW_EVENT_FORM = "Nuevo evento.";
    public static final String TITLE_EDIT_EVENT_FORM = "Editar evento.";
    public static final String TITLE_EVENTS_FORM = "Nuevo evento.";
    public static final String TITLE_CONSULT_EVENTS_FORM = "Consultar eventos.";
    public static final String TITLE_DETAIL_EVENT_DIALOG_FORM = "Detalle evento.";
       
    public static final String SELECT_A_ROW_TO_GENERATE_REPORT = "Selecciona una fila para generar el reporte...";
    public static final String SELECT_A_ROW_NECCESSARY = "Selecciona una fila para continuar...";
    public static final String MISSING_DATA = "Faltan datos.";
        
    
    public static final String LOGO_EMPRESA = "/logo_empresa.jpg";
        
    /* dato inicial para un combo box */
    public static final String CMB_SELECCIONE = "-seleccione-";
    
    /* mensajes para mostrar en los ventanas de avisos */
    public static final String MESSAGE_SAVE_SUCCESSFUL = "Se ha registrado con \u00E9xito.";
    public static final String MESSAGE_UPDATE_SUCCESSFUL = "Se actualiz\u00F3 con \u00E9xito.";
    public static final String MESSAGE_DELETE_SUCCESSFUL = "Se elimin\u00F3 con \u00E9xito.";
    public static final String MESSAGE_NOT_PARAMETER_RECEIVED = "No se recibi\u00F3 parametro.";
    public static final String MESSAGE_NOT_PERMISIONS_ADMIN = "No cuentas con permisos de administrador.";
    public static final String MESSAGE_MISSING_PARAMETERS = "Faltan parametros.";
    public static final String MESSAGE_UNEXPECTED_ERROR = "Ocurri\u00F3 un error inesperado.";
    public static final String MESSAGE_NUMBER_FORMAT_ERROR = "Introduce un numero valido.";
    public static final String MESSAGE_TITLE_ERROR = "Error";
    public static final String MESSAGE_TITLE_DETELE_RECORD_CONFIRM = "Confirme";
    public static final String MESSAGE_NOT_FOUND_JASPER_FILE = "No se encuentra el Archivo jasper.";
    public static final String NO_DATA_FOUND_EXCEPTION = "No se obtuvieron registros.";
    public static final String DETELE_RECORD_CONFIRM = "¿Eliminar registro? %s";
    
    /* mensaje generico */
    public static final String DS_MESSAGE_FAIL_LOGIN = "Contrase\u00F1a incorrecta o usuario no encontrado";
    public static final String DS_MESSAGE_EMPTY_PASSWD_LOGIN = "Introduce tu contraseña para ingresar.";
    public static final String TITLE_MESSAGE_FAIL_LOGIN = "Error al inciar sesion";
    
    // ****************************************************************************************
    // nombres de reportes jasper
    
    public static final String PATH_NAME_EVENT_REPORT_VERTICAL_A5 = "/event_report_vertical_A5.jasper";
    public static final String PDF_NAME_EVENT_REPORT_VERTICAL_A5 = "/event_report_vertical_A5.pdf";
    
    public static final String PATH_NAME_DETAIL_REPORT_A4_HORIZONTAL = "/detail_report_A4_horizontal.jasper";
    public static final String PDF_NAME_DETAIL_REPORT_A4_HORIZONTAL = "/detail_report_A4_horizontal.pdf";
}
