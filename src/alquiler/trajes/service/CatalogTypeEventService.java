package alquiler.trajes.service;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.constant.PropertyConstant;
import alquiler.trajes.dao.CatalogTypeEventDao;
import alquiler.trajes.entity.CatalogTypeEvent;
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
import java.util.logging.Logger;
import javax.persistence.NoResultException;

public final class CatalogTypeEventService {
    
    private CatalogTypeEventService(){}
    
    private static final Logger logger = Logger.getLogger(CatalogTypeEventService.class.getName());
                
    private final CatalogTypeEventDao catalogTypeEventDao = CatalogTypeEventDao.getInstance();
            
    private static CatalogTypeEventService SINGLE_INSTANCE;
        
   public static synchronized CatalogTypeEventService getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new CatalogTypeEventService();
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
            logger.log(Level.SEVERE,e.getMessage(),e);
            throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
        }

    }
    
    public List<CatalogTypeEvent> getAll () throws BusinessException {
        
        List<CatalogTypeEvent> list = new ArrayList<>();
        try {
            
            String catalogInString = PropertySystemUtil.get(PropertyConstant.CATALOG_TYPE_LIST);
            
            Gson gson = new Gson();
            
            CatalogTypeEvent[] catalogTypeEvents = 
                    gson.fromJson(catalogInString, CatalogTypeEvent[].class);           
            
            list.addAll(Arrays.asList(catalogTypeEvents));
            
            if (list.isEmpty() || list.size() <= 0) {
                logger.log(Level.INFO,"Getting catalog type event from database.");
                list = catalogTypeEventDao.getAll();
                PropertySystemUtil.save(PropertyConstant.CATALOG_TYPE_LIST.getKey(),gson.toJson(list));
            }
            
        } catch (IOException ioe) {
            logger.log(Level.SEVERE,ioe.getMessage(),ioe);
        }
        
        return list;
    }
    
}
