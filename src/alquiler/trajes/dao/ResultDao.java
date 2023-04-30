package alquiler.trajes.dao;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;


public abstract class ResultDao {    

    
    public static <T> T map(Class<T> type, Object[] tuple){
        List<Class<?>> tupleTypes = new ArrayList<>();
        for(Object field : tuple){
           tupleTypes.add(field.getClass());
        }
        try {
           Constructor<T> ctor = type.getConstructor(tupleTypes.toArray(new Class<?>[tuple.length]));
           return ctor.newInstance(tuple);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
    
    public static <T> List<T> map(Class<T> type, List<Object[]> records){
        List<T> result = new LinkedList<>();
        for(Object[] record : records){
           result.add(map(type, record));
        }
        return result;
    }
    
    public static <T> List<T> getResultList(Query query, Class<T> type){
      @SuppressWarnings("unchecked")
      List<Object[]> records = query.getResultList();
      return map(type, records);
    }
    
}
