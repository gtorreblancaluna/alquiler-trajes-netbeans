package alquiler.trajes.dao;

import alquiler.trajes.config.PersistenceManager;
import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.entity.GeneralInfo;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public final class GeneralInfoDao implements Dao<GeneralInfo> {

    static EntityManager em = PersistenceManager.getInstance().createEntityManager();
    
    private GeneralInfoDao () {}
    private static GeneralInfoDao SINGLE_INSTANCE;
        
   public static synchronized GeneralInfoDao getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new GeneralInfoDao();
        }
        return SINGLE_INSTANCE;
    }
    
    public Optional<GeneralInfo> getByKey(final String key) throws BusinessException{
        try {
            return Optional.ofNullable(
                    em.createQuery("SELECT g FROM GeneralInfo g WHERE g.enabled = '1' "
                    + "AND g.key = :key", GeneralInfo.class).setParameter("key", key)
                .getSingleResult());
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

        
    @Override
    public List<GeneralInfo> getAll() throws BusinessException{
        try {
            return em.createQuery("SELECT g FROM GeneralInfo g WHERE g.enabled = '1' ", GeneralInfo.class)               
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    @Override
    public Optional<GeneralInfo> get(long id) {
        return Optional.ofNullable(em.find(GeneralInfo.class, id));
    }

    @Override
    public void save(GeneralInfo t) {
        executeInsideTransaction(entityManager -> entityManager.persist(t));
    }

    @Override
    public void update(GeneralInfo t) {
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
    public void delete(GeneralInfo t) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
