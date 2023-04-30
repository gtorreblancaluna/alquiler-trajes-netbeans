package alquiler.trajes.service;

import alquiler.trajes.dao.EventResultDao;
import alquiler.trajes.model.params.EventParameter;
import alquiler.trajes.model.params.result.EventResult;
import java.util.List;


public class EventResultService {
    
    private static final EventResultService SINGLE_INSTANCE = null;
    
    private static final EventResultDao eventResultDao = EventResultDao.getInstance();
        
    public static EventResultService getInstance(){
        
        if (SINGLE_INSTANCE == null) {
            return new EventResultService();
        }
        return SINGLE_INSTANCE;
    }
    
    public List<EventResult> getResult (EventParameter eventParameter) {
        return eventResultDao.getResult(eventParameter);
    }
    
        
}
