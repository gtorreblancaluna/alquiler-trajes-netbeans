package alquiler.trajes.util;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.MESSAGE_NOT_FOUND_JASPER_FILE;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.DataOriginException;
import alquiler.trajes.exceptions.JasperPrintUtilException;
import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;

public final class JasperPrintUtil {
    
    private JasperPrintUtil(){
        prop = PropertiesService.getInstance();
        this.bd = prop.getProperty("db.database.name");
        this.user = prop.getProperty("db.username");
        this.password = prop.getProperty("db.password");
        this.url = prop.getProperty("db.url");
        this.driver = prop.getProperty("db.driver");
    }
    
    private static final Logger log = Logger.getLogger(JasperPrintUtil.class.getName());
    private static JasperPrintUtil SINGLE_INSTANCE;
        
    public static synchronized JasperPrintUtil getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new JasperPrintUtil();
        }
        return SINGLE_INSTANCE;
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
