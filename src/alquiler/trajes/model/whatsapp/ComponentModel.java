package alquiler.trajes.model.whatsapp;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComponentModel {
    
    private String type;
    private List<ParameterModel> parameters;
    
}
