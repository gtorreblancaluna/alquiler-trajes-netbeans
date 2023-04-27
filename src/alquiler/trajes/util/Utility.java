package alquiler.trajes.util;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.constant.RoleEnum;
import alquiler.trajes.entity.Role;
import alquiler.trajes.exceptions.UnAuthorizedException;
import alquiler.trajes.form.login.LoginForm;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;



public abstract class Utility {
    
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
        public static String deleteCharacters(String s_cadena, String s_caracteres) {
        String nueva_cadena = "";
        Character caracter = null;
        boolean valido = true;

        /* Va recorriendo la cadena s_cadena y copia a la cadena que va a regresar,
         sólo los caracteres que no estén en la cadena s_caracteres */
        for (int i = 0; i < s_cadena.length(); i++) {
            valido = true;
            for (int j = 0; j < s_caracteres.length(); j++) {
                caracter = s_caracteres.charAt(j);

                if (s_cadena.charAt(i) == caracter) {
                    valido = false;
                    break;
                }
            }
            if (valido) {
                nueva_cadena += s_cadena.charAt(i);
            }
        }

        return nueva_cadena;
    }
    
    public boolean validateHour(String hora) {
        boolean b;
        char[] a = hora.toString().toCharArray();
        String[] c = hora.split(":");
        if ((a[0] == ' ') || (a[1] == ' ') || (a[2] == ' ') || (a[3] == ' ') || (a[4] == ' ') || (Integer.parseInt(c[0]) > 24) || (Integer.parseInt(c[1]) > 59)) {
            b=false;
        }else{
            b=true;
        }
        return b;
    }
    
    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
    
    public static String removeAccents(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

    }
    
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
