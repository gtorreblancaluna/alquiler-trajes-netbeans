
package alquiler.trajes.dao;

import alquiler.trajes.exceptions.BusinessException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    
    Optional<T> get(long id)throws BusinessException;
    
    List<T> getAll()throws BusinessException;
    
    void save(T t)throws BusinessException;
    
    void update(T t)throws BusinessException;
    
    void delete(T t)throws BusinessException;
}