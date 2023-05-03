package alquiler.trajes.constant;

import lombok.Getter;

@Getter
public enum GeneralInfoEnum {
    
    COMPANY_NAME("COMPANY_NAME"),
    COMPANY_PHONES("COMPANY_PHONES"),
    INFO_FOOTER_PDF_A5("INFO_FOOTER_PDF_A5"),
    COMPANY_ADDRESS("COMPANY_ADDRESS");
    
    private String key;
    
    GeneralInfoEnum (final String key) {
       this.key = key;
    }
}
