package alquiler.trajes.util;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.DATE_FORMAT_FOR_SQL_QUERY;
import static alquiler.trajes.constant.ApplicationConstants.START_DAY_HOUR_MINUTES;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.apache.commons.lang3.time.FastDateFormat;


public class UtilityDate {

    private UtilityDate () {}
    
    private static final Logger logger = Logger.getLogger(UtilityDate.class.getName());
    
    private static UtilityDate SINGLE_INSTANCE;
    
    public static synchronized UtilityDate getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            logger.log(Level.INFO,"Creating new instance from Utility Date...");
            SINGLE_INSTANCE = new UtilityDate();
        }
        return SINGLE_INSTANCE;
    } 
   
   public String getStartDateFormatSqlQuery (Date date) {
       
       if (date != null) {
           return FastDateFormat.getInstance(DATE_FORMAT_FOR_SQL_QUERY)
                   .format(date)+ START_DAY_HOUR_MINUTES;
       }
       
       return null;
       
   }
   
    public String getEndDateFormatSqlQuery (Date date) {
   
       String dateInString = null;
       
       if (date != null) {
           dateInString = FastDateFormat.getInstance(DATE_FORMAT_FOR_SQL_QUERY)
                   .format(date)+ ApplicationConstants.END_DAY_HOUR_MINUTES;
       }
       
       return dateInString;
       
   }
      
    public Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());    
    }

    
}
