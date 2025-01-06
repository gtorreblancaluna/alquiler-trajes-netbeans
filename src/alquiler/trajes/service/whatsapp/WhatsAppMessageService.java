package alquiler.trajes.service.whatsapp;

import alquiler.trajes.exceptions.WhatsAppMessageException;
import alquiler.trajes.model.whatsapp.TemplateMessageModel;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class WhatsAppMessageService {
    
    private static final int MAX_RETRIES = 3;
        
    private static final Logger logger = Logger.getLogger(WhatsAppMessageService.class.getName());
    
    public abstract TemplateMessageModel buildTemplate (final String message);
    
    private HttpRequest buildHttpRequest (final TemplateMessageModel templateMessageModel)throws WhatsAppMessageException {
        
        Gson gson = new Gson();
        
        try {        
            return HttpRequest.newBuilder()
            .uri(new URI("https://graph.facebook.com/v13.0/119484347808876/messages"))
            .header("Authorization", "Bearer EAANY4aSsh54BO7FSviG9uhExKWIU1jFxUvqSdSE9k58aSstngxFanYZCzHBUdBSGRNRlDHmqLBxmK3AqB7jvIzr3e5idcZAKPhUUqVGsjvQNrRJL8VDtZBmBG9x0AGh2po2r4KnSmf1ZC4RZBMOlanXQReWlZApNc0cMa2RP4rrRFtD2uLKQsXdXMYb3qf2Bly")
            .header("Content-Type", "application/json")
            //.POST(HttpRequest.BodyPublishers.ofString("{ \"messaging_product\": \"whatsapp\", \"recipient_type\": \"individual\", \"to\": \"525525612070\", \"type\": \"template\", \"template\": { \"name\": \"welcome_and_detail_event\", \"language\": { \"code\": \"es_MX\" } } }"))
            //.POST(HttpRequest.BodyPublishers.ofString("{ \"messaging_product\": \"whatsapp\", \"recipient_type\": \"individual\", \"to\": \"525525612070\", \"type\": \"text\", \"text\": { \"preview_url\": false, \"body\": \"This is an example of a text message\" } }"))
            //.POST(HttpRequest.BodyPublishers.ofString("{\"messaging_product\":\"whatsapp\",\"recipient_type\":\"individual\",\"to\":\"527474985486\",\"type\":\"template\",\"template\":{\"name\":\"welcome_and_detail_event\",\"language\":{\"code\":\"es_MX\"},\"components\":[{\"type\":\"body\",\"parameters\":[{\"type\":\"text\",\"text\":\"detalleporteformal\"}]}]}}"))
              .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(templateMessageModel))).build();
        
        } catch (URISyntaxException e) {
            throw new WhatsAppMessageException(e.getMessage(),e);
        }
    }
    
    public void sendMessage (final String message) throws WhatsAppMessageException{
        //https://developers.facebook.com/blog/post/2022/11/07/adding-whatsapp-to-your-java-projects/
       
        TemplateMessageModel templateMessageModel = buildTemplate(message);
        HttpRequest request = buildHttpRequest(templateMessageModel);
        
        HttpClient http = HttpClient.newHttpClient();
        
        for (int retry = 0; retry <= MAX_RETRIES; retry++) {
            try {               
               HttpResponse<String> response = http.send(request,HttpResponse.BodyHandlers.ofString());
               logger.log(Level.INFO,"Response body: {0}",response.body());
               break;

           } catch (IOException | InterruptedException e) {
                
               try {
                    Thread.sleep(1000);                    
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new WhatsAppMessageException(e.getMessage(),e);
                }
                
                if (retry == MAX_RETRIES) {
                    throw new WhatsAppMessageException(e.getMessage(),e);
                } else {
                    logger.log(Level.INFO,"Applying retry: {0}",retry+1);
                }
           }
       } //end for
    }
    
}
