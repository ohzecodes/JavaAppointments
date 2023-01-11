package com.sw_two;

import com.sw_two.database.DbCom;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;


/**
    This is a page that a user sees after signing in.
    It also features a language changing functionality English or french.
    @author Ohzecodes
    @version 1.0
 */
public class LogedinCtrl implements Initializable {

    @FXML
    public Button logoutbtn;
    @FXML public Text uname;
    @FXML public Text uid;
    @FXML public Text timezone;
    @FXML public TableColumn<Object, Object> AppointmentId;
    @FXML public TableColumn<Object, Object> Title;
    @FXML public TableColumn<Object, Object> Description;
    @FXML public TableColumn<Object, Object> Location;

    @FXML public TableColumn<Object, Object> StarTime;
    @FXML public TableColumn<Object, Object> EndTime;
    @FXML public TableColumn<Object, Object> StartDate;
    @FXML  public TableColumn<Object, Object> EndDate;
    @FXML  public TableColumn<Object, Object> CostumerId;

    @FXML public TableColumn<Object, Object> UserId;
    @FXML public TableView<Appointment> AppointmentTable;
   @FXML public TableColumn<Object, Object> TYPE;
    public TableColumn<Object, Object> CONTACTNAME;
    public Button AddAppointment,ModifyAppointment,DeleteAppointment;

    //customer Table cols
    public TableView<Customer> CustomerTable;
    public TableColumn<Object, Object> CUSTOMER_ID;
    public TableColumn<Object, Object> CUSTOMER_NAME;
    public TableColumn<Object, Object> CUSTOMER_ADDRESS;
    public TableColumn<Object, Object> PHONE;
    public TableColumn<Object, Object> State;
    public TableColumn<Object, Object> POSTAL;
    public TableColumn<Object, Object> CREATED_ON;
    public TableColumn<Object, Object> LAST_UPDATE_ON;
    public TableColumn<Object, Object> CREATED_BY;
    public TableColumn<Object, Object> LAST_UPDATED_BY;
    public TableColumn<Object, Object> COUNTRY;
    //   buttons
    public Button AddCustomer,ModifyCustomer,DeleteCustomer;
    public ToggleGroup FilterBy;
    public Button ReportByType,ReportsByCustomer;
    public Button AppointmentReport;

    /**
     * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timezone.setText(util.getmyTZ());
        logoutbtn.setOnAction(e->logout(e));
        setuserinfo(DbCom.U);
        DeleteAppointment.setOnAction(e->DeleteAppointment(e));
        ModifyAppointment.setOnAction(e->changeScreenToModifyAppointment(e));
        setCustomerstable(DbCom.getCustomers());
        DeleteCustomer.setOnAction(e->DeleteCustomer());
        ModifyCustomer.setOnAction(e->Modify_customer(e));
        AddAppointment.setOnAction(e->AddAppointment(e));
        AddCustomer.setOnAction(e->AddCustomer(e));
        ReportsByCustomer.setOnAction(this::ReportsByCustomer);
        ReportByType.setOnAction(this::ReportsByType);
        AppointmentReport.setOnAction(this::AppointmentReportHandle);
        FilterBy.getToggles().forEach(e -> {
            RadioButton e1 = (RadioButton) e;
            e1.setOnAction(this::FILTER);
        } );
    }

    /**
     * A Funtions that runs when you toggle the monthly, weekly, all radio buttons. It sets the table columns accordingly 
     * @param e Action event
     */

    private void  FILTER(ActionEvent e) {

        RadioButton e1=(RadioButton)e.getSource();
        System.out.println(e1.getText());
        if(e1.getText().equalsIgnoreCase("ALL")){
            AppointmentTable.setItems(DbCom.U.getAllAppointments());
        }
        else if(e1.getText().equalsIgnoreCase("Month")) {
            System.out.println(ZonedDateTime.now().getMonth());
            AppointmentTable.setItems(DbCom.U.getAllAppointments()
                    .filtered(f -> f.getLocalStartDateTime().getMonth().equals(ZonedDateTime.now().getMonth()))
            );
        }
        else if(e1.getText().equalsIgnoreCase("Week"))
            AppointmentTable.setItems(
                    DbCom.U.getAllAppointments()
                            .filtered(f->util.isinthisweek(f.getLocalStartDateTime()))
            );

    }

    /**
    * opens the reports by type page
    * @param e Action event
     */
    private void ReportsByType(ActionEvent e) {

        ChangeScreen("Report_By_Type.fxml");
        //        try {
        //            ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCom.l);
        //
        //            FXMLLoader Loader = new FXMLLoader(getClass().getResource("Report_By_Type.fxml"),rr);
        //            Parent root = Loader.load();
        //            Stage stage = new Stage();
        //            stage.setTitle("Reports");
        //            stage.setScene(new Scene(root));
        //            stage.show();
        //
        //        }catch (Exception ex) {
        //            ex.printStackTrace();
        //        }

    }
     /**
    * opens the reports by customer page
    * @param e Action event
     */
    private void ReportsByCustomer(ActionEvent e) {
        ChangeScreen("Report_By_Customer.fxml");


    }
     /**
    * opens the  Report Of Appointment By Contact page
    * @param e Action event
     */
    private void AppointmentReportHandle(ActionEvent e) {
        this.ChangeScreen("ReportOfAppointmentByContact.fxml");
    }
    /**
    *opens the add apointment page
    * @param e Action event
     */
    private void  AddAppointment(ActionEvent e) {

        try {
            ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCom.l);
            if (DbCom.c.size()<1) {
               new Alert(Alert.AlertType.WARNING,rr.getString("YOUCANTADDANAPPOINTMENTWITHNOCUSTOMER")).showAndWait();
                return;
            }
            ChangeScreen("Add_Appointment.fxml");

        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    /**
    * opens the the modify Appoinment page 
     * @param e Action event
     */
    public void changeScreenToModifyAppointment(ActionEvent e)  {
        try {
            ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCom.l);
            Appointment a =AppointmentTable.getSelectionModel().getSelectedItem();
            if (a!=null) {
                FXMLLoader Loader = new FXMLLoader(getClass().getResource("ModAppointment.fxml"),rr);
                Parent root = Loader.load();
                AppointmentModCtrl mod = Loader.getController();
                mod.set(a);
                Stage stage = new Stage();
                stage.setTitle(rr.getString("MODIFYAPPOINTMENT") +":"+ a.getTitle());
                stage.setScene(new Scene(root));
                stage.show();
            }else{
                 new Alert(Alert.AlertType.ERROR, rr.getString("NOELEMENTSELECTED")).showAndWait();
            }
            }catch (Exception ex) {
                ex.printStackTrace();
            }

    }
    /**
    * Deletes the selected appointment in the table after showing a Confirmation message
     * @param e Action event
    */
    private void DeleteAppointment(ActionEvent e) {
        Appointment SelectedAppointment=  AppointmentTable.getSelectionModel().getSelectedItem();
        ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCom.l);

        if(SelectedAppointment!=null) {
            Alert A=  new Alert(Alert.AlertType.CONFIRMATION,rr.getString("APPOINTMENTCONFIRMATIONDELETE")+
                    "\n Id: " +SelectedAppointment.getAppointmentid()+
                    "\n Of Type: "+SelectedAppointment.getTYPE()+
//                    "\n Title:"+SelectedAppointment.getTitle()
                    "\n AND WILL BE CANCELLED"
            );
            Optional< ButtonType> res=A.showAndWait();
            if (res.isPresent()&&res.get()==ButtonType.OK) {
                int rs = DbCom.DeleteAppointment(e, SelectedAppointment.getAppointmentid());
                DbCom.U.getAllAppointments().remove(SelectedAppointment);
                if (rs < 1)
                    new Alert(Alert.AlertType.ERROR, rr.getString("NODATAEXISTS")).showAndWait();
            }
            }else{
            new Alert(Alert.AlertType.ERROR,rr.getString("NOELEMENTSELECTED") ).showAndWait();

        }

    }
    /**
        * logouts the user, switches to the login screen
        * expects the user to close all windows before logout
        * @param e Action event
    */
    private void logout(ActionEvent e) {
//        Window currentStage =  ((Node) e.getSource()).getScene().getWindow();

        DbCom.logout(e);

    }

    /**
        * Search the table columns of the appointment table by using the observable list
        * @param a The observable list of appointments
    */
    public  void setAppointmentcols(ObservableList<Appointment> a ) {

        if(a!=null) {
            AppointmentTable.setItems(a);
            CONTACTNAME.setCellValueFactory(new PropertyValueFactory<>("CONTACTNAME"));
            TYPE.setCellValueFactory(new PropertyValueFactory<>("TYPE"));
            AppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentid"));
            Title.setCellValueFactory(new PropertyValueFactory<>("title"));
            Description.setCellValueFactory(new PropertyValueFactory<>("description"));
            Location.setCellValueFactory(new PropertyValueFactory<>("location"));

            CostumerId.setCellValueFactory(new PropertyValueFactory<>("Customerid"));
            UserId.setCellValueFactory(new PropertyValueFactory<>("Userid"));

            StartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
            EndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));

            StarTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
            EndTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));

        }
    }
    /**
    * Sets the user info on the page
    * @param U Signed in user
     */
    public void  setuserinfo(User U) {
        uname.setText(U.getName());
        uid.setText(String.valueOf(U.getUid()));
        if(U.getAllAppointments()!=null) {
            setAppointmentcols(U.getAllAppointments().filtered(e -> e.getUserid() == U.getUid()));
        }

    }
    /**
    * opens  the add customer page
     * @param e Action event
     */
    private void AddCustomer(ActionEvent e) {
        ChangeScreen("Add_Customer.fxml");
        //        try {
        //            ResourceBundle rr=ResourceBundle.getBundle("Internationalization.lang", DbCom.l);
        //            FXMLLoader Loader = new FXMLLoader(getClass().getResource("Add_Customer.fxml"),rr);
        //            Parent root = Loader.load();
        //            Stage stage = new Stage();
        //            stage.setTitle(rr.getString("ADDCUSTOMER"));
        //            stage.setScene(new Scene(root));
        //            stage.show();
        //
        //        }catch (Exception ex) {
        //            ex.printStackTrace();
        //        }
    }
   
    
    /**
    * Opens the modify customer page
    * @param e Action event
    */
   
    public void Modify_customer(ActionEvent e) {
          ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCom.l);
          try {
              Customer c =CustomerTable.getSelectionModel().getSelectedItem();
              if (c!=null) {
                  FXMLLoader Loader = new FXMLLoader(getClass().getResource("ModCustomer.fxml"),rr);
                  Parent root = Loader.load();
                  CustomerModCtrl mod = Loader.getController();
                  mod.set(c);
                  Stage stage = new Stage();
                  stage.setTitle(rr.getString("MODIFYCUSTOMER") +" : "+ c.getCUSTOMER_NAME());
                  stage.setScene(new Scene(root));
                  stage.show();
              }else{
                  new Alert(Alert.AlertType.ERROR,rr.getString("NOELEMENTSELECTED")).showAndWait();
              }
          }catch (Exception ex) {
             ex.printStackTrace();
          }
    }

    /**
    * search the customer columns  using the given list
    * @param c  A list of customers that will be used to set the columns of the customer table
    */

    public void setCustomerstable(ObservableList<Customer> c) {
        CustomerTable.setItems(c);
        CUSTOMER_ID.setCellValueFactory(new PropertyValueFactory<>("CUSTOMER_ID"));
        CUSTOMER_NAME.setCellValueFactory(new PropertyValueFactory<>("CUSTOMER_NAME"));
        CUSTOMER_ADDRESS.setCellValueFactory(new PropertyValueFactory<>("CUSTOMER_ADDRESS"));
        POSTAL.setCellValueFactory(new PropertyValueFactory<>("POSTAL"));
        PHONE.setCellValueFactory(new PropertyValueFactory<>("PHONE"));
        State.setCellValueFactory(new PropertyValueFactory<>("STATE"));
        CREATED_ON.setCellValueFactory(new PropertyValueFactory<>("CREATED_ON"));
        LAST_UPDATE_ON.setCellValueFactory(new PropertyValueFactory<>("LAST_UPDATE_ON"));
        COUNTRY.setCellValueFactory(new PropertyValueFactory<>("Country"));
        CREATED_BY.setCellValueFactory(new PropertyValueFactory<>("CREATED_BY"));
        LAST_UPDATED_BY.setCellValueFactory(new PropertyValueFactory<>("LAST_UPDATED_BY"));
    }
    /**
    * delete the customer after showing an confirmation message
    *
    */
    public void DeleteCustomer(){
        ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCom.l);
       Customer Selected= CustomerTable.getSelectionModel().getSelectedItem();
       if(Selected!=null){
           Alert A=  new Alert(Alert.AlertType.CONFIRMATION,rr.getString("CUSTOMERCONFIRMATIONDELETE") +
                   "\n"+Selected.getCUSTOMER_NAME());
           Optional< ButtonType> res=A.showAndWait();
           if (res.isPresent()&&res.get()==ButtonType.OK) {
               int SelectedId = Selected.getCUSTOMER_ID();
               int rs =DbCom.DeleteCustomer(SelectedId);
               if(rs==1) {
                   DbCom.c.remove(Selected);
                   setAppointmentcols(DbCom.U.getAllAppointments());
               } }
       }else{
           new Alert(Alert.AlertType.ERROR, rr.getString("NOELEMENTSELECTED")).showAndWait();
       }
    }
    /**
    * Change the screen to the given screenName
    * @param ScreenName the namee of the screen Including the extension for example something.fxml
     */
    private  void ChangeScreen(String ScreenName){
        try {
            ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang", DbCom.l);
            FXMLLoader Loader = new FXMLLoader(getClass().getResource(ScreenName),rr);
            Parent root = Loader.load();
            Stage stage = new Stage();
            stage.setTitle(ScreenName.replace(".fxml","").replace("_"," "));
            stage.setScene(new Scene(root));
            stage.show();

        }catch (Exception ex) {
            ex.printStackTrace();
        }


    }

}