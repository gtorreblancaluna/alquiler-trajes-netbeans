package alquiler.trajes.dao;

import alquiler.trajes.config.PersistenceManager;
import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.entity.DetailEvent;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public final class DetailEventDao implements Dao<DetailEvent> {

    static EntityManager em = PersistenceManager.getInstance().createEntityManager();
    
    private DetailEventDao () {
        
    }
    private static DetailEventDao SINGLE_INSTANCE;
        
   public static synchronized DetailEventDao getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new DetailEventDao();
        }
        return SINGLE_INSTANCE;
    }

    @Override
    public List<DetailEvent> getAll() throws BusinessException{
        try {
            return em.createQuery("SELECT u FROM DetailEvent u ", DetailEvent.class)               
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    public List<DetailEvent> getAll(Long eventId) throws BusinessException{
        try {
            return em.createQuery("SELECT d FROM DetailEvent d WHERE d.event.id = :eventId", DetailEvent.class)
                    .setParameter("eventId", eventId)
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    @Override
    public Optional<DetailEvent> get(long id) {
        return Optional.ofNullable(em.find(DetailEvent.class, id));
    }

    @Override
    public void save(DetailEvent t) {
        executeInsideTransaction(entityManager -> entityManager.persist(t));
    }

    @Override
    public void update(DetailEvent t) {
        executeInsideTransaction(entityManager -> entityManager.merge(t));
    }

    @Override
    public void delete(DetailEvent t) {
        executeInsideTransaction(entityManager -> {
            entityManager.createQuery("DELETE FROM DetailEvent d WHERE d.id = :id")
                    .setParameter("id", t.getId());
        });
    }
    
    public void deleteAllByEvent(Long eventId) {
        executeInsideTransaction(entityManager -> {
            entityManager.createQuery("DELETE FROM DetailEvent d WHERE d.event.id = :eventId")
                    .setParameter("eventId", eventId).executeUpdate();
        });
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
