package alquiler.trajes.form.event;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.EMPTY_STRING_TXT_FIELD;
import static alquiler.trajes.constant.ApplicationConstants.ENTER_KEY;
import static alquiler.trajes.constant.ApplicationConstants.MESSAGE_NUMBER_FORMAT_ERROR;
import static alquiler.trajes.constant.ApplicationConstants.PATH_NAME_DETAIL_REPORT_A4_HORIZONTAL;
import static alquiler.trajes.constant.ApplicationConstants.PDF_NAME_DETAIL_REPORT_A4_HORIZONTAL;
import static alquiler.trajes.constant.ApplicationConstants.SELECT_A_ROW_NECCESSARY;
import static alquiler.trajes.constant.ApplicationConstants.TITLE_CONSULT_EVENTS_FORM;
import alquiler.trajes.entity.CatalogStatusEvent;
import alquiler.trajes.entity.CatalogTypeEvent;
import alquiler.trajes.entity.DetailEvent;
import alquiler.trajes.entity.Event;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.InvalidDataException;
import alquiler.trajes.model.params.EventParameter;
import alquiler.trajes.model.params.result.EventResult;
import alquiler.trajes.service.CatalogStatusEventService;
import alquiler.trajes.service.CatalogTypeEventService;
import alquiler.trajes.service.DetailEventService;
import alquiler.trajes.service.EventResultService;
import alquiler.trajes.service.EventService;
import alquiler.trajes.service.PaymentService;
import alquiler.trajes.tables.TableConsultEvents;
import alquiler.trajes.ticket.EventTicket;
import alquiler.trajes.ticket.TicketTemplate;
import alquiler.trajes.util.JasperPrintUtil;
import alquiler.trajes.util.Utility;
import alquiler.trajes.util.UtilityDate;
import alquiler.trajes.util.UtilityTable;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.print.PrintException;
import javax.swing.JOptionPane;
import javax.swing.JTable;


public class ConsultEventsForm extends javax.swing.JInternalFrame {

    private final TableConsultEvents tableConsultEvents;
    private final EventResultService eventResultService;
    private final EventService eventService;
    private final DetailEventService detailEventService;
    private final CatalogStatusEventService catalogStatusEventService;
    private final CatalogTypeEventService catalogTypeEventService;
    private final JasperPrintUtil jasperPrintUtil;
    private final PaymentService paymentService;
    private final UtilityDate utilityDate;
    
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ConsultEventsForm.class.getName());

    
    public ConsultEventsForm() {
        initComponents();
        this.setTitle(TITLE_CONSULT_EVENTS_FORM);
        this.setClosable(Boolean.TRUE);
        catalogStatusEventService = CatalogStatusEventService.getInstance();
        catalogTypeEventService = CatalogTypeEventService.getInstance();
        eventResultService = EventResultService.getInstance();
        eventService = EventService.getInstance();
        detailEventService = DetailEventService.getInstance();
        paymentService = PaymentService.getInstance();
        tableConsultEvents = new TableConsultEvents();
        jasperPrintUtil = JasperPrintUtil.getInstance();
        utilityDate = UtilityDate.getInstance();
        initCmbs();
        Utility.addJtableToPane(719, 451, this.panelTable, tableConsultEvents);        
        search(false);
        addEventListenerToTable();        
        setResizable(true);
        setIconifiable(true);
        setMaximizable(true);
    }
    
    private void initCmbs () {
        this.cmbLimit.removeAllItems();
        cmbLimit.addItem("100");
        cmbLimit.addItem("500");
        cmbLimit.addItem("1000");
        cmbLimit.addItem("10000");
        
        try {
            List<CatalogStatusEvent> status = catalogStatusEventService.getAll();
            cmbStatus.removeAllItems();

            cmbStatus.addItem(
                    new CatalogStatusEvent(0L, ApplicationConstants.CMB_SELECCIONE)
            );

            status.stream().forEach(t -> {
                cmbStatus.addItem(t);
            });
            
            List<CatalogTypeEvent> types = catalogTypeEventService.getAll();
            cmbCatalogType.removeAllItems();

            cmbCatalogType.addItem(
                    new CatalogTypeEvent(0L, ApplicationConstants.CMB_SELECCIONE)
            );

            types.stream().forEach(t -> {
                cmbCatalogType.addItem(t);
            });
            
            
            
        } catch (BusinessException businessException) {
           log.error(businessException);
           JOptionPane.showMessageDialog(
                   this, businessException.getMessage(),
                   ApplicationConstants.MESSAGE_UNEXPECTED_ERROR,
                   JOptionPane.ERROR_MESSAGE);  
        }
    }
    
    private void generateEventsFoliosPDF () {
         int LIMIT_ROWS_SELECTED = 100;         
         Map<String,Object> parameters = new HashMap<>();
         try {
             List<String> ids = getEventIdsFromTable();
             if (ids.size() > LIMIT_ROWS_SELECTED) {
                JOptionPane.showMessageDialog(this, "Limite de folios seleccionados. Selecciona menos de: "+LIMIT_ROWS_SELECTED+" folios.", "Reporte", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
             parameters.put("IDS", String.join(",", ids));
             parameters.put("TITLE", "Informe por folios.");             
             jasperPrintUtil.showPDF(parameters, PATH_NAME_DETAIL_REPORT_A4_HORIZONTAL, PDF_NAME_DETAIL_REPORT_A4_HORIZONTAL);
         } catch (BusinessException e) {
           log.error(e);
           JOptionPane.showMessageDialog(
                   this, e.getMessage(),
                   ApplicationConstants.MESSAGE_UNEXPECTED_ERROR,
                   JOptionPane.ERROR_MESSAGE);  
        }
    }
    private void generateTicket () {
        try {
            String id = Utility.getEventIdFromTableOnlyOneRowSelected(
                    tableConsultEvents,
                    TableConsultEvents.Column.BOOLEAN.getNumber(),
                    TableConsultEvents.Column.ID.getNumber());
            
            Float sumPayment = paymentService.getPaymentsByEvent(Long.parseLong(id));
            Float sumDetail = detailEventService.getSubtotalByEvent(Long.parseLong(id));
            
            final Event event = eventService.findById(Long.parseLong(id));
            final List<DetailEvent> detailEvent = detailEventService.getAll(Long.parseLong(id));
            
            TicketTemplate ticket = new EventTicket(
                    event,
                    detailEvent,
                    sumDetail,
                    sumPayment
            );
            
            ticket.generateTicket();
            
        } catch (BusinessException | PrintException e) {
            log.error(e);
           JOptionPane.showMessageDialog(
                   this, e.getMessage(),
                   ApplicationConstants.MESSAGE_UNEXPECTED_ERROR,
                   JOptionPane.ERROR_MESSAGE);  
        }
    }
    private void generateEventPDF () {
        
        try {
            
            String id = Utility.getEventIdFromTableOnlyOneRowSelected(
                    tableConsultEvents,
                    TableConsultEvents.Column.BOOLEAN.getNumber(),
                    TableConsultEvents.Column.ID.getNumber());
            
            final Event event = eventService.findById(Long.parseLong(id));       
            jasperPrintUtil.printEventPDF(event);        
            
        } catch (BusinessException e) {
            log.error(e);
           JOptionPane.showMessageDialog(
                   this, e.getMessage(),
                   ApplicationConstants.MESSAGE_UNEXPECTED_ERROR,
                   JOptionPane.ERROR_MESSAGE);  
        }
    }
    
    private void addEventListenerToTable () {
        tableConsultEvents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                  
                    JTable target = (JTable)e.getSource();
                    showEventForm(
                            String.valueOf(target.getValueAt(target.getSelectedRow(),
                                    TableConsultEvents.Column.ID.getNumber())));
                    
              }
            }
        });
    }
    
    private EventParameter getEventParameterFromInputs () throws InvalidDataException{
        
        if (!txtFolio.getText().isEmpty()) {
            try {
                Long.parseLong(txtFolio.getText());
            } catch (NumberFormatException e) {
                throw new InvalidDataException(MESSAGE_NUMBER_FORMAT_ERROR);                
            }
        }
        
        return 
                EventParameter.builder()
                        .initActivityDate(utilityDate.getStartDateFormatSqlQuery(dateChooserInitActivityDate.getDate()))
                        .endActivityDate(utilityDate.getEndDateFormatSqlQuery(this.dateChooserEndActivityDate.getDate()))
                        .initDeliveryDate(utilityDate.getStartDateFormatSqlQuery(dateChooserInitDeliveryDate.getDate()))
                        .endDeliveryDate(utilityDate.getEndDateFormatSqlQuery(this.dateChooserEndDeliveryDate.getDate()))
                        .initReturnDate(utilityDate.getStartDateFormatSqlQuery(this.dateChooserInitReturnDate.getDate()))
                        .endReturnDate(utilityDate.getEndDateFormatSqlQuery(this.dateChooserEndReturnDate.getDate()))
                        .customerName(txtCustomerName.getText().trim().toLowerCase())
                        .status((CatalogStatusEvent)cmbStatus.getModel().getSelectedItem())
                        .eventId(!txtFolio.getText().isEmpty() ? Long.parseLong(txtFolio.getText().trim()) : null)
                        .description(txtDescription.getText())
                        .limit(this.cmbLimit.getSelectedItem().toString())
                        .build();
    }
        
    private void search(boolean check){
        try {
            EventParameter eventParameter = getEventParameterFromInputs();
            if (!check) {
                // es primera consulta
                eventParameter.setInitDeliveryDate(utilityDate.getStartDateFormatSqlQuery(new Date()));
            }
            List<EventResult> results = eventResultService.getResult(eventParameter);
            
            UtilityTable.fillTableEvents(tableConsultEvents,results);
            
            if (results.isEmpty()) {
                lblInfo.setText("No se obtuvieron resultados.");
            } else {
                lblInfo.setText("Resultados obtenidos: "+results.size()+", límite de resultados: "+this.cmbLimit.getSelectedItem().toString());
            }
            
        } catch (BusinessException e) {
           log.error(e);
           JOptionPane.showMessageDialog(
                   this, e.getMessage(),
                   ApplicationConstants.MESSAGE_UNEXPECTED_ERROR,
                   JOptionPane.ERROR_MESSAGE);
        } finally {
            Toolkit.getDefaultToolkit().beep();
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        txtCustomerName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        btnCleanForm = new javax.swing.JButton();
        btnGoToEventForm = new javax.swing.JButton();
        btnGeneratePDF = new javax.swing.JButton();
        btnGeneratePDFDetail = new javax.swing.JButton();
        btnGenerateTicket = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        dateChooserInitReturnDate = new com.toedter.calendar.JDateChooser();
        dateChooserEndReturnDate = new com.toedter.calendar.JDateChooser();
        txtFolio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        cmbCatalogType = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbLimit = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        dateChooserInitActivityDate = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        dateChooserEndActivityDate = new com.toedter.calendar.JDateChooser();
        lblInfo = new javax.swing.JLabel();
        dateChooserInitDeliveryDate = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dateChooserEndDeliveryDate = new com.toedter.calendar.JDateChooser();
        panelTable = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(1070, 648));

        jLabel1.setText("Descripción:");

        txtDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescriptionKeyPressed(evt);
            }
        });

        txtCustomerName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCustomerNameKeyPressed(evt);
            }
        });

        jLabel2.setText("Cliente:");

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/search-32.png"))); // NOI18N
        btnSearch.setToolTipText("Buscar");
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnCleanForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/limpiar-32.png"))); // NOI18N
        btnCleanForm.setToolTipText("Limpiar formulario");
        btnCleanForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCleanForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanFormActionPerformed(evt);
            }
        });

        btnGoToEventForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/fiesta-de-cumpleanos-32.png"))); // NOI18N
        btnGoToEventForm.setToolTipText("Editar");
        btnGoToEventForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGoToEventForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoToEventFormActionPerformed(evt);
            }
        });

        btnGeneratePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/descargar-pdf-32.png"))); // NOI18N
        btnGeneratePDF.setToolTipText("Ver PDF");
        btnGeneratePDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGeneratePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneratePDFActionPerformed(evt);
            }
        });

        btnGeneratePDFDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/pdf-32.png"))); // NOI18N
        btnGeneratePDFDetail.setToolTipText("Ver PDF Folios");
        btnGeneratePDFDetail.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGeneratePDFDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneratePDFDetailActionPerformed(evt);
            }
        });

        btnGenerateTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/impresora-32.png"))); // NOI18N
        btnGenerateTicket.setToolTipText("Generar ticket");
        btnGenerateTicket.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGenerateTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateTicketActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCleanForm, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGoToEventForm, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGeneratePDF, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGeneratePDFDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGenerateTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGeneratePDFDetail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCleanForm, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGoToEventForm, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGeneratePDF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerateTicket, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel5.setText("Fecha de devolución:");

        txtFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFolioActionPerformed(evt);
            }
        });
        txtFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFolioKeyPressed(evt);
            }
        });

        jLabel6.setText("Folio:");

        cmbStatus.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cmbStatus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel8.setText("Estado:");

        jLabel9.setText("Tipo");

        cmbLimit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbLimit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel7.setText("Limitar resultados a:");

        jLabel4.setText("Fecha de actividad:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(dateChooserInitReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateChooserEndReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCatalogType, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbLimit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(dateChooserInitActivityDate, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateChooserEndActivityDate, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooserInitReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooserEndReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbCatalogType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dateChooserInitActivityDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooserEndActivityDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        lblInfo.setText("jLabel4");

        jLabel3.setText("Fechas de entrega:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(dateChooserInitDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateChooserEndDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateChooserInitDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dateChooserEndDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfo))
        );

        javax.swing.GroupLayout panelTableLayout = new javax.swing.GroupLayout(panelTable);
        panelTable.setLayout(panelTableLayout);
        panelTableLayout.setHorizontalGroup(
            panelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelTableLayout.setVerticalGroup(
            panelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFolioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFolioActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        
        search(Boolean.TRUE);
        
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescriptionKeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            search(Boolean.TRUE);
        }
    }//GEN-LAST:event_txtDescriptionKeyPressed

    private void txtCustomerNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerNameKeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            search(Boolean.TRUE);
        }
    }//GEN-LAST:event_txtCustomerNameKeyPressed

    private void txtFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFolioKeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            search(Boolean.TRUE);
        }
    }//GEN-LAST:event_txtFolioKeyPressed

    private void showEventForm (String eventId) {        
        Utility.openInternalForm(new EditEvent(Long.parseLong(eventId)));        
    }
    

    
    private List<String> getEventIdsFromTable () throws BusinessException{
        
        List<String> ids = 
                Utility.getColumnsIdByBooleanSelected(
                        tableConsultEvents, 
                        TableConsultEvents.Column.BOOLEAN.getNumber(), 
                        TableConsultEvents.Column.ID.getNumber()
                );
        
        if (ids.isEmpty()) {
            throw new BusinessException(SELECT_A_ROW_NECCESSARY);
        }
        
        return ids;
    }
    
    private void btnGoToEventFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoToEventFormActionPerformed
       
        try {
            showEventForm(Utility.getEventIdFromTableOnlyOneRowSelected(
                    tableConsultEvents,
                    TableConsultEvents.Column.BOOLEAN.getNumber(),
                    TableConsultEvents.Column.ID.getNumber()));
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(
                   this, e.getMessage(),
                   ApplicationConstants.MISSING_DATA,
                   JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btnGoToEventFormActionPerformed

    private void cleanForm () {
        
        txtDescription.setText(EMPTY_STRING_TXT_FIELD);
        txtCustomerName.setText(EMPTY_STRING_TXT_FIELD);
        dateChooserInitDeliveryDate.setDate(null);
        dateChooserEndDeliveryDate.setDate(null);
        dateChooserInitReturnDate.setDate(null);
        dateChooserEndReturnDate.setDate(null);
        txtFolio.setText(EMPTY_STRING_TXT_FIELD);
        txtDescription.requestFocus();
        
    }
    private void btnGeneratePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneratePDFActionPerformed
        generateEventPDF();
    }//GEN-LAST:event_btnGeneratePDFActionPerformed

    private void btnCleanFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanFormActionPerformed
        cleanForm();
    }//GEN-LAST:event_btnCleanFormActionPerformed

    private void btnGeneratePDFDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneratePDFDetailActionPerformed
        generateEventsFoliosPDF();
    }//GEN-LAST:event_btnGeneratePDFDetailActionPerformed

    private void btnGenerateTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateTicketActionPerformed
        generateTicket();
    }//GEN-LAST:event_btnGenerateTicketActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCleanForm;
    private javax.swing.JButton btnGeneratePDF;
    private javax.swing.JButton btnGeneratePDFDetail;
    private javax.swing.JButton btnGenerateTicket;
    private javax.swing.JButton btnGoToEventForm;
    private javax.swing.JButton btnSearch;
    protected javax.swing.JComboBox<CatalogTypeEvent> cmbCatalogType;
    private javax.swing.JComboBox<String> cmbLimit;
    private javax.swing.JComboBox<CatalogStatusEvent> cmbStatus;
    private com.toedter.calendar.JDateChooser dateChooserEndActivityDate;
    private com.toedter.calendar.JDateChooser dateChooserEndDeliveryDate;
    private com.toedter.calendar.JDateChooser dateChooserEndReturnDate;
    private com.toedter.calendar.JDateChooser dateChooserInitActivityDate;
    private com.toedter.calendar.JDateChooser dateChooserInitDeliveryDate;
    private com.toedter.calendar.JDateChooser dateChooserInitReturnDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JPanel panelTable;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtFolio;
    // End of variables declaration//GEN-END:variables
}
