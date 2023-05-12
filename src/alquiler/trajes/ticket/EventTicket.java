package alquiler.trajes.ticket;

import static alquiler.trajes.constant.ApplicationConstants.DECIMAL_FORMAT;
import alquiler.trajes.entity.DetailEvent;
import alquiler.trajes.entity.Event;
import java.text.DecimalFormat;
import java.util.List;
import org.apache.log4j.Logger;


public class EventTicket extends TicketTemplate {
    
    private final Event event;
    private final List<DetailEvent> detailEvent;
    private final Float subtotal;
    private final Float payments;
    private final int TOTAL_LINES_DESCRIPTION = 6;
    
    private static final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
    
    private static final Logger log = Logger.getLogger(EventTicket.class.getName());
    
    public EventTicket (final Event event,List<DetailEvent> detailEvent,Float subtotal,Float payments) {
        this.event = event;
        this.detailEvent = detailEvent;
        this.subtotal = subtotal;
        this.payments = payments;
    }

    @Override
    public void buildBody() {
        StringBuilder builder = new StringBuilder();
        builder
            .append("Folio: ").append(event.getId()).append(LINE_BREAK)
            .append("Tipo: ").append(event.getCatalogTypeEvent().getName()).append(LINE_BREAK)
            .append("Estatus: ").append(event.getCatalogStatusEvent().getName()).append(LINE_BREAK)
            .append("Subtotal:             ").append(String.format(STRING_FORMAT_NUMBER,decimalFormat.format(subtotal))).append(LINE_BREAK)
            .append("Abonos:               ").append(String.format(STRING_FORMAT_NUMBER,decimalFormat.format(payments))).append(LINE_BREAK)
            .append("Total:                ").append(String.format(STRING_FORMAT_NUMBER,decimalFormat.format(subtotal-payments))).append(LINE_BREAK)
            .append(LINE).append(LINE_BREAK);
            
                if ( String.format(STRING_FORMAT_THREE_PARAMETERS,"Cliente:",event.getCustomer().getName(),event.getCustomer().getLastName()).length() > LENGHT_BY_LINE) {
                    builder.append(String.format(STRING_FORMAT_THREE_PARAMETERS,"Cliente:",event.getCustomer().getName(),event.getCustomer().getLastName()).substring(0, LENGHT_BY_LINE)).append(LINE_BREAK);
                } else {
                    builder.append(String.format(STRING_FORMAT_THREE_PARAMETERS,"Cliente:",event.getCustomer().getName(),event.getCustomer().getLastName())).append(LINE_BREAK);
                }
            builder.append("Entrega: ").append(dateFormat.format(event.getDeliveryDate())).append(" ").append(event.getDeliveryHour()).append(LINE_BREAK)
            .append("Devolución: ").append(dateFormat.format(event.getReturnDate())).append(" ").append(event.getReturnHour()).append(LINE_BREAK);

                if ( String.format(STRING_FORMAT_THREE_PARAMETERS,"Atendió:",event.getUser().getName(),event.getUser().getLastName()).length() > LENGHT_BY_LINE) {
                    builder.append(String.format(STRING_FORMAT_THREE_PARAMETERS,"Atendió:",event.getUser().getName(),event.getUser().getLastName()).substring(0, LENGHT_BY_LINE)).append(LINE_BREAK);
                } else {
                    builder.append(String.format(STRING_FORMAT_THREE_PARAMETERS,"Atendió:",event.getUser().getName(),event.getUser().getLastName())).append(LINE_BREAK);
                }
            builder.append("Fecha elaboración: ").append(dateFormat.format(event.getCreatedAt())).append(LINE_BREAK);

            StringBuilder description = new StringBuilder();
            if (event.getDescription().length() > LENGHT_BY_LINE) {
                int lines = event.getDescription().length() / LENGHT_BY_LINE;
                int beginLenght = 0;
                int discountLines = event.getDescription().length();
                
                for (int i = 0; i <= lines ; i++) {
                    try {
                        if (discountLines < LENGHT_BY_LINE) {
                            description.append(event.getDescription().substring(beginLenght, (beginLenght+discountLines)));
                        } else {
                            description.append(event.getDescription().substring(beginLenght, (beginLenght+LENGHT_BY_LINE)));
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        log.error(e);
                        break;
                    }
                    beginLenght += LENGHT_BY_LINE;
                    discountLines -= LENGHT_BY_LINE;
                    description.append(LINE_BREAK);
                    if (i > TOTAL_LINES_DESCRIPTION){break;}
                }
            } else {
                description.append(event.getDescription());
            }

            builder.append("Descripción:").append(LINE_BREAK).append(description.toString())
                    .append(LINE).append(LINE_BREAK);
            
            int lines = 0;
            for (DetailEvent detail : detailEvent) {
                if (detail.getNameOfAggregate().length() > LENGHT_BY_LINE) {
                    builder.append(detail.getNameOfAggregate().substring(0, LENGHT_BY_LINE)).append(LINE_BREAK);
                } else {
                    builder.append(detail.getNameOfAggregate()).append(LINE_BREAK);
                }
                
                if (detail.getItems().length() > LENGHT_BY_LINE) {
                    builder.append(detail.getItems().substring(0, LENGHT_BY_LINE)).append(LINE_BREAK);
                } else {
                    builder.append(detail.getItems()).append(LINE_BREAK);
                }
                lines ++;
                if (lines < detailEvent.size()) {
                    builder.append(SUB_LINE_POINTS).append(LINE_BREAK);
                }
            }
        
        this.setBody(builder.toString());
    }
    
}
