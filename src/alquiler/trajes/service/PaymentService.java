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

public final class PaymentService {
    
    private PaymentService(){}
    
    private static final Logger log = Logger.getLogger(PaymentService.class.getName());
                
    private PaymentDao paymentDao = PaymentDao.getInstance();
            
    private static PaymentService SINGLE_INSTANCE;
        
    public static synchronized PaymentService getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new PaymentService();
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
    
    public List<Payment> getAll (final Long eventId) throws BusinessException {
        return paymentDao.getAll(eventId);
    }
    
    public Float getPaymentsByEvent (final Long eventId) throws BusinessException {
        List<Payment> payments = getAll(eventId);
        Float sumPayment = 0F;
        for (Payment payment : payments) {
            if (payment.getPayment() != null) {
                sumPayment += payment.getPayment();
            }
        }
        return sumPayment;
    }
    
}
