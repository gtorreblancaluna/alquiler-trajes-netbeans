package alquiler.trajes.service;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.dao.DetailEventDao;
import alquiler.trajes.entity.DetailEvent;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import org.apache.log4j.Logger;

public final class DetailEventService {
    
    private DetailEventService(){}
    
    private static final Logger log = Logger.getLogger(DetailEventService.class.getName());
                
    private DetailEventDao detailEventDao = DetailEventDao.getInstance();
            
    private static DetailEventService SINGLE_INSTANCE;
        
    public static synchronized DetailEventService getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new DetailEventService();
        }
        return SINGLE_INSTANCE;
    }
    
    public void deleteById (Long eventId) throws BusinessException {
        detailEventDao.deleteAllByEvent(eventId);
    }
    
    public void save (List<DetailEvent> details) throws BusinessException {

        details.stream().forEach(t -> {            
            detailEventDao.save(t);
        });
    }
    
    public DetailEvent findById (final Long id)throws BusinessException {

        try {
            
            Optional<DetailEvent> op = detailEventDao.get(id);
            if (!op.isPresent()) {
                throw new NoDataFoundException(ApplicationConstants.NO_DATA_FOUND_EXCEPTION);
            }
            return op.get();
        } catch (NoResultException e) {
            log.error(e);
            throw new NoDataFoundException(ApplicationConstants.NO_DATA_FOUND_EXCEPTION);
        }

    }
    
    public List<DetailEvent> getAll (final Long eventId) throws BusinessException {
        return detailEventDao.getAll(eventId);
    }
    
    public Float getSubtotalByEvent (final Long eventId) throws BusinessException {
        List<DetailEvent> detail = getAll(eventId);
        Float subTotal = 0F;
        for (DetailEvent det : detail) {
            if (det.getUnitPrice() != null){
                subTotal += det.getUnitPrice();
            }
        }
        return subTotal;
    }
    
}
