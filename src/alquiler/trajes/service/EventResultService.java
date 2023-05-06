package alquiler.trajes.service;

import alquiler.trajes.dao.EventResultDao;
import alquiler.trajes.model.params.EventParameter;
import alquiler.trajes.model.params.result.EventResult;
import java.util.List;


public final class EventResultService {
    
    private static EventResultService SINGLE_INSTANCE;
    
    private static final EventResultDao eventResultDao = EventResultDao.getInstance();
        
    public static synchronized EventResultService getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new EventResultService();
        }
        return SINGLE_INSTANCE;
    }
    
    public List<EventResult> getResult (EventParameter eventParameter) {
        return eventResultDao.getResult(eventParameter);
    }
    
        
}
