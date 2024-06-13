package alquiler.trajes.entity;

import alquiler.trajes.constant.ColumnDefinitionConstant;
import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = ColumnDefinitionConstant.DETAIL_EVENT_TABLE_NAME)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor()
@Setter()
@Getter
@ToString
public class DetailEvent {   

    public DetailEvent(Long id) {
        this.id = id;
    }    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @Column(name="name_of_aggregate",nullable = false, length = 355)
    private String nameOfAggregate;
    
    @Column(length = 755)
    private String items;
    
    @Column(length = 755)
    private String adjustments;

    private String status;
    
    @Column(name="unit_price")
    private Float unitPrice;
    
    @Column(name="advance_payment")
    private Float advancePayment;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="event_id", nullable=false)
    private Event event;
    
}
