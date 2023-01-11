package com.sw_two;

import com.sw_two.database.DbCom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

/**
This is the page that is used to add a Customer
@author Ohzecodes
@version 1.0 
 */
public class CustomerAddCtrl implements Initializable {


    public Button SaveBtn;
    public Button CancelBtn;
    public TextField POSTAL;
    public Text errors;

    @FXML
    TextField CUSTOMER_ID;
    @FXML
    TextField CUSTOMER_NAME;
    @FXML
    TextField CUSTOMER_ADDRESS;
    @FXML
    TextField PHONE;
    @FXML
    ComboBox<Division> STATE;
    @FXML
    ComboBox<Country> COUNTRY;

    /**
     * * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (DbCom.c.size()!=0) {
            CUSTOMER_ID.setText(DbCom.c.get(DbCom.c.size() - 1).getCUSTOMER_ID() + 1 + "");
        }else  CUSTOMER_ID.setText(String.valueOf(1));

        CUSTOMER_ID.setDisable(true);
        CUSTOMER_ID.setStyle(util.getStyle(util.StyleEnum.Disabletextbox));
        CancelBtn.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
        COUNTRY.setItems(FXCollections.observableArrayList(DbCom.Countries) );
        STATE.setVisibleRowCount(5);
        COUNTRY.setVisibleRowCount(5);
        COUNTRY.getSelectionModel().selectedItemProperty().addListener((observableValue, old, newly) -> {
       STATE.setItems(newly.getDivisions());
        });
        SaveBtn.setOnAction(this::save);

    }
    /**
     *validated fields and Saves the customer
     * @param e an ActionEvent
     */
    public void save(ActionEvent e) {

        if(validate()==0){
            String CID=CUSTOMER_ID.getText();
            String CNAME=CUSTOMER_NAME.getText();
            String CADDRESS=CUSTOMER_ADDRESS.getText();
            String CPHONE=PHONE.getText();
            Division CSTATE=STATE.getValue();
            Country CCOUNTRY=COUNTRY.getValue();
            String CPOSTAL=POSTAL.getText();
            Customer cnew=  new Customer(Integer.parseInt(CID),CNAME,CADDRESS,CPHONE,CSTATE,CCOUNTRY,
                    CPOSTAL,ZonedDateTime.now(),ZonedDateTime.now(), DbCom.U.getName(), DbCom.U.getName());
            int rs=DbCom.addCustomers(cnew);
            if(rs==1){
                DbCom.c.add(cnew);

            ((Node) (e.getSource())).getScene().getWindow().hide();
            }
        }
    }
    /**
     * validates the user input
     * @return the number of errors
     */
    public int validate() {
        ResourceBundle rr = ResourceBundle.getBundle("Internationalization.lang", DbCom.l);
        ObservableList<String> e = FXCollections.observableArrayList();
        if (CUSTOMER_ID.getText().trim().equals("")) {
            e.add(rr.getString("CUSTOMER_ID"));
            CUSTOMER_ID.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
        } else {
            CUSTOMER_ID.setStyle(util.getStyle(util.StyleEnum.Disabletextbox));
        }

        if (CUSTOMER_NAME.getText().trim().equals("")) {
            e.add(rr.getString("CUSTOMER_NAME"));
            CUSTOMER_NAME.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
        } else {
            CUSTOMER_NAME.setStyle(null);
        }

        if (CUSTOMER_ADDRESS.getText().trim().equals("")) {
            e.add(rr.getString("CUSTOMER_ADDRESS"));
            CUSTOMER_ADDRESS.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
        } else {
            CUSTOMER_ADDRESS.setStyle(null);
        }

        if (POSTAL.getText().trim().equals("")) {
            e.add(rr.getString("POSTAL"));
            POSTAL.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
        } else {
            POSTAL.setStyle(null);
        }
        if (PHONE.getText().trim().equals("")) {
            e.add(rr.getString("PHONE"));
            PHONE.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
        } else {
            PHONE.setStyle(null);
        }
        if (STATE.getValue() == null) {
            e.add(rr.getString("STATE"));
            STATE.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
        } else {
            STATE.setStyle(null);
        }
        if (COUNTRY.getValue() == null) {
            e.add(rr.getString("COUNTRY"));
            COUNTRY.setStyle(util.getStyle(util.StyleEnum.invalidvaluetf));
        } else {
            COUNTRY.setStyle(null);
        }

        if(e.size()!=0) {
            errors.setText(rr.getString("YOUREMISSING") + ":\t");
            e.forEach(err -> errors.setText(errors.getText() + err + ", "));
        }
        return e.size();
    }

}
