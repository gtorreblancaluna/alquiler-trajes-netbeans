package alquiler.trajes.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public final class PropertiesService {
    
    private static PropertiesService SINGLE_INSTANCE;
    private Properties prop;
    private final static Logger LOGGER = Logger.getLogger(PropertiesService.class.getName());
        
    public static PropertiesService getInstance() {
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new PropertiesService();
        }
        return SINGLE_INSTANCE;
    }
    
    private PropertiesService() {
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("configuration.properties")) {
            prop = new Properties();
            prop.load(input);
        } catch (IOException e) {
           LOGGER.error(e.getMessage(),e);
        }

    }
    
    public String getProperty (String property) {
        return prop.get(property).toString();
    }
    
}
