package alquiler.trajes.entity;

import alquiler.trajes.constant.ColumnDefinitionConstant;
import java.io.Serializable;
import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = ColumnDefinitionConstant.GENERAL_INFO_TABLE_NAME)
@Builder(toBuilder = true)
@AllArgsConstructor()
@NoArgsConstructor()
@Setter()
@Getter
public class GeneralInfo implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_v",nullable = false, length = 150)
    private String key;

    @Column(name = "value_v",nullable = false, length = 755)
    private String value;
    
    @Column(
        nullable = false,
        columnDefinition = ColumnDefinitionConstant.TINYINT_DEFAULT_1_DEFINITION
    )
    private Boolean enabled;

}
