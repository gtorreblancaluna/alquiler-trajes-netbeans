package alquiler.trajes.service;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.dao.GeneralInfoDao;
import alquiler.trajes.entity.GeneralInfo;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.Optional;
import javax.persistence.NoResultException;
import org.apache.log4j.Logger;

public class GeneralInfoService {
    
    private GeneralInfoService(){}
    
    private static final Logger log = Logger.getLogger(GeneralInfoService.class.getName());
                
    private GeneralInfoDao generalInfoDao = GeneralInfoDao.getInstance();
            
    private static final GeneralInfoService SINGLE_INSTANCE = null;
        
    public static GeneralInfoService getInstance(){
        
        if (SINGLE_INSTANCE == null) {
            return new GeneralInfoService();
        }
        return SINGLE_INSTANCE;
    }    
    
    
    public String getByKey (final String key)throws BusinessException {

        try {
            
            Optional<GeneralInfo> op = generalInfoDao.getByKey(key);
            if (!op.isPresent()) {
                throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
            }
            return op.get().getValue();
        } catch (NoResultException e) {
            log.error(e);
            throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
        }

    }
    
    
}
