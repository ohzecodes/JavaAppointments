package com.sw_two;

import com.sw_two.database.DbCom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
    This page shows a schedule of a contact
    @author Ohzecodes
    @version 1.0 
 */
public class ReportOfAppointmentByContact implements Initializable {


    @FXML
    public TableColumn<Object, Object> AppointmentId;
    @FXML public TableColumn<Object, Object> Title;
    @FXML public TableColumn<Object, Object> Description;
    @FXML public TableColumn<Object, Object> Location;

    @FXML public TableColumn<Object, Object> StarTime;
    @FXML public TableColumn<Object, Object> EndTime;
    @FXML public TableColumn<Object, Object> StartDate;
    @FXML  public TableColumn<Object, Object> EndDate;
    @FXML  public TableColumn<Object, Object> CostumerId;
    public TableColumn<Object, Object> UserId;
    public TableColumn<Object, Object> CONTACTNAME;
    public TableView<Appointment> AppointmentTable;
    public TableColumn<Object, Object> TYPE;
    public ToggleGroup FilterBy;
    public ComboBox<String> UserCombo;

    private ObservableList<Appointment>a;
    /**
     * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            UserCombo.setItems( FXCollections.observableArrayList(DbCom.contacts.stream().map(Contact::getContactName).toList()));
            UserCombo.setValue("");
            UserCombo.setVisibleRowCount(5);
           ObservableList<Appointment> App =DbCom.getAllAppointmentFromMemory();
            UserCombo.getSelectionModel().selectedItemProperty().addListener((observableValue, old, newly) -> {
                setAppointmentcols(App.filtered(e-> Objects.equals(e.getCONTACTNAME(), newly)));
            });
            FilterBy.getToggles().forEach(e -> {
                RadioButton e1 = (RadioButton) e;
                e1.setOnAction(this::FILTER);
            });

    }

       /**
     * A Funtions that runs when you toggle the monthly, weekly, all radio buttons. It sets the table columns accordingly 
     * @param e Action event
     */
    private void  FILTER(ActionEvent e){
        RadioButton e1=(RadioButton)e.getSource();
        if(e1.getText().equalsIgnoreCase("ALL")){
            AppointmentTable.setItems(a);
        }
        else if(e1.getText().equalsIgnoreCase("Month"))
            AppointmentTable.setItems(a.filtered(f->f.getLocalStartDateTime().getMonth().equals(ZonedDateTime.now().getMonth()))
            );
        else if(e1.getText().equalsIgnoreCase("Week"))
            AppointmentTable.setItems(
                    a.filtered(f->util.isinthisweek(f.getLocalStartDateTime()))
            );
    }
    /**
    * set the appointment columns of for the appointment table Using the given a array list
    * @param a An array list holding the appointments that you want to display
     */
    public void setAppointmentcols(ObservableList<Appointment> a){

        if(a!=null) {
            AppointmentTable.setItems(a);
            AppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentid"));
            Title.setCellValueFactory(new PropertyValueFactory<>("title"));
            TYPE.setCellValueFactory(new PropertyValueFactory<>("TYPE"));
            Description.setCellValueFactory(new PropertyValueFactory<>("description"));
            CostumerId.setCellValueFactory(new PropertyValueFactory<>("Customerid"));

            StartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
            StarTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));

            EndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
            EndTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));

        }
    }
}
