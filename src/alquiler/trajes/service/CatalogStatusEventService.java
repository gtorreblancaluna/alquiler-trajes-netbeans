package alquiler.trajes.service;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.dao.CatalogStatusEventDao;
import alquiler.trajes.entity.CatalogStatusEvent;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import org.apache.log4j.Logger;

public final class CatalogStatusEventService {
    
   private CatalogStatusEventService(){}
    
   private static final Logger log = Logger.getLogger(CatalogStatusEventService.class.getName());
                
   private final CatalogStatusEventDao catalogStatusEventDao = CatalogStatusEventDao.getInstance();
            
   private static CatalogStatusEventService SINGLE_INSTANCE;
        
   public static synchronized CatalogStatusEventService getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new CatalogStatusEventService();
        }
        return SINGLE_INSTANCE;
    }    
    
    
    public CatalogStatusEvent findById (final Long id)throws BusinessException {

        try {
            
            Optional<CatalogStatusEvent> opCatalogTypeEvent = catalogStatusEventDao.get(id);
            if (!opCatalogTypeEvent.isPresent()) {
                throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
            }
            return opCatalogTypeEvent.get();
        } catch (NoResultException e) {
            log.error(e);
            throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
        }

    }
    
    public List<CatalogStatusEvent> getAll () throws BusinessException {
        return catalogStatusEventDao.getAll();
    }
    
}
