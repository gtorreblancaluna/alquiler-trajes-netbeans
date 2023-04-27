package alquiler.trajes.entity;

import alquiler.trajes.constant.ColumnDefinitionConstant;
import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = ColumnDefinitionConstant.CATALOG_STATUS_EVENT_TABLE_NAME)
@Builder(toBuilder = true)
@AllArgsConstructor()
@NoArgsConstructor()
@Setter()
@Getter
public class CatalogStatusEvent {
    
    public CatalogStatusEvent (final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(
        nullable = false,
        columnDefinition = ColumnDefinitionConstant.TINYINT_DEFAULT_1_DEFINITION
    )
    private Boolean enabled;
    
    @Override
    public String toString() {
        return name;
    }

}
