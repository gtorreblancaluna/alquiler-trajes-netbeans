package alquiler.trajes.constant;

import lombok.Getter;

@Getter
public enum CatalogStatusEventEnum {
    
    APARTADO(1L,"Apartado"),
    IN_RENT(2L,"En renta"),
    PENDING(3L,"Pendiente"),
    CANCELED(4L,"Cancelado"),
    FINISHED(5L,"Finalizado");
    
    private Long id;
    private String name;
    
    CatalogStatusEventEnum (final Long id, final String name) {
       this.id = id;
       this.name = name;
    }
}
