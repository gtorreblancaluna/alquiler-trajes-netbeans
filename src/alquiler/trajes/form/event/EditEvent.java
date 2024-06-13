package alquiler.trajes.form.event;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.EMPTY_STRING_TXT_FIELD;
import static alquiler.trajes.constant.ApplicationConstants.MESSAGE_TITLE_ERROR;
import alquiler.trajes.entity.DetailEvent;
import alquiler.trajes.entity.Payment;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.service.PaymentService;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class EditEvent extends EventForm {

    private final PaymentService paymentService;
    private Long eventId;
    
    public EditEvent(Long eventId) {
        super();
        this.eventId = eventId;
        paymentService = PaymentService.getInstance();
        this.setTitle(ApplicationConstants.TITLE_EDIT_EVENT_FORM);        
        init();
    }
    
    
    private void init () {
        
        fillCatalogTypeEventService();
        super.lblInfoUser.setText("Atendió:");
        try {
            super.event = eventService.findById(eventId);
            
            String subrayarLabelCustomer = "<html><u>"+super.event.getCustomer().getName()+" "+super.event.getCustomer().getLastName()+"</u></html>";            
            lblCustomer.setText(subrayarLabelCustomer);
            lblUser.setText(this.event.getUser().getName()+" "+this.event.getUser().getLastName());
            lblEventCreatedAt.setText("Fecha de elaboración: "+fastDateFormatLarge.format(this.event.getCreatedAt()));
            tabPaneMain.setSelectedIndex(INDEX_EVENT_PANE);
            disableForm();
            btnEventAdd.setEnabled(true);
            btnCloneEvent.setEnabled(true);
            lblFolio.setText(String.valueOf(super.event.getId()));

            fillInputsFormFromEvent();
            cmbCatalogType.setSelectedItem(super.event.getCatalogTypeEvent());
            cmbStatus.setSelectedItem(super.event.getCatalogStatusEvent());
            
            new Thread(() -> {
                fillTablePaymentsFromEventId();
                fillTableDetailFromEvent();
                total();
            }).start();
            
        } catch (BusinessException e) {
           log.error(e);
           JOptionPane.showMessageDialog(this, e.getMessage(), ApplicationConstants.MESSAGE_UNEXPECTED_ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void fillTablePayments (List<Payment> list) {
        super.tableFormatPayments.format();
        for (Payment payment : list) {
            DefaultTableModel temp = (DefaultTableModel) tableFormatPayments.getModel();
            Object row[] = {
                payment.getId(),
                payment.getPayment(),
                payment.getComment(),
                fastDateFormatLarge.format(payment.getCreatedAt()),
                payment.getUser().getName()+ " "+ payment.getUser().getLastName()
            };
            temp.addRow(row);
        }
    }
    
    private void fillTableDetailFromEvent () {
        try {
            super.detailEvent = detailEventService.getAll(this.event.getId());
            super.tableFormatDetail.format();
            for (DetailEvent detail : this.detailEvent) {
                addDetailToTable(detail,null);
            }
        } catch (BusinessException e) {
            log.error(e);
            JOptionPane.showMessageDialog(this, e.getMessage(), MESSAGE_TITLE_ERROR, JOptionPane.ERROR_MESSAGE); 
        }
    }
    
    private void fillTablePaymentsFromEventId () {
        super.paymentsEvent = new ArrayList<>();
        try {
            super.paymentsEvent = paymentService.getAll(this.event.getId());
            fillTablePayments(this.paymentsEvent);
        } catch (BusinessException e) {
            log.error(e);
            JOptionPane.showMessageDialog(this, e.getMessage(), MESSAGE_TITLE_ERROR, JOptionPane.ERROR_MESSAGE); 
        }
        
        
    }
    
        // is called when event exist to edit.
    private void fillInputsFormFromEvent () {
        
        dateChooserDeliveryDate.setDate(this.event.getDeliveryDate());
        txtDeliveryHour.setText(this.event.getDeliveryHour());

        if (this.event.getReturnDate() != null) {
            dateChooserReturnDate.setDate(this.event.getReturnDate());
            txtReturnHour.setText(this.event.getReturnHour());
        } else {
            dateChooserReturnDate.setDate(null);
            txtReturnHour.setText(EMPTY_STRING_TXT_FIELD);
        }
        this.txtAreaDescription.setText(this.event.getDescription());
        
    }
    
}
