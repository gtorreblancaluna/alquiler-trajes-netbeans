package alquiler.trajes.ticket;

import static alquiler.trajes.constant.ApplicationConstants.DECIMAL_FORMAT;
import alquiler.trajes.entity.DetailEvent;
import alquiler.trajes.entity.Event;
import alquiler.trajes.util.UtilityTicket;
import java.text.DecimalFormat;
import java.util.List;
import org.apache.log4j.Logger;

public class EventTicket extends TicketTemplate {
    
    private final Event event;
    private final List<DetailEvent> detailEvent;
    private final Float subtotal;
    private final Float payments;
    private final int TOTAL_LINES_DESCRIPTION = 6;
    private final String CUSTOMER_LABEL = "Cliente:";
    private final String ATTENDED_LABEL = "Atendió:";
    private final String ANTICIPO_LABEL = "A.C. ";
    private final String HR_LABEL = " Hr";
    private final String SUBTOTAL_LABEL = "Subtotal:";
    private final String TOTAL_LABEL = "Total:";
    private final String PAYMENTS_LABEL = "Abonos:";
    
    private static final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
    
    private static final Logger log = Logger.getLogger(EventTicket.class.getName());
    
    public EventTicket (final Event event,List<DetailEvent> detailEvent,Float subtotal,Float payments) {
        this.event = event;
        this.detailEvent = detailEvent;
        this.subtotal = subtotal;
        this.payments = payments;
    }

    @Override
    protected void buildBody() {
        
        StringBuilder builder = new StringBuilder();
        builder
            .append("Folio: ")
                .append(event.getId())
                .append(LINE_BREAK)
            .append("Tipo: ")
                .append(event.getCatalogTypeEvent().getName())
                .append(LINE_BREAK)
            .append("Estatus: ")
                .append(event.getCatalogStatusEvent().getName())
                .append(LINE_BREAK)
            .append(SUBTOTAL_LABEL)
                .append(UtilityTicket.alignRight(
                        decimalFormat.format(subtotal),(LENGHT_BY_LINE-SUBTOTAL_LABEL.length())))
                .append(LINE_BREAK)
            .append(PAYMENTS_LABEL)
                .append(UtilityTicket.alignRight(
                        decimalFormat.format(payments),(LENGHT_BY_LINE-PAYMENTS_LABEL.length())))
                .append(LINE_BREAK)
            .append(TOTAL_LABEL)
                .append(UtilityTicket.alignRight(
                        decimalFormat.format(subtotal-payments),(LENGHT_BY_LINE-TOTAL_LABEL.length())))
                .append(LINE_BREAK)
            .append(LINE)
            .append(LINE_BREAK);
            
        final String customerNameWithLabel = String.format(
                        STRING_FORMAT_THREE_PARAMETERS,
                        CUSTOMER_LABEL,
                        event.getCustomer().getName(),
                        event.getCustomer().getLastName());
        
                if ( customerNameWithLabel.length() > LENGHT_BY_LINE) {
                    builder
                            .append(customerNameWithLabel.substring(0, LENGHT_BY_LINE));
                } else {
                    builder
                            .append(customerNameWithLabel);
                            
                }
                builder.append(LINE_BREAK);
            
             builder.append("Entrega: ")
                    .append(dateFormat.format(event.getDeliveryDate()))
                    .append(" ")
                    .append(event.getDeliveryHour())
                    .append(HR_LABEL)
                    .append(LINE_BREAK)
            .append("Devolución: ")
                    .append(dateFormat.format(event.getReturnDate()))
                    .append(" ")
                    .append(event.getReturnHour())
                    .append(HR_LABEL)
                    .append(LINE_BREAK);
             
        final String userNameWithLabel = String.format(
                        STRING_FORMAT_THREE_PARAMETERS,
                        ATTENDED_LABEL,
                        event.getUser().getName(),
                        event.getUser().getLastName());
        
                if (userNameWithLabel.length() > LENGHT_BY_LINE) {
                    builder
                            .append(userNameWithLabel.substring(0, LENGHT_BY_LINE));
                } else {
                    builder
                            .append(userNameWithLabel);
                }
            builder.append(LINE_BREAK);
            builder.append("Fecha elaboración: ")
                    .append(dateFormat.format(event.getCreatedAt()))
                    .append(LINE_BREAK);

            StringBuilder descriptionBuilder = new StringBuilder();
            UtilityTicket.appendLargeStringToStringBuilderByLengthToPrinterTermica(
                    descriptionBuilder, 
                    LENGHT_BY_LINE, 
                    event.getDescription(), 
                    LINE_BREAK,
                    TOTAL_LINES_DESCRIPTION
            );
            
            
            

            builder.append("Descripción:")
                    .append(LINE_BREAK)
                    .append(descriptionBuilder.toString())
                    .append(LINE).append(LINE_BREAK);
            
            int lines = 0;
            for (DetailEvent detail : detailEvent) {
                if (detail.getNameOfAggregate().length() > LENGHT_BY_LINE) {
                    builder
                            .append(detail.getNameOfAggregate().substring(0, LENGHT_BY_LINE))
                            .append(LINE_BREAK);
                } else {
                    builder
                            .append(detail.getNameOfAggregate())
                            .append(LINE_BREAK);
                }
                
                if (detail.getItems().length() > LENGHT_BY_LINE) {
                    builder
                            .append(detail.getItems().substring(0, LENGHT_BY_LINE))
                            .append(LINE_BREAK);
                } else {
                    builder
                            .append(detail.getItems())
                            .append(LINE_BREAK);
                }
                builder
                        .append("Imp. ")
                        .append(String.format(STRING_FORMAT_NUMBER,decimalFormat.format(detail.getUnitPrice())))
                        .append(" |  ");
                if (detail.getAdvancePayment() > 0) {
                    builder
                            .append(ANTICIPO_LABEL)
                            .append(String.format(STRING_FORMAT_NUMBER,decimalFormat.format(detail.getAdvancePayment())));
                } else {
                    builder
                            .append(ANTICIPO_LABEL)
                            .append(String.format(STRING_FORMAT_NUMBER,decimalFormat.format(0)));
                }
                builder.append(LINE_BREAK);
                lines ++;
                if (lines < detailEvent.size()) {
                    builder.append(SUB_LINE_POINTS)
                            .append(LINE_BREAK);
                }
            } // end for
        
        this.setBody(builder.toString());
    }
    
}
