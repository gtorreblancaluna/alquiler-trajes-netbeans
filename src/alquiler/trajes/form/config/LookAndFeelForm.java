
package alquiler.trajes.form.config;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.constant.PropertyConstant;
import alquiler.trajes.util.PropertySystemUtil;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.SkinInfo;


public class LookAndFeelForm extends javax.swing.JInternalFrame {

    private List<JButton> buttonsInPanel = new ArrayList<>();
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LookAndFeelForm.class.getName());
    
    public LookAndFeelForm() {
        initComponents();
        addSkinsToPanel();
        this.setClosable(true);
    }
    
    private void addSkinsToPanel(){

            String themeSelected;
            try {
                themeSelected = PropertySystemUtil.get(PropertyConstant.SYSTEM_THEME);
            
            } catch (IOException iOException) {
                themeSelected = null;
                // nothing to do.
            }
            panelInnerLookAndFeel.removeAll();
            panelInnerLookAndFeel.setLayout(new GridLayout(20, 200));
            panelInnerLookAndFeel.setMaximumSize(new Dimension(400, 400));
            Map<String, SkinInfo> skins = SubstanceLookAndFeel.getAllSkins();
            log.info("getting skins ("+skins.size()+")");
             for (SkinInfo skinInfo : skins.values()){
                System.out.println("adding button "+skinInfo.getDisplayName());
                JButton button = new JButton(skinInfo.getDisplayName());
                button.setSize(20, 15);
                button.setFont(new java.awt.Font("Arial", 1, 11));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                if (themeSelected != null && 
                        themeSelected.toLowerCase().trim().equals(skinInfo.getClassName().toLowerCase().trim())) {
                    button.setSelected(true);
                }

                button.addActionListener((java.awt.event.ActionEvent e) -> {
                    try {
                        PropertySystemUtil.save(PropertyConstant.SYSTEM_THEME.getKey(), skinInfo.getClassName());
                        SubstanceLookAndFeel.setSkin(skinInfo.getClassName());
                        SwingUtilities.updateComponentTreeUI(this);
                        buttonsInPanel.stream().forEach(btn -> btn.setSelected(false));
                        JButton source = (JButton) e.getSource();
                        source.setSelected(true);
                    } catch (IOException ex) {
                        log.error(ex);
                        JOptionPane.showMessageDialog(null, ex, ApplicationConstants.MESSAGE_UNEXPECTED_ERROR,
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
               buttonsInPanel.add(button);
               panelInnerLookAndFeel.add(button);
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

        panelInnerLookAndFeel = new javax.swing.JPanel();

        javax.swing.GroupLayout panelInnerLookAndFeelLayout = new javax.swing.GroupLayout(panelInnerLookAndFeel);
        panelInnerLookAndFeel.setLayout(panelInnerLookAndFeelLayout);
        panelInnerLookAndFeelLayout.setHorizontalGroup(
            panelInnerLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 703, Short.MAX_VALUE)
        );
        panelInnerLookAndFeelLayout.setVerticalGroup(
            panelInnerLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 377, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelInnerLookAndFeel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelInnerLookAndFeel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelInnerLookAndFeel;
    // End of variables declaration//GEN-END:variables
}
