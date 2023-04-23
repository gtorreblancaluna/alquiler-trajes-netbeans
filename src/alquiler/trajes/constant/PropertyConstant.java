package alquiler.trajes.constant;

import lombok.Getter;

@Getter
public enum PropertyConstant {
   
   SYSTEM_THEME("system.theme",SubstanceThemeConstant.BUSINESS_SKIN),
   GENERATE_TASK_ALMACEN("generate.task.almacen","true"),
   GENERATE_TASK_CHOFER("generate.task.chofer","true");
   
   PropertyConstant (final String key, final String value) {
       this.key = key;
       this.value = value;
   }
   
   private final String key;
   private final String value;
}
