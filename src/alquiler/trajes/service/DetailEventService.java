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

public class DetailEventService {
    
    private DetailEventService(){}
    
    private static final Logger log = Logger.getLogger(DetailEventService.class.getName());
                
    private DetailEventDao detailEventDao = DetailEventDao.getInstance();
            
    private static final DetailEventService SINGLE_INSTANCE = null;
        
    public static DetailEventService getInstance(){
        
        if (SINGLE_INSTANCE == null) {
            return new DetailEventService();
        }
        return SINGLE_INSTANCE;
    }   
    
    public void save (List<DetailEvent> details) throws BusinessException {
        
        detailEventDao.deleteAllByEvent(details.get(0).getId());
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
    
    public List<DetailEvent> getAll (Long eventId) throws BusinessException {
        return detailEventDao.getAll(eventId);
    }
    
}
