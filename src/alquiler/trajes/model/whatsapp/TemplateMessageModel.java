package alquiler.trajes.model.whatsapp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateMessageModel {
    private String messaging_product;
    private String recipient_type;
    private String to;
    private String type;
    private TemplateModel template;            
    
}
