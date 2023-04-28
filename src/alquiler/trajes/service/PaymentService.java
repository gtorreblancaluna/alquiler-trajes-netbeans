package alquiler.trajes.service;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.dao.PaymentDao;
import alquiler.trajes.entity.Payment;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import alquiler.trajes.form.login.LoginForm;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import org.apache.log4j.Logger;

public class PaymentService {
    
    private PaymentService(){}
    
    private static final Logger log = Logger.getLogger(PaymentService.class.getName());
                
    private PaymentDao paymentDao = PaymentDao.getInstance();
            
    private static final PaymentService SINGLE_INSTANCE = null;
        
    public static PaymentService getInstance(){
        
        if (SINGLE_INSTANCE == null) {
            return new PaymentService();
        }
        return SINGLE_INSTANCE;
    }   
    
    public void save (Payment payment) throws BusinessException {
        
        final Date now = new Date();
        
        if (payment.getId() == null || payment.getId().equals(0L)) {
            payment.setId(null);
            payment.setCreatedAt(now);
            payment.setUpdatedAt(now);
            payment.setEnabled(Boolean.TRUE);
            payment.setUser(LoginForm.userSession);
            paymentDao.save(payment);
        } else {
            payment.setUpdatedAt(now);
            paymentDao.update(payment);         
        } 
    }
    
    public void save (List<Payment> payments) throws BusinessException {
        
        for (Payment payment : payments) {
            save(payment);
        }
    }
    
    public Payment findById (final Long id)throws BusinessException {

        try {
            
            Optional<Payment> op = paymentDao.get(id);
            if (!op.isPresent()) {
                throw new NoDataFoundException(ApplicationConstants.NO_DATA_FOUND_EXCEPTION);
            }
            return op.get();
        } catch (NoResultException e) {
            log.error(e);
            throw new NoDataFoundException(ApplicationConstants.NO_DATA_FOUND_EXCEPTION);
        }

    }
    
    public List<Payment> getAll (Long eventId) throws BusinessException {
        return paymentDao.getAll(eventId);
    }
    
}
