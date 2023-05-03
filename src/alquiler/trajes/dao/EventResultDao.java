
package alquiler.trajes.dao;

import alquiler.trajes.config.PersistenceManager;
import alquiler.trajes.model.params.EventParameter;
import alquiler.trajes.model.params.result.EventResult;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;


public class EventResultDao extends ResultDao {
    
    static EntityManager em = PersistenceManager.getInstance().createEntityManager();
    
    private EventResultDao () {}
    private static final EventResultDao SINGLE_INSTANCE = null;
        
        public static EventResultDao getInstance(){
        
        if (SINGLE_INSTANCE == null) {
            return new EventResultDao();
        }
        return SINGLE_INSTANCE;
    }

    public List<EventResult> getResult (final EventParameter parameter) {
        
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ")
                .append("event.id,")
                .append("event.description,")
                .append("CONCAT(customer.name,' ',customer.last_name) AS customer,")
                .append("CONCAT(customer.phone_number1,',',customer.phone_number2,',',customer.phone_number3) AS customer_phones,")
                .append("event.delivery_date,")
                .append("event.delivery_hour,")
                .append("event.return_date,")
                .append("event.return_hour,")
                .append("type.name AS type_event,")
                .append("status.name AS status_event,")
                .append("(")
                    .append("SELECT COALESCE(SUM(p.payment),0) ")
                    .append("FROM payments p ")
                    .append("WHERE p.enabled = 1 ")
                    .append("AND p.event_id = event.id ")
                .append(") AS payments,")
                
                .append("(")
                    .append("SELECT COALESCE(SUM(d.unit_price),0) ")
                    .append("FROM detail_event d ")
                    .append("WHERE d.event_id = event.id ")
                .append(") AS sub_total,")
                
                .append("event.created_at ")
                .append("FROM events event ")
                .append("INNER JOIN customers customer ON (event.customer_id = customer.id) ")
                .append("INNER JOIN catalog_type_event type ON (type.id = event.catalog_type_event_id) ")
                .append("INNER JOIN catalog_status_event status ON (status.id = event.catalog_status_event_id) ")
                .append("WHERE event.enabled = 1 ");
        
        if (parameter.getEventId() != null && !parameter.getEventId().equals(0L)) {
            builder.append("AND event.id = ").append(parameter.getEventId()).append(" ");
        } else {                
            if (parameter.getInitDeliveryDate() != null && 
                    !parameter.getInitDeliveryDate().isEmpty() && 
                    (parameter.getEndDeliveryDate() == null || parameter.getEndDeliveryDate().isEmpty())
                    ) {
               // fecha de entrega viene con datos y fecha de devolucion esta vacia 
                builder.append("AND event.delivery_date >= '")
                        .append(parameter.getInitDeliveryDate()).append("' ");

            } else if (parameter.getInitDeliveryDate() != null && 
                    !parameter.getInitDeliveryDate().isEmpty() && 
                    parameter.getEndDeliveryDate() != null &&
                    !parameter.getEndDeliveryDate().isEmpty()                
                    ) {
                
                // entre fecha de entrega
                builder.append("AND event.delivery_date BETWEEN '")
                        .append(parameter.getInitDeliveryDate()).append("' ")
                        .append("' AND '")
                        .append(parameter.getEndDeliveryDate()).append("' ")
                        ;
            }
            
            if (parameter.getInitReturnDate()!= null && 
                    !parameter.getInitReturnDate().isEmpty() && 
                    parameter.getEndReturnDate() != null &&
                    !parameter.getEndReturnDate().isEmpty()                
                    ) {
               // entre fecha de devolucion
                builder.append("AND event.return_date BETWEEN '")
                        .append(parameter.getInitReturnDate())
                        .append("' AND '")
                        .append(parameter.getEndReturnDate()).append("' ")
                        ;

            }

            if (parameter.getDescription() != null && !parameter.getDescription().isEmpty()) {
                builder.append("AND UPPER(event.description) LIKE '%")
                        .append(parameter.getDescription().toUpperCase().trim())
                        .append("%'");
            }
            
            if (parameter.getCustomerName()!= null && !parameter.getCustomerName().isEmpty()) {
                builder.append("AND UPPER(CONCAT(customer.name,' ',customer.last_name)) LIKE '%")
                        .append(parameter.getCustomerName().toUpperCase().trim())
                        .append("%'");
            }
        }

        builder.append("ORDER BY event.delivery_date ");
        
        Query query = em.createNativeQuery(builder.toString());
        List<EventResult> list = getResultList(query, EventResult.class);
        return list;
    }
    
    
    
}
