package alquiler.trajes.model.params.result;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResult {
    
    private Integer id;
    private String description;
    private String customer;
    private Timestamp deliveryDate;
    private String deliveryHour;
    private Timestamp returnDate;
    private String returnHour;
    private Double payments;
    private Double subTotal;
    private Timestamp createdAt;
    
}
