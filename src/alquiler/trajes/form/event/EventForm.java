package alquiler.trajes.form.event;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.DATE_LARGE;
import static alquiler.trajes.constant.ApplicationConstants.DATE_MEDIUM;
import static alquiler.trajes.constant.ApplicationConstants.DECIMAL_FORMAT;
import static alquiler.trajes.constant.ApplicationConstants.DESCRIPTION_FORMAT_24_HRS;
import static alquiler.trajes.constant.ApplicationConstants.EMPTY_STRING_TXT_FIELD;
import static alquiler.trajes.constant.ApplicationConstants.ENTER_KEY;
import static alquiler.trajes.constant.ApplicationConstants.MESSAGE_TITLE_ERROR;
import static alquiler.trajes.constant.ApplicationConstants.PATH_NAME_EVENT_REPORT_VERTICAL_A5;
import static alquiler.trajes.constant.ApplicationConstants.PDF_NAME_EVENT_REPORT_VERTICAL_A5;
import alquiler.trajes.constant.GeneralInfoEnum;
import alquiler.trajes.entity.CatalogStatusEvent;
import alquiler.trajes.entity.CatalogTypeEvent;
import alquiler.trajes.entity.Customer;
import alquiler.trajes.entity.DetailEvent;
import alquiler.trajes.entity.Event;
import alquiler.trajes.entity.Payment;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.InvalidDataException;
import alquiler.trajes.exceptions.NoDataFoundException;
import alquiler.trajes.exceptions.UnAuthorizedException;
import alquiler.trajes.form.login.LoginForm;
import alquiler.trajes.service.CatalogStatusEventService;
import alquiler.trajes.service.CatalogTypeEventService;
import alquiler.trajes.service.CustomerService;
import alquiler.trajes.service.DetailEventService;
import alquiler.trajes.service.EventService;
import alquiler.trajes.service.GeneralInfoService;
import alquiler.trajes.service.PaymentService;
import alquiler.trajes.table.TableFormatDetail;
import alquiler.trajes.table.TableFormatCustomers;
import alquiler.trajes.table.TableFormatPayments;
import alquiler.trajes.util.JasperPrintUtil;
import alquiler.trajes.util.Utility;
import static alquiler.trajes.util.Utility.onlyAdminUserAccess;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.time.FastDateFormat;


public class EventForm extends javax.swing.JInternalFrame {

    private Long eventId;
    private Event event;
    private List<Payment> paymentsEvent;
    private List<DetailEvent> detailEvent;
    private final static int INDEX_CUSTOMER_PANE = 0;
    private final static int INDEX_EVENT_PANE = 1;
    private final CustomerService customerService;
    private final TableFormatCustomers customersTableFormat;
    private final TableFormatDetail tableFormatDetail;
    private final TableFormatPayments tableFormatPayments;
    private List<Customer> customers;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EventForm.class.getName());
    private static final String DATE_VALUE_CHOOSER = "date";
    private final CatalogTypeEventService catalogTypeEventService;
    private final CatalogStatusEventService catalogStatusEventService;
    private final EventService eventService;
    private final PaymentService paymentService;
    private final JasperPrintUtil jasperPrintUtil;
    private final GeneralInfoService generalInfoService;
    private final DetailEventService detailEventService;
    private List<CatalogTypeEvent> types = new ArrayList<>();
    private List<CatalogStatusEvent> status = new ArrayList<>();
    private static final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
    private final FastDateFormat fastDateFormatMedium = FastDateFormat.getInstance(DATE_MEDIUM);
    private final FastDateFormat fastDateFormatLarge = FastDateFormat.getInstance(DATE_LARGE);
    private String referenceRowToEditPaymentTable = null;

    private static final String REGEX_SPLIT_HOUR = ":";
    private static final String DELETE_CHARS_NUMBER = "$,";
    
    public EventForm(Long eventId) {
        initComponents();
        this.setTitle(ApplicationConstants.TITLE_EVENTS_FORM);
        this.eventId = eventId;
        this.customerService = CustomerService.getInstance();
        catalogTypeEventService = CatalogTypeEventService.getInstance();
        catalogStatusEventService = CatalogStatusEventService.getInstance();
        generalInfoService = GeneralInfoService.getInstance();
        jasperPrintUtil = JasperPrintUtil.getInstance();
        paymentService = PaymentService.getInstance();
        detailEventService = DetailEventService.getInstance();
        eventService = EventService.getInstance();
        customersTableFormat = new TableFormatCustomers();
        tableFormatDetail = new TableFormatDetail();
        tableFormatPayments = new TableFormatPayments();
        Utility.addJtableToPane(719, 451, this.panelInnerPaymetsTable, tableFormatPayments);
        Utility.addJtableToPane(719, 451, paneCustomersTable, customersTableFormat);
        Utility.addJtableToPane(719, 451, this.panelInnerTableAgregados, tableFormatDetail);
        
        new Thread(() -> {
            getCustomers();
        }).start();
        btnEventAdd.setEnabled(false);
        if (this.eventId != null) {
            initFormWithExistentEvent();
        } else {
            initFormWithNewEvent();
        }
        this.setClosable(true);
        addMouseListenerToTableCustomers();      
        addActionListenerToDateChoosers();
        addMouseListenerToTableDetail();
        addMouseListenerToTablePayments();

    }
    
    private void generateEventPDF () {
        
        if (this.event.getId() == null) {
            JOptionPane.showMessageDialog(
                   this, "Guarda el evento para generar el PDF.",
                   ApplicationConstants.MESSAGE_MISSING_PARAMETERS,
                   JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Map parameters = new HashMap<>();
            
            float sumPayment = 0f;
            float sumDetail = 0f;
            
            for (DetailEvent det : this.detailEvent) {
                if (det.getUnitPrice() != null){
                    sumDetail += det.getUnitPrice();
                }
            }
            
            for (Payment payment : this.paymentsEvent) {
                if (payment.getPayment() != null) {
                    sumPayment += payment.getPayment();
                }
            }
            
            String total = decimalFormat.format(sumDetail-sumPayment);
            
            parameters.put("ID", event.getId().toString());
            parameters.put("FOLIO", event.getId().toString());
            parameters.put("USER_NAME", event.getUser().getName()+" "+event.getUser().getLastName());
            parameters.put("CUSTOMER_NAME", event.getCustomer().getName()+" "+event.getCustomer().getLastName());
            parameters.put("TYPE_EVENT", event.getCatalogTypeEvent().getName());
            parameters.put("STATUS_EVENT", event.getCatalogStatusEvent().getName());
            parameters.put("DELIVERY_DATE", fastDateFormatLarge.format(event.getDeliveryDate())+" "+event.getDeliveryHour()+DESCRIPTION_FORMAT_24_HRS);
            parameters.put("RETURN_DATE", fastDateFormatLarge.format(event.getReturnDate())+" "+event.getReturnHour()+DESCRIPTION_FORMAT_24_HRS);
            parameters.put("CREATED_AT_DATE", fastDateFormatLarge.format(event.getCreatedAt()));
            parameters.put("DESCRIPTION", event.getDescription());
            parameters.put("PAYMENTS", decimalFormat.format(sumPayment));
            parameters.put("SUBTOTAL", decimalFormat.format(sumDetail));
            parameters.put("TOTAL", total);
            parameters.put("COMPANY_NAME", generalInfoService.getByKey(GeneralInfoEnum.COMPANY_NAME.getKey()));
            parameters.put("INFO_FOOTER_PDF_A5", generalInfoService.getByKey(GeneralInfoEnum.INFO_FOOTER_PDF_A5.getKey()));
            jasperPrintUtil.showPDF(parameters, PATH_NAME_EVENT_REPORT_VERTICAL_A5, PDF_NAME_EVENT_REPORT_VERTICAL_A5);
        } catch (BusinessException e) {
            log.error(e);
           JOptionPane.showMessageDialog(
                   this, e.getMessage(),
                   ApplicationConstants.MESSAGE_UNEXPECTED_ERROR,
                   JOptionPane.ERROR_MESSAGE);  
        }
    }
    
    private void getEventDateFromInputs () throws InvalidDataException{
        
        if (!Utility.validateHour(txtDeliveryHour.getText())) {
            throw new InvalidDataException("Ingresa una hora de entrega valida.");
        }        
        
        String[] deliveryHour = txtDeliveryHour.getText().split(REGEX_SPLIT_HOUR);
        String[] returnHour = txtReturnHour.getText().split(REGEX_SPLIT_HOUR);
        
        this.event.setDeliveryHour(txtDeliveryHour.getText());
        this.event.setReturnHour(txtReturnHour.getText());
        
        Date deliveryDate = dateChooserDeliveryDate.getDate();
        Date returnDate = dateChooserReturnDate.getDate();
        
        if (deliveryDate == null) {
            throw new InvalidDataException("Fecha de entrega es requerida.");
        }
        
        if(returnDate != null && deliveryDate.after(returnDate)){
           throw new InvalidDataException("Fecha de entrega debe ser menor a la fecha de devolución.");
        }        

        event.setDeliveryDate(Utility.setHourAndMinutesFromDate(deliveryHour,deliveryDate));
        
        if (dateChooserReturnDate.getDate() != null && !Utility.validateHour(txtReturnHour.getText())) {
            throw new InvalidDataException("Ingresa una hora de devolución valida.");
        } else if (dateChooserReturnDate.getDate() != null){
            event.setReturnDate(Utility.setHourAndMinutesFromDate(returnHour,returnDate));
        }
        
        
        
    }
    private void getEventFromInputs () throws InvalidDataException{
        event.setCatalogTypeEvent((CatalogTypeEvent) cmbCatalogType.getModel().getSelectedItem());
        event.setCatalogStatusEvent((CatalogStatusEvent) cmbStatus.getModel().getSelectedItem());
        event.setDescription(txtAreaDescription.getText().trim());
    }
    
    private void getDetailEventFromTable () throws InvalidDataException {
        if (tableFormatDetail.getRowCount() < 0) {
            throw new InvalidDataException("Agrega al menos un detalle al evento.");
        }
        detailEvent = new ArrayList<>();
        for (int i = 0; i < tableFormatDetail.getRowCount(); i++) {
            detailEvent.add(
                    DetailEvent.builder()
                            .nameOfAggregate(
                                    String.valueOf(tableFormatDetail.getValueAt(i, TableFormatDetail.Column.NAME.getNumber()))
                            ).items(
                                    String.valueOf(tableFormatDetail.getValueAt(i, TableFormatDetail.Column.ITEMS.getNumber()))
                            ).adjustments(
                                    String.valueOf(tableFormatDetail.getValueAt(i, TableFormatDetail.Column.ADJUTS.getNumber()))
                            )
                            .unitPrice(
                                    Float.parseFloat(
                                            String.valueOf(tableFormatDetail.getValueAt(i, TableFormatDetail.Column.IMPORT.getNumber())))
                            ).advancePayment(
                                    Float.parseFloat(
                                            String.valueOf(tableFormatDetail.getValueAt(i, TableFormatDetail.Column.PAYMENT.getNumber())))
                            )
                            .build()
            );
        }
    }
    
    private void getPaymentsFromTable() {
        for (int i = 0; i < this.tableFormatPayments.getRowCount(); i++) {
            Long id = 
                Long.parseLong(
                        String.valueOf(this.tableFormatPayments.getValueAt(i, TableFormatPayments.Column.ID.getNumber())));
            
            final float payment = 
                        Float.parseFloat(
                                Utility.deleteCharacters(String.valueOf(tableFormatPayments.getValueAt(i, TableFormatPayments.Column.IMPORT.getNumber())),DELETE_CHARS_NUMBER));
            final String comment =
                    String.valueOf(tableFormatPayments.getValueAt(i, TableFormatPayments.Column.CONCEPT.getNumber()));
            
            if (id.equals(0L)) {
                paymentsEvent.add(
                        Payment.builder()
                                .id(0L)
                                .comment(comment)
                                .payment(payment)
                                .user(LoginForm.userSession)
                                .createdAt(new Date())
                                .build()
                );
            } else {
                paymentsEvent.stream()
                    .filter(t -> t.getId().equals(id))
                    .peek(t -> t.setPayment(payment))
                    .peek(t -> t.setComment(comment))
                    .findFirst();
            }
        }
    }
    
    private void fillTablePayments (List<Payment> list) {
        this.tableFormatPayments.format();
        for (Payment payment : list) {
            DefaultTableModel temp = (DefaultTableModel) tableFormatPayments.getModel();
            Object row[] = {
                payment.getId(),
                payment.getPayment(),
                payment.getComment(),
                fastDateFormatMedium.format(payment.getCreatedAt()),
                payment.getUser().getName()+ " "+ payment.getUser().getLastName()
            };
            temp.addRow(row);
        }
    }
    
    
    private void enableForm () {
      this.dateChooserDeliveryDate.setEnabled(true);
      this.dateChooserReturnDate.setEnabled(true);
      this.cmbCatalogType.setEnabled(true);
      this.cmbStatus.setEnabled(true);
      this.txtDeliveryHour.setEnabled(true);
      this.txtReturnHour.setEnabled(true);
      this.txtAreaDescription.setEnabled(true);
      this.tableFormatDetail.setEnabled(true);
      this.tableFormatPayments.setEnabled(true);
      btnAgregadosDelete.setEnabled(true);
      btnAgregadosEdit.setEnabled(true);
      btnAgregadosAdd.setEnabled(true);
      txtPaymentImport.setEnabled(true);
      txtPaymentsConcept.setEnabled(true);
      btnPaymentsSave.setEnabled(true);
      btnPaymentsDelete.setEnabled(true);
      btnPaymentsEdit.setEnabled(true);
      btnEdit.setEnabled(false);
      btnSave.setEnabled(true);
    }
    
    private void disableForm () {
      this.dateChooserDeliveryDate.setEnabled(false);
      this.dateChooserReturnDate.setEnabled(false);
      this.cmbCatalogType.setEnabled(false);
      this.cmbStatus.setEnabled(false);
      this.txtDeliveryHour.setEnabled(false);
      this.txtReturnHour.setEnabled(false);
      this.txtAreaDescription.setEnabled(false);
      this.tableFormatDetail.setEnabled(false);
      this.tableFormatPayments.setEnabled(false);
      btnAgregadosDelete.setEnabled(false);
      btnAgregadosEdit.setEnabled(false);
      btnAgregadosAdd.setEnabled(false);
      txtPaymentImport.setEnabled(false);
      txtPaymentsConcept.setEnabled(false);
      btnPaymentsSave.setEnabled(false);
      btnPaymentsDelete.setEnabled(false);
      btnPaymentsEdit.setEnabled(false);
      btnEdit.setVisible(true);
      btnEdit.setEnabled(true);
      btnSave.setEnabled(false);
    }
    
    private void save () {
        try {
            getEventDateFromInputs();
            getEventFromInputs();
            getDetailEventFromTable();
            getPaymentsFromTable();
            eventService.save(event,detailEvent,paymentsEvent);        
            this.eventId = this.event.getId();
            initFormWithExistentEvent();
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), MESSAGE_TITLE_ERROR, JOptionPane.ERROR_MESSAGE);  
        }
    }
    
    private void total () {
        float detailTotal=0;
        float paymentsTotal=0;
        for (int i = 0 ; i < tableFormatDetail.getRowCount() ; i++) {
            detailTotal += 
                    Float.parseFloat(
                            Utility.deleteCharacters(String.valueOf(tableFormatDetail.getValueAt(i, TableFormatDetail.Column.IMPORT.getNumber())),DELETE_CHARS_NUMBER));
        }
        for (int i = 0 ; i < tableFormatPayments.getRowCount() ; i++) {
            paymentsTotal += 
                    Float.parseFloat(
                            Utility.deleteCharacters(String.valueOf(tableFormatPayments.getValueAt(i, TableFormatPayments.Column.IMPORT.getNumber())),DELETE_CHARS_NUMBER));
        }
        this.lblSubTotal.setText(decimalFormat.format(detailTotal));
        this.lblPayments.setText(decimalFormat.format(paymentsTotal));
        this.lblTotal.setText(decimalFormat.format(detailTotal-paymentsTotal));
    }
    
    private void fillCatalogTypeEventService () {
        if (types.isEmpty()) {
            try {
                types = catalogTypeEventService.getAll();
            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(this, e, MESSAGE_TITLE_ERROR, JOptionPane.ERROR_MESSAGE);  
            }
        }
        if (status.isEmpty()) {
            try {
                status = catalogStatusEventService.getAll();
            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(this, e, MESSAGE_TITLE_ERROR, JOptionPane.ERROR_MESSAGE);  
            }
        }
        cmbCatalogType.removeAllItems();
        types.stream().forEach(t -> {
            cmbCatalogType.addItem(t);
        });
        
        
        cmbStatus.removeAllItems();
        status.stream().forEach(t -> {
            cmbStatus.addItem(t);
        });
        
    }
    
    private void initFormWithExistentEvent () {
        
        fillCatalogTypeEventService();
        lblInfoUser.setText("Atendió:");
        try {
            this.event = eventService.findById(this.eventId);
            lblCustomer.setText(this.event.getCustomer().getName()+" "+this.event.getCustomer().getLastName());
            lblUser.setText(this.event.getUser().getName()+" "+this.event.getUser().getLastName());
            lblEventCreatedAt.setText("Fecha de elaboración: "+fastDateFormatLarge.format(this.event.getCreatedAt()));
            tabPaneMain.setSelectedIndex(INDEX_EVENT_PANE);
            disableForm();
            btnEventAdd.setEnabled(true);
            lblFolio.setText(String.valueOf(event.getId()));

            fillInputsFormFromEvent();
            cmbCatalogType.setSelectedItem(this.event.getCatalogTypeEvent());
            cmbStatus.setSelectedItem(this.event.getCatalogStatusEvent());
            
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
    
    private void fillTableDetailFromEvent () {
        try {
            this.detailEvent = detailEventService.getAll(this.event.getId());
            this.tableFormatDetail.format();
            for (DetailEvent detail : this.detailEvent) {
                addDetailToTable(detail,null);
            }
        } catch (BusinessException e) {
            log.error(e);
            JOptionPane.showMessageDialog(this, e.getMessage(), MESSAGE_TITLE_ERROR, JOptionPane.ERROR_MESSAGE); 
        }
    }
    
    private void fillTablePaymentsFromEventId () {
        this.paymentsEvent = new ArrayList<>();
        try {
            this.paymentsEvent = paymentService.getAll(this.event.getId());
            fillTablePayments(this.paymentsEvent);
        } catch (BusinessException e) {
            log.error(e);
            JOptionPane.showMessageDialog(this, e.getMessage(), MESSAGE_TITLE_ERROR, JOptionPane.ERROR_MESSAGE); 
        }
        
        
    }
    
    private void initFormWithNewEvent () {
        lblInfoUser.setText("Atiende:");
        lblEventCreatedAt.setText(EMPTY_STRING_TXT_FIELD);
        btnEdit.setVisible(false);
        event = new Event();
        tabPaneMain.setEnabledAt(INDEX_EVENT_PANE, false);
        tabPaneMain.setSelectedIndex(INDEX_CUSTOMER_PANE);
        Utility.setTimeout(() -> this.txtName.requestFocus(), 500);
        lblUser.setText(LoginForm.userSession.getName() + " " + LoginForm.userSession.getLastName());
        paymentsEvent = new ArrayList<>();
        this.dateChooserDeliveryDate.setDate(new Date());
    }
    
    private void addActionListenerToDateChoosers () {
        dateChooserDeliveryDate.getDateEditor().addPropertyChangeListener((PropertyChangeEvent e) -> {
            if (DATE_VALUE_CHOOSER.equals(e.getPropertyName())) {
                txtDeliveryHour.requestFocus();
            }
        });
        dateChooserReturnDate.getDateEditor().addPropertyChangeListener((PropertyChangeEvent e) -> {
            if (DATE_VALUE_CHOOSER.equals(e.getPropertyName())) {
                txtReturnHour.requestFocus();
            }
        });
    }
    
    private void addMouseListenerToTablePayments () {
        tableFormatPayments.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                editPaymentForm();                
              }
            }
          });
    }
    
    private void addMouseListenerToTableDetail () {
        tableFormatDetail.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                editDetail();                
              }
            }
          });
    }
    
    // init event
    private void addMouseListenerToTableCustomers () {
        // double click in customers table
        customersTableFormat.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                JTable target = (JTable)e.getSource();
                int row = target.getSelectedRow();
                Optional<Customer> opCustomer = customers.stream()
                        .filter(t -> t.getId().equals(Long.parseLong(String.valueOf(target.getValueAt(row, TableFormatCustomers.Column.ID.getNumber())))))
                        .findFirst();
                
                if (opCustomer.isPresent()) {
                    event.setCustomer(opCustomer.get());
                    tabPaneMain.setEnabledAt(INDEX_EVENT_PANE, true);
                    tabPaneMain.setSelectedIndex(INDEX_EVENT_PANE);
                    cleanCustomerInputs();
                    lblCustomer.setText(opCustomer.get().getName() + " " + opCustomer.get().getLastName());
                    new Thread(() -> {
                        fillCatalogTypeEventService();
                    }).start();
                }
                
              }
            }
          });
    }
    
    private void cleanCustomerInputs () {
        this.txtName.setText(EMPTY_STRING_TXT_FIELD);
        this.txtLastName.setText(EMPTY_STRING_TXT_FIELD);
        this.txtPhoneNumber1.setText(EMPTY_STRING_TXT_FIELD);
        this.txtPhoneNumber2.setText(EMPTY_STRING_TXT_FIELD);
        this.txtPhoneNumber3.setText(EMPTY_STRING_TXT_FIELD);
    }
    
    private Customer getCustomerFromInputs () {
        return Customer.builder()            
            .name(txtName.getText())
            .lastName(txtLastName.getText())
            .email(txtEmail.getText())
            .phoneNumber1(txtPhoneNumber1.getText())
            .phoneNumber2(txtPhoneNumber2.getText())
            .phoneNumber3(txtPhoneNumber3.getText())
            .build();
    }
    
    private void saveCustomer () {
        
        try {
            Customer customerFromInputs = getCustomerFromInputs();
            customerService.saveOrUpdate(customerFromInputs);
            event.setCustomer(customerFromInputs);
            tabPaneMain.setEnabledAt(INDEX_EVENT_PANE, true);
            tabPaneMain.setSelectedIndex(INDEX_EVENT_PANE);
            lblCustomer.setText(customerFromInputs.getName()+" "+customerFromInputs.getLastName());
            btnSave.setEnabled(true);
            cleanCustomerInputs();
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    ApplicationConstants.MESSAGE_MISSING_PARAMETERS, JOptionPane.ERROR_MESSAGE);
        } finally {
        }

    }
    private void fillCustomers (List<Customer> customerList) {
        customersTableFormat.format();
        DefaultTableModel tableModel = (DefaultTableModel) customersTableFormat.getModel();
            
        for (Customer customer : customerList) {
            Object fila[] = {
                customer.getId(),
                customer.getName()+ " " + customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber1(),
                customer.getPhoneNumber2(),
                customer.getPhoneNumber3(),
                customer.getCreatedAt()
            };
            tableModel.addRow(fila);
        }
    }
    private void getCustomers () {
        try {
            customers = customerService.getAll();
            fillCustomers(customers);
        } catch (NoDataFoundException e) {
            // nothing to do
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e, ApplicationConstants.MESSAGE_UNEXPECTED_ERROR, JOptionPane.ERROR_MESSAGE);
        } finally {
            this.txtName.requestFocus();
        }
    }
        
    private void searchAndFillTableCustomers () {
        try {
            List<Customer> filterCustomers =
                    customers.stream()
                            .filter(customer -> Objects.nonNull(customer))
                            .filter(customer -> Objects.nonNull(customer.getName()))
                            .filter(customer -> Objects.nonNull(customer.getLastName()))
                            .filter(customer -> Utility.removeAccents(customer.getName().toLowerCase().trim()).contains(txtName.getText().toLowerCase().trim()))
                            .filter(customer -> Utility.removeAccents(customer.getLastName().toLowerCase().trim()).contains(txtLastName.getText().toLowerCase().trim()))
                            .collect(Collectors.toList());
            fillCustomers(filterCustomers);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
            log.error(e.getMessage(),e);
        }
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPaneMain = new javax.swing.JTabbedPane();
        panelCustomers = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtLastName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPhoneNumber1 = new javax.swing.JTextField();
        txtPhoneNumber2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPhoneNumber3 = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnSaveCustomer = new javax.swing.JButton();
        paneCustomersTable = new javax.swing.JPanel();
        panelEvent = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblInfoUser = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblFolio = new javax.swing.JLabel();
        lblCustomer = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lblSubTotal = new javax.swing.JLabel();
        lblPayments = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelInnerAdds = new javax.swing.JPanel();
        panelInnerTableAgregados = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnAgregadosEdit = new javax.swing.JButton();
        btnAgregadosAdd = new javax.swing.JButton();
        btnAgregadosDelete = new javax.swing.JButton();
        btnCopyDetailRow = new javax.swing.JButton();
        panelInnerPayments = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btnPaymentsEdit = new javax.swing.JButton();
        btnPaymentsDelete = new javax.swing.JButton();
        btnPaymentsSave = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtPaymentsConcept = new javax.swing.JTextField();
        txtPaymentImport = new javax.swing.JFormattedTextField();
        panelInnerPaymetsTable = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        dateChooserDeliveryDate = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        dateChooserReturnDate = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        cmbCatalogType = new javax.swing.JComboBox<>();
        txtDeliveryHour = new javax.swing.JFormattedTextField();
        txtReturnHour = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnGeneratePDF = new javax.swing.JButton();
        btnEventAdd = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaDescription = new javax.swing.JTextPane();
        cmbStatus = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        lblEventCreatedAt = new javax.swing.JLabel();

        panelCustomers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel1.setText("Nombre:");

        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNameKeyReleased(evt);
            }
        });

        txtLastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLastNameKeyReleased(evt);
            }
        });

        jLabel2.setText("Apellidos:");

        jLabel4.setText("Teléfono:");

        txtPhoneNumber1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneNumber1KeyPressed(evt);
            }
        });

        txtPhoneNumber2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneNumber2KeyPressed(evt);
            }
        });

        jLabel5.setText("Teléfono2:");

        jLabel6.setText("Teléfono3:");

        txtPhoneNumber3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneNumber3KeyPressed(evt);
            }
        });

        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailKeyPressed(evt);
            }
        });

        jLabel7.setText("Email:");

        btnSaveCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/diskette-32.png"))); // NOI18N
        btnSaveCustomer.setToolTipText("Guardar");
        btnSaveCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(txtLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel7)
                        .addComponent(jLabel6)
                        .addComponent(txtName)
                        .addComponent(txtPhoneNumber1)
                        .addComponent(txtPhoneNumber2)
                        .addComponent(txtPhoneNumber3)
                        .addComponent(txtEmail))
                    .addComponent(btnSaveCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPhoneNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPhoneNumber2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPhoneNumber3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSaveCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(92, 92, 92))
        );

        javax.swing.GroupLayout paneCustomersTableLayout = new javax.swing.GroupLayout(paneCustomersTable);
        paneCustomersTable.setLayout(paneCustomersTableLayout);
        paneCustomersTableLayout.setHorizontalGroup(
            paneCustomersTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 796, Short.MAX_VALUE)
        );
        paneCustomersTableLayout.setVerticalGroup(
            paneCustomersTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelCustomersLayout = new javax.swing.GroupLayout(panelCustomers);
        panelCustomers.setLayout(panelCustomersLayout);
        panelCustomersLayout.setHorizontalGroup(
            panelCustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paneCustomersTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        panelCustomersLayout.setVerticalGroup(
            panelCustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paneCustomersTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelCustomersLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tabPaneMain.addTab("Clientes", new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/servicio-al-cliente-32.png")), panelCustomers); // NOI18N

        panelEvent.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel12.setText("Folio:");

        jLabel13.setText("Cliente:");

        lblInfoUser.setText("Atiende:");

        jLabel15.setText("Sub total:");

        jLabel16.setText("Abonos:");

        jLabel17.setText("Total:");

        lblSubTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblPayments.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblFolio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(lblPayments, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblInfoUser)
                        .addGap(8, 8, 8)
                        .addComponent(lblUser, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(2, 2, 2)
                        .addComponent(lblSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblFolio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblInfoUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPayments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout panelInnerTableAgregadosLayout = new javax.swing.GroupLayout(panelInnerTableAgregados);
        panelInnerTableAgregados.setLayout(panelInnerTableAgregadosLayout);
        panelInnerTableAgregadosLayout.setHorizontalGroup(
            panelInnerTableAgregadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 922, Short.MAX_VALUE)
        );
        panelInnerTableAgregadosLayout.setVerticalGroup(
            panelInnerTableAgregadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 264, Short.MAX_VALUE)
        );

        btnAgregadosEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/edit-32.png"))); // NOI18N
        btnAgregadosEdit.setToolTipText("Editar");
        btnAgregadosEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregadosEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregadosEditActionPerformed(evt);
            }
        });

        btnAgregadosAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/add-32.png"))); // NOI18N
        btnAgregadosAdd.setToolTipText("Agregar detalle");
        btnAgregadosAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregadosAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregadosAddActionPerformed(evt);
            }
        });

        btnAgregadosDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/delete-32.png"))); // NOI18N
        btnAgregadosDelete.setToolTipText("Eliminar");
        btnAgregadosDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregadosDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregadosDeleteActionPerformed(evt);
            }
        });

        btnCopyDetailRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/copiar-32.png"))); // NOI18N
        btnCopyDetailRow.setToolTipText("Clonar filas seleccionadas");
        btnCopyDetailRow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCopyDetailRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyDetailRowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAgregadosAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAgregadosEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAgregadosDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCopyDetailRow, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAgregadosAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregadosEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregadosDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCopyDetailRow)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelInnerAddsLayout = new javax.swing.GroupLayout(panelInnerAdds);
        panelInnerAdds.setLayout(panelInnerAddsLayout);
        panelInnerAddsLayout.setHorizontalGroup(
            panelInnerAddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInnerAddsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelInnerTableAgregados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelInnerAddsLayout.setVerticalGroup(
            panelInnerAddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInnerAddsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInnerAddsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelInnerTableAgregados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Agregados", panelInnerAdds);

        panelInnerPayments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelInnerPaymentsMouseClicked(evt);
            }
        });
        panelInnerPayments.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                panelInnerPaymentsKeyPressed(evt);
            }
        });

        btnPaymentsEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/edit-32.png"))); // NOI18N
        btnPaymentsEdit.setToolTipText("Editar");
        btnPaymentsEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaymentsEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentsEditActionPerformed(evt);
            }
        });

        btnPaymentsDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/delete-32.png"))); // NOI18N
        btnPaymentsDelete.setToolTipText("Eliminar");
        btnPaymentsDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaymentsDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentsDeleteActionPerformed(evt);
            }
        });

        btnPaymentsSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/diskette-32.png"))); // NOI18N
        btnPaymentsSave.setToolTipText("Guardar");
        btnPaymentsSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaymentsSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentsSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(btnPaymentsSave, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPaymentsEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPaymentsDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPaymentsEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPaymentsDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPaymentsSave)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel19.setText("Importe:");

        jLabel20.setText("Concepto:");

        txtPaymentsConcept.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPaymentsConceptKeyPressed(evt);
            }
        });

        txtPaymentImport.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPaymentImport.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPaymentImportKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPaymentsConcept)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addGap(0, 189, Short.MAX_VALUE))
                    .addComponent(txtPaymentImport))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPaymentImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPaymentsConcept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(154, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelInnerPaymetsTableLayout = new javax.swing.GroupLayout(panelInnerPaymetsTable);
        panelInnerPaymetsTable.setLayout(panelInnerPaymetsTableLayout);
        panelInnerPaymetsTableLayout.setHorizontalGroup(
            panelInnerPaymetsTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 649, Short.MAX_VALUE)
        );
        panelInnerPaymetsTableLayout.setVerticalGroup(
            panelInnerPaymetsTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelInnerPaymentsLayout = new javax.swing.GroupLayout(panelInnerPayments);
        panelInnerPayments.setLayout(panelInnerPaymentsLayout);
        panelInnerPaymentsLayout.setHorizontalGroup(
            panelInnerPaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInnerPaymentsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelInnerPaymetsTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelInnerPaymentsLayout.setVerticalGroup(
            panelInnerPaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInnerPaymentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInnerPaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelInnerPaymetsTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Abonos", panelInnerPayments);

        jLabel3.setText("Fecha y hora de entrega:");

        jLabel8.setText("Fecha y hora de devolución:");

        jLabel9.setText("Tipo:");

        try {
            txtDeliveryHour.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtReturnHour.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel10.setText("Hrs.");

        jLabel11.setText("Hrs.");

        jLabel18.setText("Descripción:");

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/edit-32.png"))); // NOI18N
        btnEdit.setToolTipText("Editar");
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/diskette-32.png"))); // NOI18N
        btnSave.setToolTipText("Guardar");
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnGeneratePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/descargar-pdf-32.png"))); // NOI18N
        btnGeneratePDF.setToolTipText("Editar");
        btnGeneratePDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGeneratePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneratePDFActionPerformed(evt);
            }
        });

        btnEventAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/add-32.png"))); // NOI18N
        btnEventAdd.setToolTipText("Nuevo evento");
        btnEventAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEventAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEventAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGeneratePDF, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEventAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(btnSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGeneratePDF, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(btnEventAdd)
        );

        jScrollPane2.setViewportView(txtAreaDescription);

        jLabel14.setText("Estado:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooserReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateChooserDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDeliveryHour, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtReturnHour, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cmbCatalogType, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2)))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(lblEventCreatedAt, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooserDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtDeliveryHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10)))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooserReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtReturnHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(cmbCatalogType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblEventCreatedAt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(32, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout panelEventLayout = new javax.swing.GroupLayout(panelEvent);
        panelEvent.setLayout(panelEventLayout);
        panelEventLayout.setHorizontalGroup(
            panelEventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEventLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEventLayout.createSequentialGroup()
                        .addGroup(panelEventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPane1)
                            .addGroup(panelEventLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(panelEventLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(252, 252, 252))))
        );
        panelEventLayout.setVerticalGroup(
            panelEventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEventLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        tabPaneMain.addTab("Evento", new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/fiesta-de-cumpleanos-32.png")), panelEvent); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabPaneMain, javax.swing.GroupLayout.PREFERRED_SIZE, 1042, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPaneMain)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCustomerActionPerformed
        saveCustomer();
    }//GEN-LAST:event_btnSaveCustomerActionPerformed

    private void txtPhoneNumber1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNumber1KeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            saveCustomer();
        }
    }//GEN-LAST:event_txtPhoneNumber1KeyPressed

    private void txtPhoneNumber2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNumber2KeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            saveCustomer();
        }
    }//GEN-LAST:event_txtPhoneNumber2KeyPressed

    private void txtPhoneNumber3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNumber3KeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            saveCustomer();
        }
    }//GEN-LAST:event_txtPhoneNumber3KeyPressed

    private void txtEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            saveCustomer();
        }
    }//GEN-LAST:event_txtEmailKeyPressed

    private void txtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyReleased
        searchAndFillTableCustomers();
    }//GEN-LAST:event_txtNameKeyReleased

    private void txtLastNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLastNameKeyReleased
        searchAndFillTableCustomers();
    }//GEN-LAST:event_txtLastNameKeyReleased

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        enableForm();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private DetailEvent getDetailEventFromRowSelected () {
        return
                DetailEvent.builder()
                        .id(
                                Long.parseLong(String.valueOf(tableFormatDetail.getValueAt(
                                    tableFormatDetail.getSelectedRow(), TableFormatDetail.Column.ID.getNumber()))
                        ))
                        .nameOfAggregate(
                                String.valueOf(tableFormatDetail.getValueAt(
                                    tableFormatDetail.getSelectedRow(), TableFormatDetail.Column.NAME.getNumber())
                        )).items(
                                String.valueOf(tableFormatDetail.getValueAt(
                                    tableFormatDetail.getSelectedRow(), TableFormatDetail.Column.ITEMS.getNumber())
                        ))
                        .adjustments(
                                String.valueOf(tableFormatDetail.getValueAt(
                                    tableFormatDetail.getSelectedRow(), TableFormatDetail.Column.ADJUTS.getNumber())
                        )).unitPrice(
                                Float.parseFloat(String.valueOf(tableFormatDetail.getValueAt(
                                    tableFormatDetail.getSelectedRow(), TableFormatDetail.Column.IMPORT.getNumber()))
                        )).advancePayment(
                                Float.parseFloat(String.valueOf(tableFormatDetail.getValueAt(
                                    tableFormatDetail.getSelectedRow(), TableFormatDetail.Column.PAYMENT.getNumber()))
                        ))
                        .build();
    }
    
    private void editDetail () {
        if (this.tableFormatDetail.getSelectedRow() == - 1) {
            JOptionPane.showMessageDialog(this, 
                    ApplicationConstants.SELECT_A_ROW_NECCESSARY, MESSAGE_TITLE_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }
         
        DetailEvent detailEvent = getDetailEventFromRowSelected();
        showDetailEventAddToTableDialog(detailEvent,String.valueOf(this.tableFormatDetail.getSelectedRow()));
    }
    
    private void btnAgregadosEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregadosEditActionPerformed

        editDetail();
    }//GEN-LAST:event_btnAgregadosEditActionPerformed

    private void addDetailToTable (final DetailEvent detail, String referenceRow) {
        Float total = 
                    detail.getUnitPrice() - (detail.getAdvancePayment() != null ? detail.getAdvancePayment() : 0F);
        if (referenceRow == null) {
            DefaultTableModel temp = (DefaultTableModel) tableFormatDetail.getModel();
            
            Object row[] = {
                false,
                detail.getId() != null ? detail.getId() : "0",
                detail.getNameOfAggregate(),
                detail.getItems(),
                detail.getAdjustments(),
                detail.getUnitPrice(),
                detail.getAdvancePayment(),
                total
            };
            temp.addRow(row);
        } else {
            tableFormatDetail.setValueAt(detail.getId() != null ? detail.getId() : "0"
                    , Integer.parseInt(referenceRow),TableFormatDetail.Column.ID.getNumber() );
            
            tableFormatDetail.setValueAt(detail.getNameOfAggregate()
                    , Integer.parseInt(referenceRow),TableFormatDetail.Column.NAME.getNumber() );
            
            tableFormatDetail.setValueAt(detail.getItems(),
                    Integer.parseInt(referenceRow),TableFormatDetail.Column.ITEMS.getNumber() );
            
            tableFormatDetail.setValueAt(detail.getAdjustments(),
                    Integer.parseInt(referenceRow),TableFormatDetail.Column.ADJUTS.getNumber() );
            
            tableFormatDetail.setValueAt(detail.getUnitPrice(),
                    Integer.parseInt(referenceRow),TableFormatDetail.Column.IMPORT.getNumber() );
            
            tableFormatDetail.setValueAt(detail.getAdvancePayment() != null ? detail.getAdvancePayment() : "0",
                    Integer.parseInt(referenceRow),TableFormatDetail.Column.PAYMENT.getNumber() );
            
            tableFormatDetail.setValueAt(total,
                    Integer.parseInt(referenceRow),TableFormatDetail.Column.TOTAL.getNumber() );
        }
        total();
    }
    
    private void showDetailEventAddToTableDialog (DetailEvent detail, String referenceRow) {
        Frame f = JOptionPane.getFrameForComponent(this);
        
        DetailEventAddToTableDialog dialog = 
               new DetailEventAddToTableDialog(f, true, detail);
               
       DetailEvent restultDetail = dialog.showDialog();
       addDetailToTable(restultDetail,referenceRow);
    }
    
    private void btnAgregadosAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregadosAddActionPerformed
       showDetailEventAddToTableDialog(null,null);        
    }//GEN-LAST:event_btnAgregadosAddActionPerformed

    private void editPaymentForm () {
        if (this.tableFormatPayments.getSelectedRow() == - 1) {
            JOptionPane.showMessageDialog(this, 
                    ApplicationConstants.SELECT_A_ROW_NECCESSARY, MESSAGE_TITLE_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtPaymentImport.setText(
                String.valueOf(
                       tableFormatPayments.getValueAt(
                               this.tableFormatPayments.getSelectedRow(),
                               TableFormatPayments.Column.IMPORT.getNumber()
                               )
                )
        );
        
        txtPaymentsConcept.setText(
                String.valueOf(
                       tableFormatPayments.getValueAt(
                               this.tableFormatPayments.getSelectedRow(),
                               TableFormatPayments.Column.CONCEPT.getNumber()
                               )
                )
        );
        
        referenceRowToEditPaymentTable = String.valueOf(
                       this.tableFormatPayments.getSelectedRow()
                );
    }
    
    private void btnPaymentsEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentsEditActionPerformed
        editPaymentForm();
        
    }//GEN-LAST:event_btnPaymentsEditActionPerformed

    private void btnPaymentsSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentsSaveActionPerformed
       addRowToPaymentTable();
    }//GEN-LAST:event_btnPaymentsSaveActionPerformed

    private void addRowToPaymentTable () {
        
        if (txtPaymentImport.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    ApplicationConstants.MISSING_DATA, MESSAGE_TITLE_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (referenceRowToEditPaymentTable != null) {
            
            tableFormatPayments.setValueAt(
                    txtPaymentImport.getText(), 
                    Integer.parseInt(referenceRowToEditPaymentTable),
                    TableFormatPayments.Column.IMPORT.getNumber());
            
            tableFormatPayments.setValueAt(
                    txtPaymentsConcept.getText(), 
                    Integer.parseInt(referenceRowToEditPaymentTable),
                    TableFormatPayments.Column.CONCEPT.getNumber());
        } else {
            DefaultTableModel temp = (DefaultTableModel) tableFormatPayments.getModel();
            Object row[] = {
                "0",
                txtPaymentImport.getText(),
                txtPaymentsConcept.getText(),
                fastDateFormatMedium.format(new Date()),
                LoginForm.userSession.getName() + " " + LoginForm.userSession.getLastName()
            };
            temp.addRow(row);
            
        }
        referenceRowToEditPaymentTable = null;
        cleanInputsPaymentForm();
        total();
    }
    
    private void enabledInputsPaymentForm () {
        txtPaymentImport.setEnabled(true);
        txtPaymentsConcept.setEnabled(true);
        txtPaymentImport.requestFocus();
    }
    
    private void cleanForm () {
        
        final Date now = new Date();
        
        lblFolio.setText(EMPTY_STRING_TXT_FIELD);
        this.dateChooserDeliveryDate.setDate(now);
        this.dateChooserReturnDate.setDate(now);
        this.cmbCatalogType.setSelectedIndex(0);
        this.cmbStatus.setSelectedIndex(0);
        this.txtDeliveryHour.setText(EMPTY_STRING_TXT_FIELD);
        this.txtReturnHour.setText(EMPTY_STRING_TXT_FIELD);
        this.txtAreaDescription.setText(EMPTY_STRING_TXT_FIELD);
        this.tableFormatDetail.format();
        this.tableFormatPayments.format();
        total();
        
    }
    
    private void cleanInputsPaymentForm () {
        txtPaymentImport.setText(EMPTY_STRING_TXT_FIELD);
        txtPaymentsConcept.setText(EMPTY_STRING_TXT_FIELD);
        txtPaymentImport.requestFocus();
    }
    

    
    private void btnAgregadosDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregadosDeleteActionPerformed
        try {
            Utility.removeRowsCheckedTable(tableFormatDetail,
                    TableFormatDetail.Column.BOOLEAN.getNumber());
            total();
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, 
                    e.getMessage(), MESSAGE_TITLE_ERROR, JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregadosDeleteActionPerformed

    private void btnPaymentsDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentsDeleteActionPerformed
        
        try {
            onlyAdminUserAccess();
        } catch (UnAuthorizedException e) {
            JOptionPane.showMessageDialog(this, 
                    e.getMessage(), MESSAGE_TITLE_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }
        
               
        if (this.tableFormatPayments.getSelectedRow() == - 1) {
            JOptionPane.showMessageDialog(this, 
                    ApplicationConstants.SELECT_A_ROW_NECCESSARY, MESSAGE_TITLE_ERROR, JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!paymentsEvent.isEmpty()) {
            Long id = 
                Long.parseLong(
                        String.valueOf(this.tableFormatPayments.getValueAt(this.tableFormatPayments.getSelectedRow(), TableFormatPayments.Column.ID.getNumber())));
            paymentsEvent.stream()
                    .filter(t -> t.getId().equals(id))
                    .peek(t -> t.setEnabled(false))
                    .findFirst();
                                
        }
        
        int seleccion = Utility.showConfirmDialogYesNo();
        if (seleccion == 0) {//presiono que si
           ((DefaultTableModel)tableFormatPayments.getModel()).removeRow(this.tableFormatPayments.getSelectedRow());
           total();
        }
        
        
        
    }//GEN-LAST:event_btnPaymentsDeleteActionPerformed

    private void btnGeneratePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneratePDFActionPerformed
        generateEventPDF();
    }//GEN-LAST:event_btnGeneratePDFActionPerformed

    private void txtPaymentImportKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaymentImportKeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            addRowToPaymentTable();
        }
    }//GEN-LAST:event_txtPaymentImportKeyPressed

    private void txtPaymentsConceptKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaymentsConceptKeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            addRowToPaymentTable();
        }
    }//GEN-LAST:event_txtPaymentsConceptKeyPressed

    private void panelInnerPaymentsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_panelInnerPaymentsKeyPressed
        
    }//GEN-LAST:event_panelInnerPaymentsKeyPressed

    private void panelInnerPaymentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelInnerPaymentsMouseClicked
        txtPaymentImport.requestFocus();
    }//GEN-LAST:event_panelInnerPaymentsMouseClicked
    private void newEventForm () {
        btnEventAdd.setEnabled(false);
        this.eventId = null;
        cleanCustomerInputs();
        cleanInputsPaymentForm();
        cleanForm();
        enableForm();
        initFormWithNewEvent();
    }
    private void btnEventAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEventAddActionPerformed
        newEventForm();
    }//GEN-LAST:event_btnEventAddActionPerformed

    private void btnCopyDetailRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyDetailRowActionPerformed
        try {
            Utility.cloneRowsCheckedTable(tableFormatDetail, TableFormatDetail.Column.BOOLEAN.getNumber(),
                    TableFormatDetail.Column.ID.getNumber());
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, 
                    e.getMessage(), MESSAGE_TITLE_ERROR, JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnCopyDetailRowActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregadosAdd;
    private javax.swing.JButton btnAgregadosDelete;
    private javax.swing.JButton btnAgregadosEdit;
    private javax.swing.JButton btnCopyDetailRow;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEventAdd;
    private javax.swing.JButton btnGeneratePDF;
    private javax.swing.JButton btnPaymentsDelete;
    private javax.swing.JButton btnPaymentsEdit;
    private javax.swing.JButton btnPaymentsSave;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveCustomer;
    private javax.swing.JComboBox<CatalogTypeEvent> cmbCatalogType;
    private javax.swing.JComboBox<CatalogStatusEvent> cmbStatus;
    private com.toedter.calendar.JDateChooser dateChooserDeliveryDate;
    private com.toedter.calendar.JDateChooser dateChooserReturnDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblCustomer;
    private javax.swing.JLabel lblEventCreatedAt;
    private javax.swing.JLabel lblFolio;
    private javax.swing.JLabel lblInfoUser;
    private javax.swing.JLabel lblPayments;
    private javax.swing.JLabel lblSubTotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel paneCustomersTable;
    private javax.swing.JPanel panelCustomers;
    private javax.swing.JPanel panelEvent;
    private javax.swing.JPanel panelInnerAdds;
    private javax.swing.JPanel panelInnerPayments;
    private javax.swing.JPanel panelInnerPaymetsTable;
    private javax.swing.JPanel panelInnerTableAgregados;
    private javax.swing.JTabbedPane tabPaneMain;
    private javax.swing.JTextPane txtAreaDescription;
    private javax.swing.JFormattedTextField txtDeliveryHour;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtName;
    private javax.swing.JFormattedTextField txtPaymentImport;
    private javax.swing.JTextField txtPaymentsConcept;
    private javax.swing.JTextField txtPhoneNumber1;
    private javax.swing.JTextField txtPhoneNumber2;
    private javax.swing.JTextField txtPhoneNumber3;
    private javax.swing.JFormattedTextField txtReturnHour;
    // End of variables declaration//GEN-END:variables
}
