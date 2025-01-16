package alquiler.trajes.form;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.LOCALE_COUNTRY;
import static alquiler.trajes.constant.ApplicationConstants.LOCALE_LANGUAGE;
import alquiler.trajes.constant.PropertyConstant;
import alquiler.trajes.form.config.LookAndFeelForm;
import alquiler.trajes.form.customer.CustomersForm;
import alquiler.trajes.form.event.ConsultEventsForm;
import alquiler.trajes.form.event.EditEvent;
import alquiler.trajes.form.event.NewEvent;
import alquiler.trajes.form.event.NewEventFromDateChooser;
import alquiler.trajes.form.login.LoginForm;
import alquiler.trajes.form.user.UsersForm;
import alquiler.trajes.form.utility.UtilityForm;
import alquiler.trajes.model.params.EventParameter;
import alquiler.trajes.model.params.result.EventResult;
import alquiler.trajes.service.EventResultService;
import alquiler.trajes.tables.TableConsultEvents;
import alquiler.trajes.util.PropertySystemUtil;
import alquiler.trajes.util.Utility;
import alquiler.trajes.util.UtilityDate;
import alquiler.trajes.util.UtilityTable;
import com.toedter.calendar.JCalendar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.io.IOException;
import java.util.Locale;

public class MainForm extends javax.swing.JFrame {
    
    private final TableConsultEvents tableConsultEvents;
    private final EventResultService eventResultService;
    private final UtilityDate utilityDate;
    private static final Logger logger = Logger.getLogger(LoginForm.class.getName());
    private final Locale locale;
    

    public MainForm() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.lblUserSession.setText(LoginForm.userSession.getName() + " " + LoginForm.userSession.getLastName());
        this.setExtendedState(MAXIMIZED_BOTH);
        tableConsultEvents = new TableConsultEvents();
        eventResultService = EventResultService.getInstance();
        utilityDate = UtilityDate.getInstance();
        Utility.addJtableToPane(400, 200, this.panelProxEventos, tableConsultEvents);
        addEventListenerToTable();
        fillTable();
        locale = new Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY);
        
        dateChooser.setLocale(locale);
        dateChooser.getDayChooser().addPropertyChangeListener(
        //property sliderListener detects change of date in date chooser
        (PropertyChangeEvent evt)-> { dateChooserPropertChanged(evt);});
        

        
    }
    
    private void dateChooserPropertChanged(PropertyChangeEvent evt) {
        System.out.println("date is :"+ dateChooser.getDayChooser());
        Utility.openInternalForm(new NewEventFromDateChooser(dateChooser.getDate())); 
        
    }
    
    private void fillTable(){
        try{
            Long plusDays = 
                    Long.valueOf(PropertySystemUtil.get(PropertyConstant.PLUS_DAYS_TO_EVENTS_IN_MAIN_FORM));
            
            jLabel11.setText("Próximos eventos. (Mostrando los siguientes "+Integer.valueOf(plusDays.toString())+" días)");
            
            LocalDate localDate = LocalDate.now().plusDays(plusDays);
            EventParameter eventParameter = new EventParameter();
            eventParameter.setInitDeliveryDate(
                    utilityDate.getStartDateFormatSqlQuery(new Date())
            );
            eventParameter.setEndDeliveryDate(
                    utilityDate.getEndDateFormatSqlQuery(utilityDate.asDate(localDate))
            );
            eventParameter.setLimit("1000");
            List<EventResult> results = eventResultService.getResult(eventParameter);
            UtilityTable.fillTableEvents(tableConsultEvents,results);
        }catch(IOException e){
            logger.log(Level.SEVERE,e.getMessage(),e);           
            JOptionPane.showMessageDialog(this, e, ApplicationConstants.MESSAGE_UNEXPECTED_ERROR,
                    JOptionPane.ERROR_MESSAGE);
        } 
      
    }
    
    private void showEventForm (String eventId) {        
        Utility.openInternalForm(new EditEvent(Long.parseLong(eventId)));        
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel15 = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel14 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpanelEvents = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jbtnAddEvent = new javax.swing.JButton();
        jbtnConsultEvents = new javax.swing.JButton();
        dateChooser = new com.toedter.calendar.JCalendar();
        panelProxEventos = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jbnUpdate = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jpanelUtilities = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbtnUsers = new javax.swing.JButton();
        jbtnCustomers = new javax.swing.JButton();
        jbtnGeneralInfo = new javax.swing.JButton();
        jbtnLookAndFeel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaLogger = new javax.swing.JTextArea();
        panelFooter = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblUserSession = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jpanelEvents.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jbtnAddEvent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/agregar-32.png"))); // NOI18N
        jbtnAddEvent.setText("Agregar evento");
        jbtnAddEvent.setToolTipText("Agregar evento");
        jbtnAddEvent.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbtnAddEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddEventActionPerformed(evt);
            }
        });

        jbtnConsultEvents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/calendario-32.png"))); // NOI18N
        jbtnConsultEvents.setText("Consultar eventos");
        jbtnConsultEvents.setToolTipText("Consultar eventos");
        jbtnConsultEvents.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbtnConsultEvents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnConsultEventsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelProxEventosLayout = new javax.swing.GroupLayout(panelProxEventos);
        panelProxEventos.setLayout(panelProxEventosLayout);
        panelProxEventosLayout.setHorizontalGroup(
            panelProxEventosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelProxEventosLayout.setVerticalGroup(
            panelProxEventosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 108, Short.MAX_VALUE)
        );

        jbnUpdate.setText("Actualizar");
        jbnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnUpdateActionPerformed(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Próximos eventos.");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 360, Short.MAX_VALUE)
                .addComponent(jbnUpdate)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbnUpdate)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelProxEventos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jbtnAddEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtnConsultEvents)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnAddEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnConsultEvents, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelProxEventos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jpanelEventsLayout = new javax.swing.GroupLayout(jpanelEvents);
        jpanelEvents.setLayout(jpanelEventsLayout);
        jpanelEventsLayout.setHorizontalGroup(
            jpanelEventsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelEventsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpanelEventsLayout.setVerticalGroup(
            jpanelEventsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelEventsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Eventos", new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/fiesta-de-cumpleanos-32.png")), jpanelEvents, "Eventos"); // NOI18N

        jbtnUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/trabajando-32.png"))); // NOI18N
        jbtnUsers.setText("Usuarios");
        jbtnUsers.setToolTipText("Usuarios");
        jbtnUsers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbtnUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnUsersActionPerformed(evt);
            }
        });

        jbtnCustomers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/clasificacion-32.png"))); // NOI18N
        jbtnCustomers.setText("Clientes");
        jbtnCustomers.setToolTipText("Clientes");
        jbtnCustomers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbtnCustomers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCustomersActionPerformed(evt);
            }
        });

        jbtnGeneralInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/settings-32.png"))); // NOI18N
        jbtnGeneralInfo.setText("Configuración");
        jbtnGeneralInfo.setToolTipText("Configuración");
        jbtnGeneralInfo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbtnGeneralInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGeneralInfoActionPerformed(evt);
            }
        });

        jbtnLookAndFeel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/cepillo-32.png"))); // NOI18N
        jbtnLookAndFeel.setText("Apariencia");
        jbtnLookAndFeel.setToolTipText("Apariencia");
        jbtnLookAndFeel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbtnLookAndFeel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnLookAndFeelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnUsers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnCustomers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnGeneralInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnLookAndFeel)
                .addContainerGap(399, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnCustomers, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnGeneralInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnLookAndFeel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(220, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpanelUtilitiesLayout = new javax.swing.GroupLayout(jpanelUtilities);
        jpanelUtilities.setLayout(jpanelUtilitiesLayout);
        jpanelUtilitiesLayout.setHorizontalGroup(
            jpanelUtilitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpanelUtilitiesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpanelUtilitiesLayout.setVerticalGroup(
            jpanelUtilitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelUtilitiesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configuración", new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/configuracion-32.png")), jpanelUtilities, "Configuracion"); // NOI18N

        txtAreaLogger.setColumns(20);
        txtAreaLogger.setRows(5);
        jScrollPane1.setViewportView(txtAreaLogger);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)))
        );

        jLabel7.setText("Contacto: gtorreblancaluna@gmail.com. Gerardo Torreblanca Luna");

        jLabel8.setText("version: 2025.01.06");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUserSession, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUserSession, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img64/traje-de-traje-y-corbata-64.png"))); // NOI18N

        javax.swing.GroupLayout panelFooterLayout = new javax.swing.GroupLayout(panelFooter);
        panelFooter.setLayout(panelFooterLayout);
        panelFooterLayout.setHorizontalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFooterLayout.setVerticalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFooterLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelFooterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDesktopPane1.setLayer(jPanel14, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(panelFooter, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDesktopPane1)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDesktopPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnLookAndFeelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLookAndFeelActionPerformed
        Utility.openInternalForm(new LookAndFeelForm()); 
    }//GEN-LAST:event_jbtnLookAndFeelActionPerformed

    private void jbtnUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnUsersActionPerformed
        Utility.openInternalForm(new UsersForm()); 
    }//GEN-LAST:event_jbtnUsersActionPerformed

    private void jbtnCustomersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCustomersActionPerformed
        Utility.openInternalForm(new CustomersForm()); 
    }//GEN-LAST:event_jbtnCustomersActionPerformed

    private void jbtnAddEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddEventActionPerformed
        Utility.openInternalForm(new NewEvent()); 
    }//GEN-LAST:event_jbtnAddEventActionPerformed

    private void jbtnConsultEventsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnConsultEventsActionPerformed
        Utility.openInternalForm(new ConsultEventsForm());   
    }//GEN-LAST:event_jbtnConsultEventsActionPerformed

    private void jbnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnUpdateActionPerformed
        if (jbnUpdate.isEnabled()) {
            jbnUpdate.setEnabled(false);
            Utility.setTimeout(() -> jbnUpdate.setEnabled(true), 5000);
            fillTable();
        }
    }//GEN-LAST:event_jbnUpdateActionPerformed

    private void jbtnGeneralInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGeneralInfoActionPerformed
        Utility.openInternalForm(new UtilityForm()); 
    }//GEN-LAST:event_jbtnGeneralInfoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JCalendar dateChooser;
    public static javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbnUpdate;
    private javax.swing.JButton jbtnAddEvent;
    private javax.swing.JButton jbtnConsultEvents;
    private javax.swing.JButton jbtnCustomers;
    private javax.swing.JButton jbtnGeneralInfo;
    private javax.swing.JButton jbtnLookAndFeel;
    private javax.swing.JButton jbtnUsers;
    private javax.swing.JPanel jpanelEvents;
    private javax.swing.JPanel jpanelUtilities;
    public static javax.swing.JLabel lblUserSession;
    private javax.swing.JPanel panelFooter;
    private javax.swing.JPanel panelProxEventos;
    private javax.swing.JTextArea txtAreaLogger;
    // End of variables declaration//GEN-END:variables
}
