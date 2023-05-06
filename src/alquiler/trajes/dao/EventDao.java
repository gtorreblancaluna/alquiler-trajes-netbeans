package alquiler.trajes.dao;

import alquiler.trajes.config.PersistenceManager;
import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.entity.Event;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import alquiler.trajes.model.params.EventParameter;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public final class EventDao implements Dao<Event> {

    static EntityManager em = PersistenceManager.getInstance().createEntityManager();
    
    private EventDao () {
        
    }
    private static EventDao SINGLE_INSTANCE;
        
   public static synchronized EventDao getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new EventDao();
        }
        return SINGLE_INSTANCE;
    }

    
    
    @Override
    public List<Event> getAll() throws BusinessException{
        try {
            return em.createQuery("SELECT u FROM Event u WHERE enabled = '1' ", Event.class)               
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }
    
    public List<Event> getAllByParameters(EventParameter parameters) throws BusinessException{
        try {
            return em.createQuery("SELECT u FROM Event u WHERE enabled = '1' ", Event.class)               
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    @Override
    public Optional<Event> get(long id) {
        return Optional.ofNullable(em.find(Event.class, id));
    }

    @Override
    public void save(Event t) {
        executeInsideTransaction(entityManager -> entityManager.persist(t));
    }

    @Override
    public void update(Event t) {
        executeInsideTransaction(entityManager -> entityManager.merge(t));
    }

    @Override
    public void delete(Event t) {
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
