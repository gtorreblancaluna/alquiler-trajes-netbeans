package alquiler.trajes.form.event;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.DATE_FORMAT_FOR_SQL_QUERY;
import static alquiler.trajes.constant.ApplicationConstants.DATE_LARGE;
import static alquiler.trajes.constant.ApplicationConstants.DECIMAL_FORMAT;
import static alquiler.trajes.constant.ApplicationConstants.END_DAY_HOUR_MINUTES;
import static alquiler.trajes.constant.ApplicationConstants.ENTER_KEY;
import static alquiler.trajes.constant.ApplicationConstants.MESSAGE_NUMBER_FORMAT_ERROR;
import static alquiler.trajes.constant.ApplicationConstants.SELECT_A_ROW_TO_GENERATE_REPORT;
import static alquiler.trajes.constant.ApplicationConstants.START_DAY_HOUR_MINUTES;
import static alquiler.trajes.constant.ApplicationConstants.TITLE_CONSULT_EVENTS_FORM;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.InvalidDataException;
import alquiler.trajes.form.MainForm;
import alquiler.trajes.model.params.EventParameter;
import alquiler.trajes.model.params.result.EventResult;
import alquiler.trajes.service.EventResultService;
import alquiler.trajes.table.TableConsultEvents;
import alquiler.trajes.util.Utility;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.time.FastDateFormat;


public class ConsultEventsForm extends javax.swing.JInternalFrame {

    private final TableConsultEvents tableConsultEvents;
    private final EventResultService eventResultService;
    private final FastDateFormat fastDateFormatLarge = FastDateFormat.getInstance(DATE_LARGE);
    private static final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
    private final FastDateFormat fastDateFormatSqlQuery = FastDateFormat.getInstance(DATE_FORMAT_FOR_SQL_QUERY);
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ConsultEventsForm.class.getName());
    
    public ConsultEventsForm() {
        initComponents();
        this.setTitle(TITLE_CONSULT_EVENTS_FORM);
        this.setClosable(Boolean.TRUE);
        eventResultService = EventResultService.getInstance();
        tableConsultEvents = new TableConsultEvents();
        Utility.addJtableToPane(719, 451, this.panelTable, tableConsultEvents);
        dateChooserInitDeliveryDate.setDate(new Date());
        search(false);
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
                        .initDeliveryDate(
                                this.dateChooserInitDeliveryDate.getDate() != null ? 
                                        fastDateFormatSqlQuery.format(
                                                this.dateChooserInitDeliveryDate.getDate()) + START_DAY_HOUR_MINUTES :
                                        null
                                        )
                        .endDeliveryDate(
                                this.dateChooserEndDeliveryDate.getDate() != null ? 
                                        fastDateFormatSqlQuery.format(
                                                this.dateChooserEndDeliveryDate.getDate()) + END_DAY_HOUR_MINUTES :
                                        null
                        ).initReturnDate(
                                this.dateChooserInitReturnDate.getDate() != null ? 
                                        fastDateFormatSqlQuery.format(
                                                this.dateChooserInitReturnDate.getDate()) + START_DAY_HOUR_MINUTES :
                                        null
                        ).endReturnDate(
                                this.dateChooserEndReturnDate.getDate() != null ? 
                                        fastDateFormatSqlQuery.format(
                                                this.dateChooserEndReturnDate.getDate()) + END_DAY_HOUR_MINUTES :
                                        null
                        ).customerName(txtCustomerName.getText().trim().toLowerCase())
                        .eventId(!txtFolio.getText().isEmpty() ? Long.parseLong(txtFolio.getText().trim()) : null)
                        .description(txtDescription.getText())
                        .build();
    }
        
    private void search(boolean check){
        try {
            List<EventResult> results = eventResultService.getResult(getEventParameterFromInputs());
            tableConsultEvents.format();
            DefaultTableModel temp = (DefaultTableModel) tableConsultEvents.getModel();
            for (EventResult result : results) {
                Object row[] = {
                    check,
                    result.getId(),
                    result.getDescription(),
                    result.getCustomer(),
                    fastDateFormatLarge.format(result.getDeliveryDate()) + " "+ result.getDeliveryHour(),
                    fastDateFormatLarge.format(result.getReturnDate())+ " " + result.getReturnHour(),
                    decimalFormat.format(result.getPayments()),
                    decimalFormat.format(result.getSubTotal()),
                    decimalFormat.format(result.getSubTotal() - result.getPayments())
                };
                temp.addRow(row);
            }
        } catch (BusinessException e) {
           log.error(e);
           JOptionPane.showMessageDialog(
                   this, e.getMessage(),
                   ApplicationConstants.MESSAGE_UNEXPECTED_ERROR,
                   JOptionPane.ERROR_MESSAGE);
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
        jLabel3 = new javax.swing.JLabel();
        dateChooserInitDeliveryDate = new com.toedter.calendar.JDateChooser();
        dateChooserEndDeliveryDate = new com.toedter.calendar.JDateChooser();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        dateChooserInitReturnDate = new com.toedter.calendar.JDateChooser();
        dateChooserEndReturnDate = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnCleanForm = new javax.swing.JButton();
        btnGoToEventForm = new javax.swing.JButton();
        btnGeneratePDF = new javax.swing.JButton();
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

        jLabel3.setText("Entre fechas de entrega:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(dateChooserInitDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateChooserEndDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooserInitDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooserEndDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel5.setText("Entre fechas de devolución:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(dateChooserInitReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateChooserEndReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooserInitReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooserEndReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel6.setText("Folio:");

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

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/search-32.png"))); // NOI18N
        btnSearch.setToolTipText("Buscar");
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnCleanForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/limpiar-32.png"))); // NOI18N
        btnCleanForm.setToolTipText("Buscar");
        btnCleanForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnGoToEventForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/fiesta-de-cumpleanos-32.png"))); // NOI18N
        btnGoToEventForm.setToolTipText("Buscar");
        btnGoToEventForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGoToEventForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoToEventFormActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtFolio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCleanForm, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGoToEventForm, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGeneratePDF, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(btnCleanForm)
                    .addComponent(btnGoToEventForm)
                    .addComponent(btnGeneratePDF, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(45, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(26, 26, 26))))
        );

        javax.swing.GroupLayout panelTableLayout = new javax.swing.GroupLayout(panelTable);
        panelTable.setLayout(panelTableLayout);
        panelTableLayout.setHorizontalGroup(
            panelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelTableLayout.setVerticalGroup(
            panelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
        EventForm eventForm = new EventForm(Long.parseLong(eventId));
        eventForm.setLocation(this.getWidth() / 2 - eventForm.getWidth() / 2, this.getHeight() / 2 - eventForm.getHeight() / 2 - 20);
        MainForm.jDesktopPane1.add(eventForm);
        eventForm.show();
    }
    
    private String getEventIdFromTableOnlyOneRowSelected () throws BusinessException{
        
        List<String> ids = 
                Utility.getColumnsIdByBooleanSelected(
                        tableConsultEvents, 
                        TableConsultEvents.Column.BOOLEAN.getNumber(), 
                        TableConsultEvents.Column.ID.getNumber()
                );
        
        if (ids.isEmpty() || (!ids.isEmpty() && ids.size() > 1)) {
            throw new BusinessException(SELECT_A_ROW_TO_GENERATE_REPORT);
        }
        
        return ids.get(0);
    }
    
    private void btnGoToEventFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoToEventFormActionPerformed
       
        try {
            showEventForm(getEventIdFromTableOnlyOneRowSelected());
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(
                   this, e.getMessage(),
                   ApplicationConstants.MISSING_DATA,
                   JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btnGoToEventFormActionPerformed

    private void btnGeneratePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneratePDFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGeneratePDFActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCleanForm;
    private javax.swing.JButton btnGeneratePDF;
    private javax.swing.JButton btnGoToEventForm;
    private javax.swing.JButton btnSearch;
    private com.toedter.calendar.JDateChooser dateChooserEndDeliveryDate;
    private com.toedter.calendar.JDateChooser dateChooserEndReturnDate;
    private com.toedter.calendar.JDateChooser dateChooserInitDeliveryDate;
    private com.toedter.calendar.JDateChooser dateChooserInitReturnDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel panelTable;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtFolio;
    // End of variables declaration//GEN-END:variables
}