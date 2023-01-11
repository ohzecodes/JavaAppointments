package com.sw_two;

import com.sw_two.database.DbCom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
This is a page that a user can use to find out which customer had How many appointments?
@author Ohzecodes
@version 1.0 
 */
public class ReportByCustomerCtrl implements Initializable {



    public TableView<CountObj> reportTbl;
    public TableColumn<Object, Object> type;
    public TableColumn<Object, Object> countTC;

    public TableColumn<Object, Object> customer;

    ObservableList<Appointment> App ;

    /**
     * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App = DbCom.getAllAppointmentFromMemory();
        ObservableList<CountObj> count= FXCollections.observableArrayList();
            Find(count);
        reportTbl.setItems(count);
        customer.setCellValueFactory(new PropertyValueFactory<>("type"));
        countTC.setCellValueFactory(new PropertyValueFactory<>("count"));


    }
    /** A look up function that returns the corresponding name of the given ID
    * @param id the given id 
    */
    private String getCustomerNAmeById(int id){
       return DbCom.c.filtered( c1->c1.getCUSTOMER_ID()==id ).get(0).getCUSTOMER_NAME();
    }
    /**
    * Finds and stores unique customer Name and the number of appointments they have.
     * uses a lambda function to generate the number of each appointment made by each distinct and stores it to count variable
    * @param count An ObservableList of CountObj objects where the unique customer IDs and appointment counts will be stored.
    */

    private void Find(ObservableList<CountObj> count){
/**
 * uses a lambda function to generate the number of each appointment made by each distinct and stores it to count variable
 */
        App.stream().map(Appointment::getCustomerid).distinct().toList().forEach(f->{
            int OneCount = CountFn(f);
            if(OneCount!=0) count.add(new CountObj(OneCount,getCustomerNAmeById(f)));
        });

    }
    /**
    Returns the number of appointments for a given customer.
    @param CustomerId The unique identifier for the customer.
    @return The number of appointments for the specified customer.
    */

    private int CountFn(int CustomerId){
        return  App.filtered(a-> Objects.equals(a.getCustomerid(), CustomerId)).size();

    }





}
