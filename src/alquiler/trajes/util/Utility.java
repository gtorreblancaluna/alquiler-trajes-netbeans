package alquiler.trajes.util;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.MESSAGE_TITLE_DETELE_RECORD_CONFIRM;
import alquiler.trajes.constant.RoleEnum;
import alquiler.trajes.entity.Role;
import alquiler.trajes.exceptions.UnAuthorizedException;
import alquiler.trajes.form.login.LoginForm;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;



public abstract class Utility {
    private static final int POSITION_HOUR = 0;
    private static final int POSITION_MINUTE = 1;
    
    public static Date setHourAndMinutesFromDate (String[] hourAndMinute, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourAndMinute[POSITION_HOUR]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hourAndMinute[POSITION_MINUTE]));
        Date returnDate = calendar.getTime();
        return returnDate;
    }
    
    public static int showConfirmDialogYesNo () {
        // return 0 when yes button pusehd.
        return JOptionPane.showOptionDialog(null,
                String.format(ApplicationConstants.DETELE_RECORD_CONFIRM, 
                            ""),
                            MESSAGE_TITLE_DETELE_RECORD_CONFIRM,
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "Si"
                );
        
    }    

    
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
    
    public static boolean validateHour(String hora) {
        boolean b;
        char[] a = hora.toCharArray();
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
