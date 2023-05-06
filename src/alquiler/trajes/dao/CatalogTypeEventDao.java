package alquiler.trajes.dao;

import alquiler.trajes.config.PersistenceManager;
import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.entity.CatalogTypeEvent;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public final class CatalogTypeEventDao implements Dao<CatalogTypeEvent> {

    static EntityManager em = PersistenceManager.getInstance().createEntityManager();
    
    private CatalogTypeEventDao () {}
    private static CatalogTypeEventDao SINGLE_INSTANCE;
        
   public static synchronized CatalogTypeEventDao getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new CatalogTypeEventDao();
        }
        return SINGLE_INSTANCE;
    }

        
    @Override
    public List<CatalogTypeEvent> getAll() throws BusinessException{
        try {
            return em.createQuery("SELECT c FROM CatalogTypeEvent c WHERE c.enabled = '1' ", CatalogTypeEvent.class)               
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    @Override
    public Optional<CatalogTypeEvent> get(long id) {
        return Optional.ofNullable(em.find(CatalogTypeEvent.class, id));
    }

    @Override
    public void save(CatalogTypeEvent catalogTypeEvent) {
        executeInsideTransaction(entityManager -> entityManager.persist(catalogTypeEvent));
    }

    @Override
    public void update(CatalogTypeEvent catalogTypeEvent) {
        executeInsideTransaction(entityManager -> entityManager.merge(catalogTypeEvent));
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
    public void delete(CatalogTypeEvent t) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
