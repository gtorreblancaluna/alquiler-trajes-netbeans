package alquiler.trajes.entity;

import alquiler.trajes.constant.ColumnDefinitionConstant;
import java.io.Serializable;
import lombok.*;
import java.util.Date;
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
@Table(name = ColumnDefinitionConstant.EVENTS_TABLE_NAME)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor()
@Setter()
@Getter
@ToString
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="catalog_type_event_id", nullable=false)
    private CatalogTypeEvent catalogTypeEvent;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="catalog_status_event_id", nullable=false)
    private CatalogStatusEvent catalogStatusEvent;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;
    
    @Column(name="delivery_date",nullable = false)
    private Date deliveryDate;
    
    @Column(name="delivery_hour",nullable = false)
    private String deliveryHour;
    
    @Column(name="return_date")
    private Date returnDate;
    
    @Column(name="return_hour")
    private String returnHour;

    @Column(nullable = false, length = 755)
    private String description;
    
    @Column(length = 755)
    private String comments;

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
