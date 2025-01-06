package alquiler.trajes.service;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.constant.PropertyConstant;
import alquiler.trajes.dao.CatalogStatusEventDao;
import alquiler.trajes.entity.CatalogStatusEvent;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import alquiler.trajes.util.PropertySystemUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import javax.persistence.NoResultException;

public final class CatalogStatusEventService {
    
   private CatalogStatusEventService(){}
    
   private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CatalogTypeEventService.class.getName());
                
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
            logger.log(Level.SEVERE,e.getMessage(),e);
            throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
        }

    }
    
    public List<CatalogStatusEvent> getAll () throws BusinessException {
        
        List<CatalogStatusEvent> list = new ArrayList<>();
        
        try {
            
            String catalogInString = PropertySystemUtil.get(PropertyConstant.CATALOG_STATUS_LIST);
            
            Gson gson = new Gson();
            
            CatalogStatusEvent[] catalogTypeEvents = 
                    gson.fromJson(catalogInString, CatalogStatusEvent[].class);           
            
            list.addAll(Arrays.asList(catalogTypeEvents));
            
            if (list.isEmpty() || list.size() <= 0) {
                logger.log(Level.INFO,"Getting catalog status event from database.");
                list = catalogStatusEventDao.getAll();
                PropertySystemUtil.save(PropertyConstant.CATALOG_STATUS_LIST.getKey(),gson.toJson(list));
            }
            
        } catch (IOException ioe) {
            logger.log(Level.SEVERE,ioe.getMessage(),ioe);
        }
        
        return list;

    }
    
}
