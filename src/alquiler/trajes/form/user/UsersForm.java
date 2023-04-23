package alquiler.trajes.form.user;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.constant.RoleEnum;
import alquiler.trajes.entity.Role;
import alquiler.trajes.entity.User;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.InvalidDataException;
import alquiler.trajes.exceptions.NoDataFoundException;
import alquiler.trajes.service.UserService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import lombok.Getter;

public class UsersForm extends javax.swing.JInternalFrame {

    private static final org.apache.log4j.Logger log = 
            org.apache.log4j.Logger.getLogger(UsersForm.class.getName());
    
    private final UserService userService;
    private static String idUserToEdit;
    
    public UsersForm() {
        initComponents();
        setVisibleOffPasswordTxtFields();
        this.setClosable(true);
        this.setTitle(ApplicationConstants.TITLE_USERS_FORM);
        userService = UserService.getInstance();
        fillTable();
        initButtons();
    }
    
    private void cleanInputs () {
        this.txtName.setText("");
        this.txtLastName.setText("");
        this.txtPhoneNumber.setText("");
        this.txtPasswd.setText("");
        this.txtRepeatPasswd.setText("");
    }
    
    private void initButtons () {
        this.btnSave.setEnabled(false);
    }
    
    private void setVisibleOnPasswordTxtFields () {
        jLabel5.setVisible(true);
        jLabel6.setVisible(true);
        this.txtPasswd.setVisible(true);
        this.txtRepeatPasswd.setVisible(true);
    }
    
    private void setVisibleOffPasswordTxtFields () {
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);
        this.txtPasswd.setVisible(false);
        this.txtRepeatPasswd.setVisible(false);
    }
    
    private void fillTable () {
        formatTable();
        try {
            List<User> users = userService.getAll();
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            
            for (User user : users) {
                Object fila[] = {
                    user.getId(),
                    user.getName()+ " " + user.getLastName(),
                    user.getPhoneNumber(),
                    user.getCreatedAt()
                };
                tableModel.addRow(fila);
            }
        } catch (NoDataFoundException e) {
            lblInfo.setText(ApplicationConstants.NO_FOUND_LIST_REGISTER);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e, ApplicationConstants.MESSAGE_UNEXPECTED_ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void formatTable () {
        
        // customize column types
        DefaultTableModel tableModel = new DefaultTableModel(Column.getColumnNames(), 0){
            @Override
            public Class getColumnClass(int column) {
                return Column.values()[column].getClazzType();
            }

            @Override
            public boolean isCellEditable (int row, int column) {
                return Column.values()[column].getIsEditable();
            }

        };
        
        table.setModel(tableModel);

        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(tableModel); 
        table.setRowSorter(ordenarTabla);

        DefaultTableCellRenderer centrar = new DefaultTableCellRenderer();
        centrar.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);



        for (Column column : Column.values()) {
            table.getColumnModel()
                    .getColumn(column.getNumber())
                    .setPreferredWidth(column.getSize());
        }


        table.getColumnModel().getColumn(Column.ID.getNumber()).setMaxWidth(0);
        table.getColumnModel().getColumn(Column.ID.getNumber()).setMinWidth(0);
        table.getColumnModel().getColumn(Column.ID.getNumber()).setPreferredWidth(0);
        
        try {
            DefaultTableModel temp = (DefaultTableModel) table.getModel();
            temp.removeRow(temp.getRowCount() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            ;
        }
        
    }
    
    @Getter
    private enum Column {
        
        ID(0,20,"id",String.class, false),
        NAME(1,80,"Nombre",String.class, false),
        PHONE_NUMBER(2,40,"Teléfono",String.class, false),
        CREATION_DATE(3,40,"Creado",String.class, false);
        
        private final int number;
        private final int size;
        private final String name;
        private final Class clazzType;
        private final Boolean isEditable;

        private Column(int number, int size, String name, Class clazzType, Boolean isEditable) {
            this.number = number;
            this.size = size;
            this.name = name;
            this.clazzType = clazzType;
            this.isEditable = isEditable;
        }
        
        public static String[] getColumnNames () {
            List<String> columnNames = new ArrayList<>();
            for (Column column : Column.values()) {
                columnNames.add(column.getName());
            }
            return columnNames.toArray(new String[0]);
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
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPhoneNumber = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPasswd = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        txtRepeatPasswd = new javax.swing.JPasswordField();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnRoles = new javax.swing.JButton();
        btnChangePasswd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        lblInfo = new javax.swing.JLabel();

        jLabel1.setText("Nombre:");

        jLabel2.setText("Apellidos:");

        jLabel4.setText("Teléfono:");

        jLabel5.setText("Contraseña:");

        jLabel6.setText("Repite la contraseña:");

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/add-32.png"))); // NOI18N
        btnAdd.setToolTipText("Agregar");
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

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

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/delete-32.png"))); // NOI18N
        btnDelete.setToolTipText("Eliminar");
        btnDelete.setContentAreaFilled(false);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnRoles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/project-manager-32.png"))); // NOI18N
        btnRoles.setToolTipText("Roles");
        btnRoles.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnChangePasswd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/alquiler/trajes/img/img32/password-32.png"))); // NOI18N
        btnChangePasswd.setToolTipText("Cambiar contraseña");
        btnChangePasswd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPasswd, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(103, 103, 103)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtRepeatPasswd, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRoles, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChangePasswd, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPasswd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRepeatPasswd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdd)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRoles)
                    .addComponent(btnChangePasswd))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        lblInfo.setToolTipText("");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
       cleanInputs();
       this.txtName.requestFocus();
       this.btnSave.setEnabled(true);
       this.setVisibleOnPasswordTxtFields();
       idUserToEdit = null;
    }//GEN-LAST:event_btnAddActionPerformed
    private void validatePasswd () throws InvalidDataException{
        if (txtPasswd.getPassword().length <= 0 || txtRepeatPasswd.getPassword().length <= 0) {
                throw new InvalidDataException("Contraseña es requerida.");
            }
        if (txtPasswd.getPassword().length > 0 && txtRepeatPasswd.getPassword().length > 0 ) {
            String pass = String.valueOf(txtPasswd.getPassword());
            String repeatPass = String.valueOf(txtRepeatPasswd.getPassword());
            
            if (pass.length() < 4 || repeatPass.length() < 4) {
                throw new InvalidDataException("Introduce una contraseña mayor a 4 caracteres.");
            } else if (!pass.equals(repeatPass)) {
                throw new InvalidDataException("Constraseña no coincide.");
            }
        }
    }
    private User getUserFromInputs () {
        return User.builder()
                .id(idUserToEdit != null ? Long.parseLong(idUserToEdit) : null)
                .name(txtName.getText())
                .lastName(txtLastName.getText())
                .phoneNumber(txtPhoneNumber.getText())
                .password(String.valueOf(txtPasswd.getPassword()))
                .build();
    }
    
    private void saveOrUpdate () {
        try {
            if (idUserToEdit == null) {
                validatePasswd();
            }
            
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(RoleEnum.DEFAULT.getId()));
            final User user = getUserFromInputs();
            user.setRoles(roles);
            userService.saveOrUpdate(user);
            fillTable();
            initButtons();
            cleanInputs();
            lblInfo.setText(ApplicationConstants.MESSAGE_SAVE_SUCCESSFUL);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    ApplicationConstants.MESSAGE_MISSING_PARAMETERS, JOptionPane.ERROR_MESSAGE);
        } finally {
            idUserToEdit = null;
        }
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        saveOrUpdate ();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void editForm() {
        String id = String.valueOf(table.getValueAt(table.getSelectedRow(), Column.ID.number));
        this.txtName.requestFocus();
        this.btnSave.setEnabled(true);
        try {
            User user = userService.findById(Long.parseLong(id));
            this.txtName.setText(user.getName());
            this.txtLastName.setText(user.getLastName());
            this.txtPhoneNumber.setText(user.getPhoneNumber());
            idUserToEdit = String.valueOf(user.getId());
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    ApplicationConstants.MESSAGE_UNEXPECTED_ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        if (evt.getClickCount() == 2) {
            editForm();
        }
    }//GEN-LAST:event_tableMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (this.table.getSelectedRow() == - 1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para continuar ", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        editForm();
    }//GEN-LAST:event_btnEditActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnChangePasswd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnRoles;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPasswd;
    private javax.swing.JTextField txtPhoneNumber;
    private javax.swing.JPasswordField txtRepeatPasswd;
    // End of variables declaration//GEN-END:variables
}
