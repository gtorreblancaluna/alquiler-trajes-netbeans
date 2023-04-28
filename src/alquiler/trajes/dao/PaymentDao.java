package alquiler.trajes.dao;

import alquiler.trajes.config.PersistenceManager;
import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.entity.Payment;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class PaymentDao implements Dao<Payment> {

    static EntityManager em = PersistenceManager.getInstance().createEntityManager();
    
    private PaymentDao () {
        
    }
    private static final PaymentDao SINGLE_INSTANCE = null;
        
    public static PaymentDao getInstance(){
        
        if (SINGLE_INSTANCE == null) {
            return new PaymentDao();
        }
        return SINGLE_INSTANCE;
    }

    @Override
    public List<Payment> getAll() throws BusinessException{
        try {
            return em.createQuery("SELECT p FROM Payment p WHERE p.enabled = '1' ", Payment.class)               
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    public List<Payment> getAll(Long eventId) throws BusinessException{
        try {
            return em.createQuery("SELECT p FROM Payment p WHERE p.enabled = '1' "
                    + "AND p.event.id = :eventId"
                    + "", Payment.class)
                    .setParameter("eventId", eventId)
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    @Override
    public Optional<Payment> get(long id) {
        return Optional.ofNullable(em.find(Payment.class, id));
    }

    @Override
    public void save(Payment t) {
        executeInsideTransaction(entityManager -> entityManager.persist(t));
    }

    @Override
    public void update(Payment t) {
        executeInsideTransaction(entityManager -> entityManager.merge(t));
    }

    private void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit(); 
        }
        catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(Payment t) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
