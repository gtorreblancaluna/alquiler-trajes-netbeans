package alquiler.trajes.constant;

import lombok.Getter;

@Getter
public enum CatalogTypeEventEnum {
    
    RENT(1L,"Alquiler"),
    SALES(2L,"Venta");
    
    private Long id;
    private String name;
    
    CatalogTypeEventEnum (final Long id, final String name) {
       this.id = id;
       this.name = name;
    }
}
