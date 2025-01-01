package alquiler.trajes.util;

import static alquiler.trajes.constant.ApplicationConstants.DATE_LARGE;
import static alquiler.trajes.constant.ApplicationConstants.DECIMAL_FORMAT;
import static alquiler.trajes.constant.ApplicationConstants.LOCALE_COUNTRY;
import static alquiler.trajes.constant.ApplicationConstants.LOCALE_LANGUAGE;
import alquiler.trajes.model.params.result.EventResult;
import alquiler.trajes.service.CatalogStatusEventService;
import alquiler.trajes.tables.TableConsultEvents;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.time.FastDateFormat;

public final class UtilityTable {
    
    private UtilityTable () {
        throw new IllegalStateException("UtilityTable");
    }
    
    private static final Locale locale = new Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY);
    private static final FastDateFormat fastDateFormatLarge = FastDateFormat.getInstance(DATE_LARGE,locale);
    private static final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
    
    
    public static void fillTableEvents (
            final TableConsultEvents tableConsultEvents,final List<EventResult> results) {
        
        tableConsultEvents.format();
        DefaultTableModel temp = (DefaultTableModel) tableConsultEvents.getModel();
        for (EventResult result : results) {
            Object row[] = {
                false,
                result.getId(),
                result.getDescription(),
                result.getCustomer(),
                result.getCustomerPhones(),
                fastDateFormatLarge.format(result.getDeliveryDate()) + " "+ result.getDeliveryHour(),
                fastDateFormatLarge.format(result.getReturnDate())+ " " + result.getReturnHour(),
                result.getTypeEvent(),
                result.getStatusEvent(),
                decimalFormat.format(result.getPayments()),
                decimalFormat.format(result.getSubTotal()),
                decimalFormat.format(result.getSubTotal() - result.getPayments())
            };
            temp.addRow(row);
        }
    
    }
    

    
}
