package alquiler.trajes.entity;

import alquiler.trajes.constant.ColumnDefinitionConstant;
import lombok.*;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    
    @Column(name="unit_price")
    private Float unitPrice;
    
    @Column(name="advance_payment")
    private Float advancePayment;

    @Column(
        nullable = false,
        columnDefinition = ColumnDefinitionConstant.TINYINT_DEFAULT_1_DEFINITION
    )
    private Boolean enabled;

    @Column(name="created_at",nullable = false, updatable = false)
    private Date createdAt;
    
    @Column(name="updated_at",nullable = false)
    private Date updatedAt;


}
