package com.sw_two;

import com.sw_two.database.DbCom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
This is a page that user can see what type how many appointments

@author Ohzecodes
@version 1.0 
 */
public class ReportByTypeCtrl implements Initializable {
    public ToggleGroup MONTH  ;

    public TableView<CountObj> reportTbl;
    public TableColumn<Object, Object> type;
    public TableColumn<Object, Object> countTC;

    /**
     * Intialize method that gets initialized when this page launches
     *  * this code uses multiple lambda functions to set the an action on each of the Radio buttons of months
     *  * the actions are also set by using lambda functions, by unsetting the count, then getting the chosen month
     *  * and passing it to the find function
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<CountObj> count= FXCollections.observableArrayList();
        /**
         * this code uses multiple lambda functions to set the an action on each of the Radio buttons of months
         * the actions are also set by using lambda functions, by unsetting the count, then getting the chosen month
         * and passing it to the find function
         */
        MONTH.getToggles().forEach(s-> {
                 RadioButton Onerb = (RadioButton) s;
                 Onerb.setOnAction(e -> {
                     RadioButton rb = (RadioButton) e.getSource();
                     count.removeAll(count);
                             Month m = Month.valueOf(rb.getText().trim().toUpperCase());
                            Find(count,m);
                         }
                 );

            if(Onerb.getText().trim().equalsIgnoreCase(LocalDate.now().getMonth().toString())) {
                Onerb.setSelected(true);
                Find(count, ZonedDateTime.now().getMonth());
            }
             });
        reportTbl.setItems(count);
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        countTC.setCellValueFactory(new PropertyValueFactory<>("count"));

    }
    /**
    Finds the number of appointments of a specific type in a given month.
    @param count A list to store the counts of the different appointment types.
    @param m The month to search for appointments in.
    */
    private void Find(ObservableList<CountObj> count,Month m){
        ObservableList<Appointment> App = DbCom.getAllAppointmentFromMemory();
        App.stream().map(f -> f.getTYPE())
                .distinct().toList().forEach(f->{
            int OneCount = CountFn(f,m);
            if(OneCount!=0)
                count.add(new CountObj(OneCount,f));
        });

    }
    /**
    Returns the number of appointments for a given type.
    @param type The type of Appointment
    @param month  The month to search for appointments in.
    @return The number of appointments for the specified type.
    */
    private int CountFn(String type, Month month){
        ObservableList<Appointment> App =DbCom.getAllAppointmentFromMemory();
     return  App.filtered(a->a.getLocalStartDateTime().getMonth()== month)
              .filtered(a-> Objects.equals(a.getTYPE().trim(), type)).size();
    }

}
