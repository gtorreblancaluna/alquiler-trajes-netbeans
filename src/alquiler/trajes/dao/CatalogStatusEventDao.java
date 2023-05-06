package alquiler.trajes.dao;

import alquiler.trajes.config.PersistenceManager;
import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.entity.CatalogStatusEvent;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public final class CatalogStatusEventDao implements Dao<CatalogStatusEvent> {

    static EntityManager em = PersistenceManager.getInstance().createEntityManager();
    
    private CatalogStatusEventDao () {}
    private static CatalogStatusEventDao SINGLE_INSTANCE;
        
   public static synchronized CatalogStatusEventDao getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new CatalogStatusEventDao();
        }
        return SINGLE_INSTANCE;
    }

        
    @Override
    public List<CatalogStatusEvent> getAll() throws BusinessException{
        try {
            return em.createQuery("SELECT c FROM CatalogStatusEvent c WHERE c.enabled = '1' ", CatalogStatusEvent.class)               
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    @Override
    public Optional<CatalogStatusEvent> get(long id) {
        return Optional.ofNullable(em.find(CatalogStatusEvent.class, id));
    }

    @Override
    public void save(CatalogStatusEvent t) {
        executeInsideTransaction(entityManager -> entityManager.persist(t));
    }

    @Override
    public void update(CatalogStatusEvent t) {
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
    public void delete(CatalogStatusEvent t) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
