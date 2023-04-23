package alquiler.trajes.entity;

import alquiler.trajes.constant.ColumnDefinitionConstant;
import lombok.*;
import java.util.Date;
import javax.persistence.CascadeType;
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
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter()
@Getter
@ToString
public class DetailEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="event_id", nullable=false)
    private Event event;
   
    @Column(name="name_of_aggregate",nullable = false, length = 355)
    private String nameOfAggregate;
    
    @Column(length = 755)
    private String description;
    
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
