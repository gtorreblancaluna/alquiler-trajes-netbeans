package alquiler.trajes.constant;

import lombok.Getter;

@Getter
public enum CatalogTypeEventEnum {
    
    ADMIN(1L,"Alquiler"),
    DEFAULT(2L,"Venta");
    
    private Long id;
    private String name;
    
    CatalogTypeEventEnum (final Long id, final String name) {
       this.id = id;
       this.name = name;
    }
}
