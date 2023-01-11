package com.sw_two;

import com.sw_two.database.DbCom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
/**
this is a page that is used to edit Appointments 
@author Ohzecodes
@version 1.0  */
public class AppointmentModCtrl implements Initializable {
    public Text errors;

    public ComboBox<Integer> customerIdCombo;
    public ComboBox<Integer> userIdCombo;
    @FXML public Button CancelBtn,SaveBtn;
    @FXML public TextField AppointmentId;
    @FXML  public  TextField AppointmentTitle;
    @FXML public TextArea AppointmentDescription;
    @FXML public TextField AppointmentLocation;
    public DatePicker StartDate;
    public DatePicker EndDate;
    public ComboBox<Integer> Starthour;
    public ComboBox<Integer> Startmin;
    public ComboBox<Integer> endHour;
    public ComboBox<Integer> endMin;
    public TextField TypeTextBox;
    public ComboBox<String> contactCombo;

    /**
     * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCombo.setItems(FXCollections.observableArrayList(DbCom.contacts.stream().map(e->e.getContactName()).collect(Collectors.toList())));
        errors.setText("");
        AppointmentId.setDisable(true);
        AppointmentId.setStyle(util.getStyle(util.StyleEnum.Disabletextbox));
        ObservableList<Integer> Hours= FXCollections.observableArrayList();
        ObservableList<Integer> Minutes= FXCollections.observableArrayList();
        Startmin.setVisibleRowCount(5);
        endMin.setVisibleRowCount(5);
        Starthour.setVisibleRowCount(5);
        endHour.setVisibleRowCount(5);
       ObservableList<Integer> cust=FXCollections.observableArrayList();

       DbCom.c.forEach(customer -> cust.add(customer.getCUSTOMER_ID()));
        customerIdCombo.setItems(cust);
        ObservableList<Integer> AllUsers=FXCollections.observableArrayList();
        DbCom.Uar.forEach(e->AllUsers.add(e.getUid()));
        userIdCombo.setItems(AllUsers);

        for (int i = 0; i <= 23; i++) {
            Hours.add(i);
        }
        for (int i = 0; i <= 59; i+=5) {
            Minutes.add(i);
        }
        Startmin.setItems(Minutes);
        Starthour.setItems(Hours);
        endMin.setItems(Minutes);
        endHour.setItems(Hours);
        SaveBtn.setOnAction(e->save(e));
        CancelBtn.setOnAction(e->    ((Node) (e.getSource())).getScene().getWindow().hide());
    }
    /**
     *validated fields and Saves the appointment
     * @param e an ActionEvent
     */
    public void save(ActionEvent e){
        if (validate()==0){
        int appointmentid = Integer.parseInt(AppointmentId.getText().trim());
        int customerid = customerIdCombo.getValue();
        int userid = userIdCombo.getValue();
        String title =AppointmentTitle.getText();
        String description = AppointmentDescription.getText();
        String location = AppointmentLocation.getText();

        ZonedDateTime localStartDateTime = ZonedDateTime.of(StartDate.getValue(),LocalTime.of( Starthour.getValue(), Startmin.getValue(),00) , ZoneId.of(util.getmyTZ()));
        ZonedDateTime  localEndDateTime = ZonedDateTime.of(StartDate.getValue(),LocalTime.of( endHour.getValue(), endMin.getValue(),00) , ZoneId.of(util.getmyTZ()));

        String ContactName= contactCombo.getValue();
       Contact CONTACT=     DbCom.contacts.filtered(f-> Objects.equals(f.getContactName(), ContactName)).get(0);
            String type = TypeTextBox.getText();
             Appointment a=  new Appointment(appointmentid,  title,  description,  location,type,
                 localStartDateTime,  localEndDateTime,customerid,  userid,CONTACT);
        int rs =DbCom.updateAppointment(a);

        if (rs==1){
            Appointment Old = DbCom.U.getAllAppointments().filtered(app->app.getAppointmentid()==appointmentid).get(0);
            if(userid!= DbCom.U.getUid()){
                DbCom.U.getAllAppointments().remove(Old);
            }else{
                int index= DbCom.U.getAllAppointments().indexOf(Old);
                DbCom.U.getAllAppointments().set(index,a);
            }
            ((Node)(e.getSource())).getScene().getWindow().hide();
        }
    }}

    /**
     * sets the fields using the given appointment
     * @param a  the given appointment
     */
    public void set(Appointment a) {
        contactCombo.setValue(a.getCONTACTNAME());
        TypeTextBox.setText(a.getTYPE());
        userIdCombo.setValue(a.getUserid());
        customerIdCombo.setValue(a.getCustomerid());
        AppointmentId.setText(a.getAppointmentid()+" ");
        AppointmentTitle.setText(a.getTitle()+" ");
        AppointmentDescription.setText(a.getDescription()+" ");
        AppointmentLocation.setText(a.getLocation()+" ");
        StartDate.setValue(a.getStartDate());
        EndDate.setValue(a.getEndDate());
        Starthour.setValue(a.getStartTime().getHour());
        Startmin.setValue(a.getStartTime().getMinute());
        endHour.setValue(a.getEndTime().getHour());
        endMin.setValue(a.getEndTime().getMinute());

    }
    /**
     * validates the user input
     * @return the number of errors
     */
    private  int validate(){
        ResourceBundle rr=ResourceBundle.getBundle("Internationalization.lang", DbCom.l);
        ObservableList<String> b=FXCollections.observableArrayList();

        if(customerIdCombo.getValue() == null){
            customerIdCombo.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
            b.add(rr.getString("CUSTOMERID"));
        }else{
            customerIdCombo.setStyle(null);
        }
        if(AppointmentTitle.getText().trim().equals("")){
            AppointmentTitle.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
            b.add(rr.getString("TITLE"));
        }else{
            AppointmentTitle.setStyle(null);
        }
        if(userIdCombo.getValue()==null){
            userIdCombo.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));

            b.add(rr.getString("USERID"));
        } else{
            userIdCombo.setStyle(null);
        }
        if(AppointmentDescription.getText().trim().equals("")){
            AppointmentDescription.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
            b.add(rr.getString("DESCRIPTION"));
        } else{
            AppointmentDescription.setStyle(null);
        }
        if(AppointmentLocation.getText().trim().equals("")){
            AppointmentLocation.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
            b.add(rr.getString( "LOCATION"));
        } else{
            AppointmentLocation.setStyle(null);
        }
        if(TypeTextBox.getText().trim().equals("")){
            TypeTextBox.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
            b.add(rr.getString( "TYPE"));
        } else{
            TypeTextBox.setStyle(null);
        }
        if(contactCombo.getValue() == null){
            contactCombo.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
            b.add(rr.getString("CONTACTNAME"));
        }else{
            contactCombo.setStyle(null);
        }
        if (StartDate.getValue()==null){
            StartDate.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));

            b.add(rr.getString("STARTDATE"));
        } else {
            StartDate.setStyle(null);
        }
        if(EndDate.getValue() == null){
            EndDate.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
            b.add(rr.getString("ENDDATE") );
        } else{
            EndDate.setStyle(null);

        }
        if (StartDate.getValue()!=null&&EndDate.getValue()!=null) {
            ZonedDateTime start = ZonedDateTime.of(StartDate.getValue(), LocalTime.of(Starthour.getValue(), (Startmin.getValue()), 0), ZoneId.of(util.getmyTZ())),
                    end = ZonedDateTime.of(EndDate.getValue(), LocalTime.of(endHour.getValue(), endMin.getValue(), 0), ZoneId.of(util.getmyTZ()));

            if (end.isBefore(start))
                b.add(rr.getString("ENDCANTBEBEFORESTART"));

            if (util.OverlapingintheArray(start, end, DbCom.U.getAllAppointments(), Integer.parseInt(AppointmentId.getText().trim())))
                b.add(rr.getString("OVERLAPPINGDATE"));
            if (!(util.isinofficeHours(start))) {
                b.add(rr.getString("STARTNOTINOFFICE")); }
            if (!(util.isinofficeHours(end))) {
                b.add(rr.getString("ENDNOTINOFFICE")); }
        }
        errors.setText(rr.getString("YOUREMISSING")+":\t");

        b.forEach(e-> errors.setText(errors.getText()+""+e+","));
        return  b.size();
    }

}
