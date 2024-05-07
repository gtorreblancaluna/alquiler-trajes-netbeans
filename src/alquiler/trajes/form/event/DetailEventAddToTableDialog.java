package alquiler.trajes.form.event;

import static alquiler.trajes.constant.ApplicationConstants.ENTER_KEY;
import static alquiler.trajes.constant.ApplicationConstants.MESSAGE_TITLE_ERROR;
import static alquiler.trajes.constant.ApplicationConstants.TITLE_DETAIL_EVENT_DIALOG_FORM;
import alquiler.trajes.entity.DetailEvent;
import alquiler.trajes.exceptions.InvalidDataException;
import alquiler.trajes.util.Utility;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


public class DetailEventAddToTableDialog extends javax.swing.JDialog {
    
    private DetailEvent detailToReturn = new DetailEvent();

    public DetailEventAddToTableDialog(java.awt.Frame parent, boolean modal, DetailEvent detail) {
        super(parent, modal);
        initComponents();
        //this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setTitle(TITLE_DETAIL_EVENT_DIALOG_FORM);
        if (detail != null) {
            fillDetailToInputs(detail);
            detailToReturn.setId(detail.getId());
        }
        txtName.requestFocus();
        addEscapeListener();
        addEventListener();
    }
    
    private void addEventListener () {
    
        this.addWindowListener(new WindowListener() {

                @Override
                public void windowActivated(WindowEvent arg0) {
                    // Do nothing
                }
                @Override
                public void windowClosed(WindowEvent arg0) {
                    System.out.println("windowClosed");
                    detailToReturn = null;
                    setVisible(false);
                    dispose();
                }
                @Override
                public void windowClosing(WindowEvent arg0) {
                    System.out.println("windowClosing");
                    detailToReturn = null;
                    setVisible(false);
                    dispose();
                }
                @Override
                public void windowDeactivated(WindowEvent arg0) {
                    // Do nothing
                }
                @Override
                public void windowDeiconified(WindowEvent arg0) {
                    // Do nothing
                }
                @Override
                public void windowIconified(WindowEvent arg0) {
                    // Do nothing
                }
                @Override
                public void windowOpened(WindowEvent arg0) {
                    // Do nothing
                }

            });
    }
    
    // close dialog when esc is pressed.
    private void addEscapeListener() {
        ActionListener escListener = (ActionEvent e) -> {
            detailToReturn = null;
            setVisible(false);
            dispose();
        };

        this.getRootPane().registerKeyboardAction(escListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

    }
    
    private void fillDetailToInputs (DetailEvent detail) {
        txtName.setText(detail.getNameOfAggregate());
        txtConcepts.setText(detail.getItems());
        txtAdjuts.setText(detail.getAdjustments());
        txtImport.setText(String.valueOf(detail.getUnitPrice()));
        txtPayment.setText(String.valueOf(detail.getAdvancePayment()));
    }
    
    private void validateInputs () throws InvalidDataException {
        
        if (txtName.getText().isEmpty()) {
            throw new InvalidDataException("Nombre es requerido.");
        } else if (txtName.getText().length() > 100000) {
            throw new InvalidDataException("Limite excedido.");
        }
        if (txtImport.getText().isEmpty()) {
            throw new InvalidDataException("Importe es requerido.");
        } else if (txtImport.getText().length() > 100000) {
            throw new InvalidDataException("Limite excedido.");
        }
    }
    
    private void buildDetailFromInputs () throws InvalidDataException{
        
        validateInputs();
        
        detailToReturn = 
                DetailEvent.builder()
                        .nameOfAggregate(txtName.getText().trim())
                        .items(txtConcepts.getText().trim())
                        .adjustments(txtAdjuts.getText().trim())
                        .unitPrice(Float.parseFloat(Utility.deleteCharacters(txtImport.getText(),",")))
                        .advancePayment(
                                !txtPayment.getText().isEmpty() ?
                                Float.parseFloat(Utility.deleteCharacters(txtPayment.getText(),",")) :
                                        0F)
                        .build();
    }
    
    DetailEvent showDialog() {
        setVisible(true);
        return detailToReturn;
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
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtImport = new javax.swing.JFormattedTextField();
        txtPayment = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtConcepts = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAdjuts = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Nombre:");

        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNameKeyPressed(evt);
            }
        });

        jLabel2.setText("Conceptos:");

        jLabel3.setText("Ajustes:");

        jLabel4.setText("Importe:");

        jLabel5.setText("Anticipo:");

        btnOK.setText("Aceptar");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtImport.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtImport.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtImportKeyPressed(evt);
            }
        });

        txtPayment.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPayment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPaymentKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(txtConcepts);

        jScrollPane2.setViewportView(txtAdjuts);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtImport, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPayment))
                    .addComponent(txtName)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(216, 216, 216)
                                .addComponent(btnOK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCancel))
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(166, 166, 166)
                                .addComponent(jLabel5))
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel))
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
    
    private void sendData () {
        try {
            buildDetailFromInputs();
            setVisible(false);
            dispose();
        } catch (InvalidDataException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), MESSAGE_TITLE_ERROR, JOptionPane.ERROR_MESSAGE);  
        }
    }
    
    
    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        sendData();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        detailToReturn = null; 
        setVisible(false);
         dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            sendData();
        }
    }//GEN-LAST:event_txtNameKeyPressed

    private void txtImportKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImportKeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            sendData();
        }
    }//GEN-LAST:event_txtImportKeyPressed

    private void txtPaymentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaymentKeyPressed
        if (evt.getKeyCode() == ENTER_KEY) {
            sendData();
        }
    }//GEN-LAST:event_txtPaymentKeyPressed

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
            java.util.logging.Logger.getLogger(DetailEventAddToTableDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetailEventAddToTableDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetailEventAddToTableDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetailEventAddToTableDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DetailEventAddToTableDialog dialog = new DetailEventAddToTableDialog(new javax.swing.JFrame(), true, new DetailEvent());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane txtAdjuts;
    private javax.swing.JTextPane txtConcepts;
    private javax.swing.JFormattedTextField txtImport;
    private javax.swing.JTextField txtName;
    private javax.swing.JFormattedTextField txtPayment;
    // End of variables declaration//GEN-END:variables
}
