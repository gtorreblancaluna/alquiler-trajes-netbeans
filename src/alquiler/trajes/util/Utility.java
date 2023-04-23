package alquiler.trajes.util;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.constant.RoleEnum;
import alquiler.trajes.entity.Role;
import alquiler.trajes.exceptions.UnAuthorizedException;
import alquiler.trajes.form.login.LoginForm;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;



public abstract class Utility {
    
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void onlyAdminUserAccess () throws UnAuthorizedException {
        Optional<Role> adminRole = 
                LoginForm.userSession.getRoles()
                        .stream()
                        .filter(t -> t.getId().equals(RoleEnum.ADMIN.getId()))
                        .findFirst();
        
        if (!adminRole.isPresent()) {
            throw new UnAuthorizedException(ApplicationConstants.MESSAGE_NOT_PERMISIONS_ADMIN);
        }
    }
    
    public static void addJtableToPane (int sizeVertical, int sizeHorizontal, JPanel jPanel,JTable tableToAdd ) {
        
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(tableToAdd);
        
        javax.swing.GroupLayout tabPanelGeneralLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(tabPanelGeneralLayout);
        tabPanelGeneralLayout.setHorizontalGroup(
            tabPanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPanelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, sizeVertical, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabPanelGeneralLayout.setVerticalGroup(
            tabPanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabPanelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, sizeHorizontal, Short.MAX_VALUE))
        );
    }
    
    public static String getPathLocation()throws IOException,URISyntaxException{

        File file = new File(Utility.class.getProtectionDomain().getCodeSource().getLocation()
            .toURI()).getParentFile();
        return file+"";
    
    }   
    
}
