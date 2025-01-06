package alquiler.trajes.model.params;

import alquiler.trajes.entity.CatalogStatusEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventParameter {
    
    private String initActivityDate;
    private String endActivityDate;
    private String initDeliveryDate;
    private String endDeliveryDate;
    private String initReturnDate;
    private String endReturnDate;
    private String customerName;
    private Long eventId;
    private String description;
    private String limit;
    private CatalogStatusEvent status;
}
