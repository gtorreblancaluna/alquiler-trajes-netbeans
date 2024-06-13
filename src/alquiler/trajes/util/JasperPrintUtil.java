package alquiler.trajes.util;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.DATE_LARGE;
import static alquiler.trajes.constant.ApplicationConstants.DECIMAL_FORMAT;
import static alquiler.trajes.constant.ApplicationConstants.DESCRIPTION_FORMAT_24_HRS;
import static alquiler.trajes.constant.ApplicationConstants.LOCALE_COUNTRY;
import static alquiler.trajes.constant.ApplicationConstants.LOCALE_LANGUAGE;
import static alquiler.trajes.constant.ApplicationConstants.MESSAGE_NOT_FOUND_JASPER_FILE;
import static alquiler.trajes.constant.ApplicationConstants.PATH_NAME_EVENT_REPORT_VERTICAL_A5;
import static alquiler.trajes.constant.ApplicationConstants.PDF_NAME_EVENT_REPORT_VERTICAL_A5;
import alquiler.trajes.constant.GeneralInfoEnum;
import alquiler.trajes.entity.Event;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.DataOriginException;
import alquiler.trajes.exceptions.JasperPrintUtilException;
import alquiler.trajes.service.DetailEventService;
import alquiler.trajes.service.GeneralInfoService;
import alquiler.trajes.service.PaymentService;
import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

public final class JasperPrintUtil {
    
    private JasperPrintUtil(){
        prop = PropertiesService.getInstance();
        this.bd = prop.getProperty("db.database.name");
        this.user = prop.getProperty("db.username");
        this.password = prop.getProperty("db.password");
        this.url = prop.getProperty("db.url");
        this.driver = prop.getProperty("db.driver");
        locale = new Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY);
        generalInfoService = GeneralInfoService.getInstance();
        fastDateFormatLarge = FastDateFormat.getInstance(DATE_LARGE,locale);
        paymentService = PaymentService.getInstance();
        detailEventService = DetailEventService.getInstance();
    }
    
    private static final Logger log = Logger.getLogger(JasperPrintUtil.class.getName());
    private static JasperPrintUtil SINGLE_INSTANCE;
    private final GeneralInfoService generalInfoService;
    private final FastDateFormat fastDateFormatLarge;
    private static final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
    private final Locale locale;
    private final PaymentService paymentService;
    private final DetailEventService detailEventService;
        
    public static synchronized JasperPrintUtil getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new JasperPrintUtil();
        }
        return SINGLE_INSTANCE;
    }
    
    public void printEventPDF (final Event event) throws BusinessException {
        getConnection();
        try {
            
            Float sumPayment = paymentService.getPaymentsByEvent(event.getId());
            Float sumDetail = detailEventService.getSubtotalByEvent(event.getId());
            
            Map<String,Object> parameters = new HashMap<>();
            
            String pathLocation = Utility.getPathLocation();
            
            parameters.put("URL_SUB_REPORT_CONSULTA", pathLocation+"/");
            parameters.put("URL_IMAGEN",pathLocation+ApplicationConstants.LOGO_EMPRESA );
            
            parameters.put("ID", event.getId().toString());
            parameters.put("FOLIO", event.getId().toString());
            parameters.put("USER_NAME", event.getUser().getName()+" "+event.getUser().getLastName());
            parameters.put("CUSTOMER_NAME", event.getCustomer().getName()+" "+event.getCustomer().getLastName());
            parameters.put("TYPE_EVENT", event.getCatalogTypeEvent().getName());
            parameters.put("STATUS_EVENT", event.getCatalogStatusEvent().getName());
            parameters.put("DELIVERY_DATE", fastDateFormatLarge.format(event.getDeliveryDate())+" "+event.getDeliveryHour()+DESCRIPTION_FORMAT_24_HRS);
            parameters.put("RETURN_DATE", fastDateFormatLarge.format(event.getReturnDate())+" "+event.getReturnHour()+DESCRIPTION_FORMAT_24_HRS);
            parameters.put("CREATED_AT_DATE", fastDateFormatLarge.format(event.getCreatedAt()));
            parameters.put("DESCRIPTION", event.getDescription());
            parameters.put("PAYMENTS", decimalFormat.format(sumPayment));
            parameters.put("SUBTOTAL", decimalFormat.format(sumDetail));
            parameters.put("TOTAL", decimalFormat.format(sumDetail-sumPayment));
            parameters.put("COMPANY_NAME", generalInfoService.getByKey(GeneralInfoEnum.COMPANY_NAME.getKey()));
            parameters.put("INFO_FOOTER_PDF_A5", generalInfoService.getByKey(GeneralInfoEnum.INFO_FOOTER_PDF_A5.getKey()));
            parameters.put("IMPORTANT_INFO", generalInfoService.getByKey(GeneralInfoEnum.IMPORTANT_INFO.getKey()));
            
            JasperPrint jasperPrint;
            String locationFile = pathLocation+PATH_NAME_EVENT_REPORT_VERTICAL_A5;
            System.out.println("Cargando desde: " + locationFile);
            if (locationFile == null) {
                throw new JasperPrintUtilException(MESSAGE_NOT_FOUND_JASPER_FILE);
            }
            JasperReport masterReport = (JasperReport) JRLoader.loadObjectFromFile(locationFile);
            jasperPrint = JasperFillManager.fillReport(masterReport, parameters, this.connection);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pathLocation+PDF_NAME_EVENT_REPORT_VERTICAL_A5);
            File file = new File(pathLocation+PDF_NAME_EVENT_REPORT_VERTICAL_A5);
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            throw new JasperPrintUtilException(e.getMessage(),e);
        }
    }
    
    public void showPDF (
            final Map<String,Object> parameters,
            final String pathJasperReport,
            final String namePDF
            ) throws BusinessException {
        getConnection();
        try {
            String pathLocation = Utility.getPathLocation();
            parameters.put("URL_SUB_REPORT_CONSULTA", pathLocation+"/");
            parameters.put("URL_IMAGEN",pathLocation+ApplicationConstants.LOGO_EMPRESA );
            JasperPrint jasperPrint;
            String locationFile = pathLocation+pathJasperReport;
            System.out.println("Cargando desde: " + locationFile);
            if (locationFile == null) {
                throw new JasperPrintUtilException(MESSAGE_NOT_FOUND_JASPER_FILE);
            }
            JasperReport masterReport = (JasperReport) JRLoader.loadObjectFromFile(locationFile);
            jasperPrint = JasperFillManager.fillReport(masterReport, parameters, this.connection);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pathLocation+namePDF);
            File file = new File(pathLocation+namePDF);
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            throw new JasperPrintUtilException(e.getMessage(),e);
        }
    }
    
    private void getConnection() throws BusinessException {
        
        if (this.connection != null) {
            return;
        }
        
        try {
            //obtenemos el driver de para mysql
            Class.forName(driver);
            //obtenemos la conexión
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Conexion a base de datos " + bd + ". listo");
            }
        
        } catch (SQLNonTransientConnectionException e) {
           System.out.println("la conexion se ha cerrado "+e);
           throw new DataOriginException(e.getMessage(),e);
        } catch (SQLException e) {
            System.out.println(e);
            throw new DataOriginException(e.getMessage(),e);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
            throw new DataOriginException(e.getMessage(),e);
        }catch (Exception e) {
            throw new DataOriginException(e.getMessage(),e);
        }
    }
    
    private final PropertiesService prop;
    private final String bd;
    private final String user; 
    private final String password;
    private final String url;
    private final String driver;
    private Connection connection;
    
}
