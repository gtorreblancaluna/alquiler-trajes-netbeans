package alquiler.trajes.service;

import alquiler.trajes.ticket.TicketTemplate;
import javax.print.PrintException;
import org.apache.log4j.Logger;

public class TicketService {
    
    private TicketService(){}
    
    private static final Logger log = Logger.getLogger(TicketService.class.getName());
                
            
    private static TicketService SINGLE_INSTANCE;
        
   public static synchronized TicketService getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new TicketService();
        }
        return SINGLE_INSTANCE;
    }
   
   public void printTicket (TicketTemplate ticketTemplate) throws PrintException {
       ticketTemplate.generateTicket();
   }
}
