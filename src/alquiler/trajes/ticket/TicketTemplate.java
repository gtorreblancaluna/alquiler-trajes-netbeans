package alquiler.trajes.ticket;

import static alquiler.trajes.constant.ApplicationConstants.DATE_MEDIUM;
import alquiler.trajes.constant.GeneralInfoEnum;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.service.GeneralInfoService;
import alquiler.trajes.util.UtilityTicket;
import java.util.Date;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

public abstract class TicketTemplate {    

    private static final Logger log = Logger.getLogger(TicketTemplate.class.getName());
    protected final FastDateFormat dateFormat = FastDateFormat.getInstance(DATE_MEDIUM);
    private final GeneralInfoService generalInfoService;
    
    protected TicketTemplate () {
        generalInfoService = GeneralInfoService.getInstance();
    }

    protected final String LINE_BREAK = "\n";
    protected final String LINE = "================================";
    protected final String SUB_LINE_POINTS = "................................";
    protected final int LENGHT_BY_LINE = 32;
    protected final String STRING_FORMAT_NUMBER = "%1$9s";
    protected final String STRING_FORMAT_TWO_PARAMETERS = "%s %s";
    protected final String STRING_FORMAT_THREE_PARAMETERS = "%s %s %s";
    
    //default implementation
    private String buildHeader() {
        
        System.out.println("Building Header");
        StringBuilder headerBuilder = new StringBuilder();
        try {
            final String companyName = 
                    generalInfoService.getByKey(GeneralInfoEnum.COMPANY_NAME.getKey());
            
            if (companyName.length() > LENGHT_BY_LINE) {
                headerBuilder.append(companyName.substring(0,LENGHT_BY_LINE));
            } else {
                headerBuilder.append(UtilityTicket.center(companyName,LENGHT_BY_LINE));
            }
            
            headerBuilder.append(LINE_BREAK);
            headerBuilder.append(UtilityTicket.center("Agradecemos su preferencia.",LENGHT_BY_LINE));
            
        } catch (BusinessException e) {
        }
        return headerBuilder.toString();
    }
    
    //default implementation
    private String buildFooter() {
        
        System.out.println("Building Footer");
        StringBuilder footerBuilder = new StringBuilder();
        try {
            String infoFooter = generalInfoService.getByKey(GeneralInfoEnum.INFO_FOOTER_PDF_A5.getKey());
            footerBuilder.append(UtilityTicket.justify(
                    LENGHT_BY_LINE, 
                    infoFooter, 
                    LINE_BREAK,12));
            footerBuilder.append(LINE_BREAK);
        } catch (BusinessException e) {
        }   
        footerBuilder.append("Fecha impresión: ").append(dateFormat.format(new Date()));
        return footerBuilder.toString();
    }
    
    //method to be implemented by subclass
    protected abstract String buildBody();
    
    //template method, final so subclasses can't override
    public final void generateTicket() throws PrintException{
            
            StringBuilder contentTicket = new StringBuilder();
            contentTicket
                    .append(LINE_BREAK)
                    .append(LINE_BREAK)
                    .append(LINE)
                    .append(LINE_BREAK)
                    .append(StringUtils.stripAccents(buildHeader()))
                    .append(LINE_BREAK)
                    .append(LINE)
                    .append(LINE_BREAK)
                    .append(StringUtils.stripAccents(buildBody()))
                    .append(LINE).append(LINE_BREAK)
                    .append(StringUtils.stripAccents(buildFooter()))
                    .append(LINE_BREAK)
                    .append(LINE_BREAK);
            
            System.out.println("Ticket generated: \n"+contentTicket.toString());
            
            //Especificamos el tipo de dato a imprimir
            //Tipo: bytes; Subtipo: autodetectado
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            //Aca obtenemos el servicio de impresion por defatul
            //Si no quieres ver el dialogo de seleccionar impresora usa esto
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

            //Con esto mostramos el dialogo para seleccionar impresora
            //Si quieres ver el dialogo de seleccionar impresora usalo
            //Solo mostrara las impresoras que soporte arreglo de bits
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            //PrintService service = ServiceUI.printDialog(null, 700, 200, printService, defaultService, flavor, pras);
            //Creamos un arreglo de tipo byte
            byte[] bytes;
            //Aca convertimos el string(cuerpo del ticket) a bytes tal como
            //lo maneja la impresora(mas bien ticketera :p)
            bytes = contentTicket.toString().getBytes();
            //Creamos un documento a imprimir, a el se le appendeara
            //el arreglo de bytes
            Doc doc = new SimpleDoc(bytes, flavor, null);
            //Creamos un trabajo de impresión
            DocPrintJob job = defaultService.createPrintJob();
            //Imprimimos dentro de un try de a huevo
            //El metodo print imprime
            job.print(doc, null);
            
    }
    
}
