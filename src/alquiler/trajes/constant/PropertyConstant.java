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
   CATALOG_STATUS_LIST("catalog.status.list","[]"),
   CATALOG_TYPE_LIST("catalog.type.list","[]"),
   RETURN_HOUR_EVENT_FORM_DEFAULT("return.hour.evento.form.default","12:00"),
   COMPANY_NAME_PDF_A5("company.name.pdf.5","PORTE FORMAL"),
   INFO_FOOTER_PDF_A5("info.footer.pdf.5","Es necesario traer tu INE original y comprobante de domicilio original Tels. 7474985486,7471067891(solo whatsapp). Horario Lunes a Sabado 10:30 a 19:30. Conserve esta nota para cualquier aclaración. Se cobrará $50.00 por cada dia sin devolver a partir del dia indicado como 'Fecha de devolución'."),
   IMPORTANT_INFO_PDF_A5("important.info.pdf.5","IMPORTANTE TRAER INE ORIGINAL Y COMPROBANTE DE DOMICILIO ORIGINAL (NO MAYOR A 3 MESES)");
   
   PropertyConstant (final String key, final String value) {
       this.key = key;
       this.value = value;
   }
   
   private final String key;
   private final String value;
}
