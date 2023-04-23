package alquiler.trajes.constant;

import lombok.Getter;

@Getter
public enum RoleEnum {
    
    ADMIN(1L,"Admin"),
    DEFAULT(2L,"Default");
    
    private Long id;
    private String name;
    
    RoleEnum (final Long id, final String name) {
       this.id = id;
       this.name = name;
    }
}
