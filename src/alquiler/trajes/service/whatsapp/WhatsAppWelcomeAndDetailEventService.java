package alquiler.trajes.service.whatsapp;

import alquiler.trajes.model.whatsapp.ComponentModel;
import alquiler.trajes.model.whatsapp.LanguageModel;
import alquiler.trajes.model.whatsapp.ParameterModel;
import alquiler.trajes.model.whatsapp.TemplateMessageModel;
import alquiler.trajes.model.whatsapp.TemplateModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class WhatsAppWelcomeAndDetailEventService extends WhatsAppMessageService {
    
    private static final Logger logger = Logger.getLogger(WhatsAppWelcomeAndDetailEventService.class.getName());
       
    private static WhatsAppWelcomeAndDetailEventService SINGLE_INSTANCE;
        
    public static synchronized WhatsAppWelcomeAndDetailEventService getInstance() {

         if (SINGLE_INSTANCE == null) {
             SINGLE_INSTANCE = new WhatsAppWelcomeAndDetailEventService();
         }
         return SINGLE_INSTANCE;
     }  
   
    @Override
    public TemplateMessageModel buildTemplate (final String message) {
        List<ParameterModel> parameters = new ArrayList<>();
               parameters.add(ParameterModel.builder()
                       .type("text")
                       .text(message)
                       .build());

               List<ComponentModel> components = new ArrayList<>();
               components.add(ComponentModel.builder()
                       .type("body")
                       .parameters(parameters)
                       .build());

               return
                       TemplateMessageModel.builder()
                               .messaging_product("whatsapp")
                               .recipient_type("individual")
                               .to("525525612070")
                               .type("template")
                               .template(TemplateModel.builder()
                                       .name("welcome_and_detail_event")
                                       .language(LanguageModel.builder()
                                               .code("es_MX")
                                               .build())
                                       .components(components)
                                       .build())
                               .build();
    }
    
}
