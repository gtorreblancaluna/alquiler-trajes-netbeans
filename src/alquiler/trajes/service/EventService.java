package alquiler.trajes.service;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.dao.EventDao;
import alquiler.trajes.entity.DetailEvent;
import alquiler.trajes.entity.Event;
import alquiler.trajes.entity.Payment;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import alquiler.trajes.form.login.LoginForm;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import org.apache.log4j.Logger;

public final class EventService {
    
    private EventService(){}
    
    private static final Logger log = Logger.getLogger(EventService.class.getName());
                
    private final EventDao eventDao = EventDao.getInstance();
    private final DetailEventService detailEventService = DetailEventService.getInstance();
    private final PaymentService paymentService = PaymentService.getInstance();
            
    private static EventService SINGLE_INSTANCE;
        
    public static synchronized EventService getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new EventService();
        }
        return SINGLE_INSTANCE;
    }  
    
    public void save (Event event,
            List<DetailEvent> detailEvent,
            List<Payment> payments) throws BusinessException {
        
        final Date now = new Date();
        
        if (event.getId() == null) {
            event.setCreatedAt(now);
            event.setUpdatedAt(now);
            event.setUser(LoginForm.userSession);
            event.setEnabled(true);
        } else {
            event.setUpdatedAt(now);
            detailEventService.deleteById(event.getId());
        }
        eventDao.save(event);
        
        detailEvent.stream().forEach(t -> t.setEvent(event));
        payments.stream().forEach(t -> t.setEvent(event));
        
        
        detailEventService.save(detailEvent);
        paymentService.save(payments);

    }
    
    public Event findById (final Long id)throws BusinessException {

        try {
            
            Optional<Event> op = eventDao.get(id);
            if (!op.isPresent()) {
                throw new NoDataFoundException(ApplicationConstants.NO_DATA_FOUND_EXCEPTION);
            }
            return op.get();
        } catch (NoResultException e) {
            log.error(e);
            throw new NoDataFoundException(ApplicationConstants.NO_DATA_FOUND_EXCEPTION);
        }

    }
    
    public List<Event> getAll () throws BusinessException {
        return eventDao.getAll();
    }
    
}
