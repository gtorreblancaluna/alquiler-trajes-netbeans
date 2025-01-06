package alquiler.trajes.model.whatsapp;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateModel {
    private String name;
    private LanguageModel language;
    private List<ComponentModel> components;
}
