package alquiler.trajes.entity;

import alquiler.trajes.constant.ColumnDefinitionConstant;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = ColumnDefinitionConstant.ROLES_TABLE_NAME)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class Role implements Serializable {

    public Role(String name) {
        this.name = name;
    }
    public Role(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String name;
    
    @Column(
        nullable = false,
        columnDefinition = ColumnDefinitionConstant.TINYINT_DEFAULT_1_DEFINITION
    )
    private Boolean enabled;
    
    @PrePersist
    void preInsert() {        
        this.enabled = true;
    }
    
    @PreUpdate
    void preUpdate() {        
        if (this.enabled == null){
            this.enabled = true;
        }
    }
}
