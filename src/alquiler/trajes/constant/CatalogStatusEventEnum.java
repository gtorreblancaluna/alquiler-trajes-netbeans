package alquiler.trajes.constant;

import lombok.Getter;

@Getter
public enum CatalogStatusEventEnum {
    
    APARTADO(1L,"Apartado"),
    IN_RENT(2L,"En renta"),
    CANCELED(3L,"Cancelado"),
    FINISHED(4L,"Finalizado");
    
    private Long id;
    private String name;
    
    CatalogStatusEventEnum (final Long id, final String name) {
       this.id = id;
       this.name = name;
    }
}
