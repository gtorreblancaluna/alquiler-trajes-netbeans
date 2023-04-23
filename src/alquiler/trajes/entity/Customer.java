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
@Table(name = ColumnDefinitionConstant.CUSTOMERS_TABLE_NAME)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter()
@Getter
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 45)
    private String email;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(name="last_name",nullable = false, length = 45)
    private String lastName;

    @Column(name="phone_number1",nullable = false,length = 10)
    private String phoneNumber1;
    
    @Column(name="phone_number2",length = 10)
    private String phoneNumber2;
        
    @Column(name="phone_number3",length = 10)
    private String phoneNumber3;

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
