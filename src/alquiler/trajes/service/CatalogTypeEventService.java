package alquiler.trajes.service;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.dao.CatalogTypeEventDao;
import alquiler.trajes.entity.CatalogTypeEvent;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import org.apache.log4j.Logger;

public class CatalogTypeEventService {
    
    private CatalogTypeEventService(){}
    
    private static final Logger log = Logger.getLogger(CatalogTypeEventService.class.getName());
                
    private CatalogTypeEventDao catalogTypeEventDao = CatalogTypeEventDao.getInstance();
            
    private static final CatalogTypeEventService SINGLE_INSTANCE = null;
        
    public static CatalogTypeEventService getInstance(){
        
        if (SINGLE_INSTANCE == null) {
            return new CatalogTypeEventService();
        }
        return SINGLE_INSTANCE;
    }    
    
    
    public CatalogTypeEvent findById (final Long id)throws BusinessException {

        try {
            
            Optional<CatalogTypeEvent> opCatalogTypeEvent = catalogTypeEventDao.get(id);
            if (!opCatalogTypeEvent.isPresent()) {
                throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
            }
            return opCatalogTypeEvent.get();
        } catch (NoResultException e) {
            log.error(e);
            throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
        }

    }
    
    public List<CatalogTypeEvent> getAll () throws BusinessException {
        return catalogTypeEventDao.getAll();
    }
    
}
