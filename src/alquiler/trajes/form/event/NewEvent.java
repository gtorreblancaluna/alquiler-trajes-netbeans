package alquiler.trajes.form.event;

import alquiler.trajes.constant.ApplicationConstants;
import static alquiler.trajes.constant.ApplicationConstants.EMPTY_STRING_TXT_FIELD;
import alquiler.trajes.entity.Event;
import static alquiler.trajes.form.event.EventForm.INDEX_CUSTOMER_PANE;
import static alquiler.trajes.form.event.EventForm.INDEX_EVENT_PANE;
import alquiler.trajes.form.login.LoginForm;
import alquiler.trajes.util.Utility;
import java.util.ArrayList;
import java.util.Date;


public class NewEvent extends EventForm {
    
    public NewEvent() {
        super();
        this.setTitle(ApplicationConstants.TITLE_NEW_EVENT_FORM);
        init();
    }

    @Override
    public final void init() {
        lblInfoUser.setText("Atiende:");
        lblEventCreatedAt.setText(EMPTY_STRING_TXT_FIELD);
        lblSubTotal.setText(EMPTY_STRING_TXT_FIELD);
        lblPayments.setText(EMPTY_STRING_TXT_FIELD);
        lblTotal.setText(EMPTY_STRING_TXT_FIELD);
        btnEdit.setVisible(false);
        event = new Event();
        tabPaneMain.setEnabledAt(INDEX_EVENT_PANE, false);
        tabPaneMain.setSelectedIndex(INDEX_CUSTOMER_PANE);
        btnCloneEvent.setEnabled(false);
        Utility.setTimeout(() -> this.txtName.requestFocus(), 500);
        lblUser.setText(LoginForm.userSession.getName() + " " + LoginForm.userSession.getLastName());
        paymentsEvent = new ArrayList<>();
        this.dateChooserDeliveryDate.setDate(new Date());
    }
    
}
