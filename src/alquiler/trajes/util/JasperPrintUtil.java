package alquiler.trajes.util;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.DATE_LARGE;
import static alquiler.trajes.constant.ApplicationConstants.DESCRIPTION_FORMAT_24_HRS;
import static alquiler.trajes.constant.ApplicationConstants.LOCALE_COUNTRY;
import static alquiler.trajes.constant.ApplicationConstants.LOCALE_LANGUAGE;
import static alquiler.trajes.constant.ApplicationConstants.MESSAGE_NOT_FOUND_JASPER_FILE;
import static alquiler.trajes.constant.ApplicationConstants.PATH_NAME_EVENT_REPORT_VERTICAL_A5;
import static alquiler.trajes.constant.ApplicationConstants.PDF_NAME_EVENT_REPORT_VERTICAL_A5;
import alquiler.trajes.constant.JasperPrintConstants;
import alquiler.trajes.constant.PropertyConstant;
import alquiler.trajes.entity.Event;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.DataOriginException;
import alquiler.trajes.exceptions.JasperPrintUtilException;
import alquiler.trajes.service.DetailEventService;
import alquiler.trajes.service.PaymentService;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

public final class JasperPrintUtil {
    
    private static final Logger log = Logger.getLogger(JasperPrintUtil.class.getName());
    private static JasperPrintUtil SINGLE_INSTANCE;
    private final FastDateFormat fastDateFormatLarge;
    private static final DecimalFormat decimalFormat = Utility.getDecimalFormat();
    private final Locale locale;
    private final PaymentService paymentService;
    private final DetailEventService detailEventService;
    private final PropertiesService prop;
    private final String bd;
    private final String user; 
    private final String password;
    private final String url;
    private final String driver;
    private Connection connection;
    
    private JasperPrintUtil(){
        
        prop = PropertiesService.getInstance();
        this.bd = prop.getProperty(ApplicationConstants.DATABASE_NAME_PROPERTY);
        this.user = prop.getProperty(ApplicationConstants.DATABASE_USERNAME_PROPERTY);
        this.password = prop.getProperty(ApplicationConstants.DATABASE_PASSWORD_PROPERTY);
        this.url = prop.getProperty(ApplicationConstants.DATABASE_URL_PROPERTY);
        this.driver = prop.getProperty(ApplicationConstants.DATABASE_DRIVER_PROPERTY);
        locale = new Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY);
        fastDateFormatLarge = FastDateFormat.getInstance(DATE_LARGE,locale);
        paymentService = PaymentService.getInstance();
        detailEventService = DetailEventService.getInstance();
    }
    

        
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
            
            String locationFile = pathLocation+PATH_NAME_EVENT_REPORT_VERTICAL_A5;
            if (locationFile.isBlank()) {
                throw new JasperPrintUtilException(MESSAGE_NOT_FOUND_JASPER_FILE);
            }
            
            parameters.put(JasperPrintConstants.URL_SUB_REPORT_CONSULTA, pathLocation+"/");
            parameters.put(JasperPrintConstants.URL_IMAGEN,pathLocation+ApplicationConstants.LOGO_EMPRESA );
            
            parameters.put(JasperPrintConstants.ID, event.getId().toString());
            parameters.put(JasperPrintConstants.FOLIO, event.getId().toString());
            parameters.put(JasperPrintConstants.USER_NAME, event.getUser().getName());
            parameters.put(JasperPrintConstants.CUSTOMER_NAME, event.getCustomer().getName()+" "+event.getCustomer().getLastName());
            parameters.put(JasperPrintConstants.TYPE_EVENT, event.getCatalogTypeEvent().getName());
            parameters.put(JasperPrintConstants.STATUS_EVENT, event.getCatalogStatusEvent().getName());
            parameters.put(JasperPrintConstants.DELIVERY_DATE, fastDateFormatLarge.format(event.getDeliveryDate())+" "+event.getDeliveryHour()+DESCRIPTION_FORMAT_24_HRS);
            parameters.put(JasperPrintConstants.RETURN_DATE, fastDateFormatLarge.format(event.getReturnDate())+" "+event.getReturnHour()+DESCRIPTION_FORMAT_24_HRS);
            parameters.put(JasperPrintConstants.CREATED_AT_DATE, fastDateFormatLarge.format(event.getCreatedAt()));
            parameters.put(JasperPrintConstants.DESCRIPTION, event.getDescription());
            parameters.put(JasperPrintConstants.PAYMENTS, decimalFormat.format(sumPayment));
            parameters.put(JasperPrintConstants.SUBTOTAL, decimalFormat.format(sumDetail));
            parameters.put(JasperPrintConstants.TOTAL, decimalFormat.format(sumDetail-sumPayment));
            parameters.put(JasperPrintConstants.COMPANY_NAME, PropertySystemUtil.get(PropertyConstant.COMPANY_NAME_PDF_A5));
            parameters.put(JasperPrintConstants.INFO_FOOTER_PDF_A5, PropertySystemUtil.get(PropertyConstant.INFO_FOOTER_PDF_A5));
            parameters.put(JasperPrintConstants.IMPORTANT_INFO, PropertySystemUtil.get(PropertyConstant.IMPORTANT_INFO_PDF_A5));
            parameters.put(JasperPrintConstants.PRINT_DATE, ApplicationConstants.DATE_PRINT_JASPER+fastDateFormatLarge.format(new Date()));
                        
            JasperReport masterReport = (JasperReport) JRLoader.loadObjectFromFile(locationFile);
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parameters, this.connection);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pathLocation+PDF_NAME_EVENT_REPORT_VERTICAL_A5);
            File file = new File(pathLocation+PDF_NAME_EVENT_REPORT_VERTICAL_A5);
            Desktop.getDesktop().open(file);
        } catch (BusinessException | IOException | URISyntaxException | JRException e) {
            throw new JasperPrintUtilException(e.getMessage(),e);
        }
    }
    
    public void showPDF (final Map<String,Object> parameters,
            final String pathJasperReport,
            final String namePDF
            ) throws BusinessException {
        getConnection();
        try {
            String pathLocation = Utility.getPathLocation();
            

            parameters.put(JasperPrintConstants.URL_SUB_REPORT_CONSULTA, pathLocation+"/");
            parameters.put(JasperPrintConstants.URL_IMAGEN,pathLocation+ApplicationConstants.LOGO_EMPRESA );
            JasperPrint jasperPrint;
            String locationFile = pathLocation+pathJasperReport;
            
            if (locationFile.isBlank()) {
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
        }catch (ClassNotFoundException | SQLException e) {
            throw new DataOriginException(e.getMessage(),e);
        }
    }

    
}
