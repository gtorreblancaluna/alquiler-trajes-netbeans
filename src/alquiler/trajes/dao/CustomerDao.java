package alquiler.trajes.dao;

import alquiler.trajes.config.PersistenceManager;
import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.entity.Customer;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class CustomerDao implements Dao<Customer> {

    static EntityManager em = PersistenceManager.getInstance().createEntityManager();
    
    private CustomerDao () {}
    private static final CustomerDao SINGLE_INSTANCE = null;
        
    public static CustomerDao getInstance(){
        if (SINGLE_INSTANCE == null) {
            return new CustomerDao();
        }
        return SINGLE_INSTANCE;
    }

        
    @Override
    public List<Customer> getAll() throws BusinessException{
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.enabled = '1' ", Customer.class)               
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    @Override
    public Optional<Customer> get(long id) {
        return Optional.ofNullable(em.find(Customer.class, id));
    }

    @Override
    public void save(Customer user) {
        executeInsideTransaction(entityManager -> entityManager.persist(user));
    }

    @Override
    public void update(Customer user) {
        executeInsideTransaction(entityManager -> entityManager.merge(user));
    }

    @Override
    public void delete(Customer t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
}
