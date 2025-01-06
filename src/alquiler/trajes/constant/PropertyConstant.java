package alquiler.trajes.constant;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public enum PropertyConstant {
    
   SYSTEM_THEME("system.theme",SubstanceThemeConstant.BUSINESS_SKIN),
   LAST_LOGIN_DATE_TIME("last.login.date.time",LocalDateTime.now().toString()),
   MINUTES_TO_REQUEST_NEW_SESSION("minutes.to.request.new.session",String.valueOf(5)),
   PLUS_DAYS_TO_EVENTS_IN_MAIN_FORM("plus.days.events.in.main.form",String.valueOf(5)),
   CALCULATE_RETURN_DATE("calculate.return.date","true"),
   CATALOG_STATUS("catalog_status","{}"),
   CATALOG_TYPE("catalog_status","{}");
   
   PropertyConstant (final String key, final String value) {
       this.key = key;
       this.value = value;
   }
   
   private final String key;
   private final String value;
}
