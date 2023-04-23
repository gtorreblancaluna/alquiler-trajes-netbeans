package alquiler.trajes.dao;

import alquiler.trajes.config.PersistenceManager;
import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.entity.User;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class UserDao implements Dao<User> {

    static EntityManager em = PersistenceManager.getInstance().createEntityManager();
    
    private UserDao () {
        
    }
    private static final UserDao SINGLE_INSTANCE = null;
        
    public static UserDao getInstance(){
        
        if (SINGLE_INSTANCE == null) {
            return new UserDao();
        }
        return SINGLE_INSTANCE;
    }

    
    public User findByPasswd(final String passwordEncrypted) throws BusinessException{
        
        try {
        return em.createQuery("SELECT u FROM User u WHERE u.password = :passwd "
                + "AND enabled = '1' ", User.class)
                .setParameter("passwd", passwordEncrypted)
                .getSingleResult();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND_LOGIN);
        }
    }
    
    @Override
    public List<User> getAll() throws BusinessException{
        try {
            return em.createQuery("SELECT u FROM User u WHERE enabled = '1' ", User.class)               
                .getResultList();
        } catch (NoResultException e) {
            throw new NoDataFoundException(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        }
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public void save(User user) {
        executeInsideTransaction(entityManager -> entityManager.persist(user));
    }

    @Override
    public void update(User user) {
        executeInsideTransaction(entityManager -> entityManager.merge(user));
    }

    @Override
    public void delete(User t) {
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
