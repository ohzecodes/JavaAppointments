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
This is the page that is used to add an appointment
@author Ohzecodes
@version 1.0 
 */
public class AppointmentAddCtrl implements Initializable {

    public Text errors;
    public ComboBox<Integer> customerIdCombo;
    public ComboBox<Integer> userIdCombo;
    @FXML
    public Button CancelBtn, SaveBtn;
    @FXML
    public TextField AppointmentId;
    @FXML
    public TextField AppointmentTitle;
    @FXML
    public TextArea AppointmentDescription;
    @FXML
    public TextField AppointmentLocation;
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
        errors.setText("");
        contactCombo.setVisibleRowCount(5);
        contactCombo.setItems(FXCollections.observableArrayList(DbCom.contacts.stream().map(e->e.getContactName()).collect(Collectors.toList())));
        AppointmentId.setDisable(true);
        AppointmentId.setStyle(util.getStyle(util.StyleEnum.Disabletextbox));
        int newAid = DbCom.GetLastAppointmentID() + 1;
        AppointmentId.setText(newAid + "");
        ObservableList<Integer> Hours = FXCollections.observableArrayList();
        ObservableList<Integer> Minutes = FXCollections.observableArrayList();
        Startmin.setVisibleRowCount(5);
        endMin.setVisibleRowCount(5);
        Starthour.setVisibleRowCount(5);
        endHour.setVisibleRowCount(5);
        ObservableList<Integer> cust = FXCollections.observableArrayList();
        DbCom.c.forEach(customer -> cust.add(customer.getCUSTOMER_ID()));
        customerIdCombo.setItems(cust);
        ObservableList<Integer> AllUsers = FXCollections.observableArrayList();
        DbCom.Uar.forEach(e -> AllUsers.add(e.getUid()));
        userIdCombo.setItems(AllUsers);

        for (int i = 0; i <= 23; i++) {
            Hours.add(i);
        }
        for (int i = 0; i <= 59; i += 5) {
            Minutes.add(i);
        }
        Startmin.setItems(Minutes);
        Starthour.setItems(Hours);
        endMin.setItems(Minutes);
        endHour.setItems(Hours);
        SaveBtn.setOnAction(e -> save(e));
        Startmin.setValue(0);
        Starthour.setValue(0);
        endMin.setValue(0);
        endHour.setValue(0);
        userIdCombo.setValue(DbCom.U.getUid());
        CancelBtn.setOnAction(e->((Node) (e.getSource())).getScene().getWindow().hide());

    }

    /**
     *validated fields and Saves the appointment
     * @param e an ActionEvent
     */

    private void save(ActionEvent e) {

        if(validate()==0) {

            int appointmentid = Integer.parseInt(AppointmentId.getText().trim());
            int customerid = customerIdCombo.getValue();
            int userid = userIdCombo.getValue();
            String title = AppointmentTitle.getText().trim();
            String description = AppointmentDescription.getText().trim();
            String location = AppointmentLocation.getText().trim();

            ZonedDateTime localStartDateTime = ZonedDateTime.of(StartDate.getValue(), LocalTime.of(Starthour.getValue(), (Startmin.getValue()), 0), ZoneId.of(util.getmyTZ()));
            ZonedDateTime localEndDateTime = ZonedDateTime.of(StartDate.getValue(), LocalTime.of(endHour.getValue(), endMin.getValue(), 0), ZoneId.of(util.getmyTZ()));


            String ContactName=  contactCombo.getValue();
            String type=TypeTextBox.getText();
            Appointment a = new Appointment(appointmentid, title, description, location,type,
                    localStartDateTime, localEndDateTime,
                    customerid, userid,DbCom.contacts.filtered(f-> Objects.equals(f.getContactName(), ContactName)).get(0));

            int rs = DbCom.AddAppointment(a);
            if (rs == 1) {
                if(userid== DbCom.U.getUid()){
                    DbCom.U.getAllAppointments().add(a);
                }

                ((Node) (e.getSource())).getScene().getWindow().hide();
            }
        }
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
        }if (StartDate.getValue()!=null&&EndDate.getValue()!=null) {
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
