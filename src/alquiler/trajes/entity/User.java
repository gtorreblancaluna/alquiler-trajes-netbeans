package alquiler.trajes.entity;

import alquiler.trajes.constant.ColumnDefinitionConstant;
import java.io.Serializable;
import lombok.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Entity
@Table(name = ColumnDefinitionConstant.USERS_TABLE_NAME)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter()
@Getter
@ToString
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 45)
    private String email;

    @Column(nullable = false, length = 120, unique = true)
    private String password;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(name="last_name",nullable = false, length = 45)
    private String lastName;

    @Column(name="phone_number",length = 10)
    private String phoneNumber;

    @Column(
        nullable = false,
        columnDefinition = ColumnDefinitionConstant.TINYINT_DEFAULT_1_DEFINITION
    )
    private Boolean enabled;

    @Column(name="created_at",nullable = false, updatable = false)
    private Date createdAt;
    
    @Column(name="updated_at",nullable = false)
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = ColumnDefinitionConstant.USERS_ROLES_TABLE_NAME,
            joinColumns = @JoinColumn(name = ColumnDefinitionConstant.JOIN_COLUMN_USER_ID),
            inverseJoinColumns = @JoinColumn(name = ColumnDefinitionConstant.JOIN_COLUMN_ROLE_ID)
    )
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @PrePersist
    void preInsert() {
        this.createdAt = new Date();
        this.enabled = true;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = new Date();
        if (this.enabled == null){
            this.enabled = true;
        }
    }
}
