package com.sw_two.database;

import com.sw_two.exceptions.InvalidCredentialException;
import com.sw_two.*;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.*;
import java.sql.*;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


import static com.sw_two.util.*;

/** 
 * This is a utility file that is supposed to communicate with the database and store essential information ie: Customers list, locale,and User.

    @author Ohzecodes
    @version 1.0  
 */
public class DbCom {
    /*
    It's a good practice to use an env file.
    but for the purposes of this project, we will just define them here like this.
    */
//    ---------------------- Db Information-------------------------
    private static final String dbuserName = dbstrings.dbuserName;
    private static final String dbpassword = dbstrings.dbpassword;
    private static final int Port=dbstrings.Port;
    private static final String Db_name=dbstrings.Db_name;
    private static final String dbconfig = dbstrings.dbconfig;
//------------------------- Db Information end here -----------------
    public static User U = new User(); // this is the signed in
    public static Locale l = Locale.getDefault();// locale for language settings
    public static ObservableList<Customer> c = FXCollections.observableArrayList(); //a list of customer
    public static ObservableList<Contact> contacts = FXCollections.observableArrayList();//a list of contacts
    public static ObservableList<User> Uar = FXCollections.observableArrayList(); //a list of all the user in the application
    public static ObservableList<Country> Countries = FXCollections.observableArrayList(); //a list of countries that a customer may belong to.

//    ------------------------Login Methods --------------------------

    /**
     * If the user exists and the password matches, it will change the screen to the login screen and log a success message.
     * If the password does not match, it will throw an exception and log a failure message
     * @param e  an ActionEvent
     * @param username the user name form the user input
     * @param Password the password from the user input
     */
    public static void loginWithUserNameAndPassword(ActionEvent e, String username, String Password) {

        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        ObservableList<String>  ARR= FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("SELECT * FROM users WHERE User_Name= ?");
            s.setString(1, username);
            r = s.executeQuery();
            if (!r.isBeforeFirst()) {
                ARR.add(ZonedDateTime.now()+ ",Status failed, username does not exist");
                throw new InvalidCredentialException("Invalid UserName Or Password");
            } else {
                while (r.next()) {
                    String retrievedPassword = r.getString("PASSWORD");
                    int UserId = r.getInt("USER_ID");
                    String UserName = r.getString("User_Name");
                    if (retrievedPassword.equals(Password)) {
                        ChangeScreenToLogin(e, UserName, UserId);
                        ARR.add(ZonedDateTime.now()+ ",Status: successful");
                    } else {
                        ARR.add(ZonedDateTime.now()+ ",Status: failed" );
                        throw new InvalidCredentialException("Invalid UserName Or Password");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (InvalidCredentialException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", l).getString("INVALIDUSERNAMEORPASSWORDERROR")
            );
            a.show();
        } finally {
            try {
                r.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                util.writingToOuputFile(ARR);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Changes Screen To Login Screen and alert if any appointment between now and 15 minutes
     * @param e an ActionEvent
     * @param name the username of the user
     * @param UserId the userId of the user
     */
    private static void ChangeScreenToLogin(ActionEvent e, String name, int UserId) {
        try {
            if (Countries.size()==0) getCountries();
            getAllUsers();

            ObservableList<Appointment> a= getAppointmentsFromDb(new ActionEvent(), UserId);
            Uar.filtered(user -> user.getUid()==UserId).get(0).setAllAppointments(a);
            U= Uar.filtered(user -> user.getUid()==UserId).get(0);
            ObservableList<Appointment> appointmentinFifteen = U.getAllAppointments().filtered(an -> in15minutes(an.getLocalStartDateTime()) && an.getUserid() == UserId);
            Parent root = new FXMLLoader().load(Objects.requireNonNull(DbCom.class.getResource("/com/sw_two/Logedin.fxml")), ResourceBundle.getBundle("Internationalization.Lang", l));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle(name + " Logged in");
            stage.setScene(new Scene(root));
            stage.show();

            ResourceBundle rr = ResourceBundle.getBundle("Internationalization.lang", l);
            String AlertString;
            if (appointmentinFifteen.size() != 0) {
                AlertString = rr.getString("YOUHAVE") + " " + appointmentinFifteen.size() + " " +
                        rr.getString("APPOINTMENTS") + ":\n";
            //The application displays an alert message when an appointment is scheduled within 15 minutes of logging in.
            // The appointment ID, date, and time are not evident in the alert.
                for (Appointment an : appointmentinFifteen) {
                    AlertString += "id: "+ an.getAppointmentid()
                            + " Title: "+ an.getTitle()
                            + " Start Date: "+ an.getLocalStartDateTime().toLocalDate()
                            + " Start Time: "+an.getLocalStartDateTime().toLocalTime()
                            + " End Date: "+ an.getLocalEndDateTime().toLocalDate()
                            + " End Time: "+ an.getLocalEndDateTime().toLocalTime()
                            + "\n----------------------------------------\n";
                }

            }else{
                AlertString=AlertString = rr.getString("YOUHAVE") + " no "+rr.getString("APPOINTMENTS") +" in the next 15 minutes";

            }
            Alert alert=  new Alert(Alert.AlertType.INFORMATION, AlertString);

            alert.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * unset the user, removes all contacts, all users from User array, and customers
     * @param e  an ActionEvent
     */
    public static void logout(ActionEvent e) {
        U.Signout();
        contacts.removeAll(contacts);
        Uar.removeAll(Uar);
        c.removeAll(c);
        System.out.println("_______________ USER SIGNED OUT_______________________");
        try {
            Parent root = new FXMLLoader().load(Objects.requireNonNull(DbCom.class.getResource("/com/sw_two/Login.fxml")), ResourceBundle.getBundle("Internationalization.Lang", l));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    /**
    * fetches All user form db
    * */
    private static ObservableList<User> getAllUsers() {
        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement(" SELECT User_ID, User_Name  FROM users ;");

            r = s.executeQuery();
            if (r.isBeforeFirst()) {

                while (r.next()) {

                    int USER_ID = r.getInt("USER_ID");
                    String NAME = r.getString("User_Name");
                   User u =new User(USER_ID, NAME);

                   u.setAllAppointments(getAppointmentsFromDb(new ActionEvent(),USER_ID));
                    Uar.add(u);
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Uar;

    }
    /*Appointment Methods */

    /**
    * fetches the last appointment id from the appointment table
    * */
    public static int GetLastAppointmentID() {
        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        int apointment_id = 0;
        ObservableList<Appointment> a = FXCollections.observableArrayList();
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement(" SELECT (Appointment_ID) FROM appointments ORDER BY Appointment_ID DESC LIMIT 1;");
            r = s.executeQuery();
            if (r.isBeforeFirst()) {
                while (r.next()) {
                    apointment_id = r.getInt("Appointment_ID");
                    return apointment_id;
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            r.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            s.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return apointment_id;
    }

    /***
     * fethhes all the Appointments of this  user from the appointment table  and return it also store it in the appointment array
     * @param e an ActionEvent
     * @param UserId the user id
     * @return a list of appointments
     */
    public static ObservableList<Appointment> getAppointmentsFromDb(ActionEvent e, int UserId) {
        if (contacts.size()==0)
        getContacts();
        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        ObservableList<Appointment> a = FXCollections.observableArrayList();
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("SELECT appointments.Appointment_ID, appointments.Title,    appointments.Description,   " +
                    " appointments.Location,    appointments.Type,    appointments.Start,    appointments.End,    appointments.Create_Date,  " +
                    "  appointments.Created_By,    appointments.Last_Update,    appointments.Last_Updated_By,    appointments.Customer_ID,   " +
                    " appointments.User_ID,    appointments.Contact_ID"+
                    ",contacts.Contact_Name ,contacts.Email " +
                    "FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID where appointments.User_ID=? ");
                s.setInt(1,UserId);
            r = s.executeQuery();
            if (r.isBeforeFirst()) {
                while (r.next()) {
                    int ContactID  = r.getInt("Contact_ID");
                    String ContactName=r.getString("Contact_Name");

                    int apointment_id = r.getInt("Appointment_ID");
                    String title = r.getString("Title");
                    String description = r.getString("Description");
                    String location = r.getString("Location");
                    int customer_id = r.getInt("Customer_ID");

                    int user_id = r.getInt("User_ID");
                    String TYPE= r.getString("Type");

                    LocalDate Startdate = r.getDate("START").toLocalDate();
                    LocalDate EndDate = r.getDate("END").toLocalDate();
                    LocalTime StartTime = r.getTime("START").toLocalTime();
                    LocalTime EndTime = r.getTime("END").toLocalTime();
                    ZonedDateTime LocalStartDateTime = UTCtolocal(UTCDateTime(Startdate, StartTime));
                    ZonedDateTime LocalEndDateTime = UTCtolocal(UTCDateTime(EndDate, EndTime));
                    String ContactEmail=r.getString("Email");
                    Appointment n = new Appointment(apointment_id, title, description, location,TYPE,
                            LocalStartDateTime, LocalEndDateTime,
                            customer_id, user_id, new Contact(ContactID,ContactName, ContactEmail));

                    a.add(n);

                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return a;

    }

    /**
     * addd appointment to the db
     * @param a an appointment that is to be added
     * @return 1 if successful adding, else 0
     */
    public static int AddAppointment(Appointment a) {
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {

            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("INSERT INTO appointments(Appointment_ID  , Title  , Description  , Location  , Type  , Start  , End  , Create_Date  , Created_By  , Last_Update  , Last_Updated_By  , Customer_ID  , User_ID  , Contact_ID)" +
                    "VALUES (? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?);");

            s.setInt(1,a.getAppointmentid());
            s.setString(2,a.getTitle());
            s.setString(3,a.getDescription());
            s.setString(4,a.getLocation());
            s.setString(5,a.getTYPE());
            s.setTimestamp(6, Timestamp.valueOf(localtoUTC((a.getLocalStartDateTime())).toLocalDateTime()));
            s.setTimestamp(7,Timestamp.valueOf(localtoUTC((a.getLocalEndDateTime())).toLocalDateTime()));
            s.setTimestamp(8,Timestamp.valueOf(localtoUTC(ZonedDateTime.now()).toLocalDateTime()));
            s.setString(9,U.getName());
            s.setTimestamp(10,Timestamp.valueOf(localtoUTC(ZonedDateTime.now()).toLocalDateTime()));
            s.setString(11,U.getName());
            s.setInt(12,a.getCustomerid());
            s.setInt(13,U.getUid());
            s.setInt(14,a.getContactId()) ;
            rs = s.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", l).getString("CANTUPDATEAPPOINTMENT")
            ).showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {

                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return rs;


    }
    /**
     * edit appointment in the db
     * @param a an appointment that is to be edited
     * @return 1 if successful update, else 0
     */
    public static int updateAppointment(Appointment a) {
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("UPDATE appointments SET " +
                    "Title = ?, Description = ?, Location = ?," +
                    " Type = ?, Start = ?, End = ?, Last_Update = ?, " +
                    "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?," +
                    " Contact_ID = ? WHERE Appointment_ID = ?");

            s.setString(1, a.getTitle());
            s.setString(2, a.getDescription());
            s.setString(3, a.getLocation());
            s.setString(4, a.getTYPE());
            s.setTimestamp(5, Timestamp.valueOf(localtoUTC((a.getLocalStartDateTime())).toLocalDateTime()));
            s.setTimestamp(6,Timestamp.valueOf(localtoUTC((a.getLocalEndDateTime())).toLocalDateTime()));
            s.setTimestamp(7,Timestamp.valueOf(localtoUTC(ZonedDateTime.now()).toLocalDateTime()));
            s.setString(8,U.getName());

            s.setInt(9, a.getCustomerid());

            s.setInt(10, a.getUserid());
            s.setInt(11, a.getContactId());
            s.setInt(12,a.getAppointmentid());
            rs = s.executeUpdate();


        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", l).getString("CANTUPDATEAPPOINTMENT")
            ).showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return rs;
    }

    /**
     * DELETE the appointment form the db
     * @param e an ActionEvent
     * @param selected the appointment id of the appoint that you want to delete
     * @return 1 if successful removal, else 0
     */
    public static int DeleteAppointment(ActionEvent e, int selected) {
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("DELETE FROM appointments  WHERE Appointment_ID = ?");
            s.setInt(1, selected);
            rs = s.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        }
    }


    /**
     * get Contacts from the db
     * @return a  list of contacts
     */
    public static ObservableList<Contact> getContacts(){
        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("SELECT * FROM contacts");

            r = s.executeQuery();
            if (r.isBeforeFirst()) {

                while (r.next()) {



//                    // TODO: update the contact to add email
                    String ContactEmail=r.getString("Email");

                    contacts.add(new Contact( r.getInt("Contact_ID"),r.getString("Contact_Name"),ContactEmail));
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return contacts;

    }

    /**
     * list of appointments from traversing all of the users and map  their appointments to different array
     * @return a  list of contacts
     */
    public static ObservableList<Appointment> getAllAppointmentFromMemory(){

     return     FXCollections.observableArrayList(Uar.filtered(u -> u.getAllAppointments() != null)
                .stream()
                .flatMap(u -> u.getAllAppointments().stream())
                .collect(Collectors.toList()));

//      return  ;
    }

    /* CUSTOMERS */

    /**
     *get Customers form the db
     * @return a  list of customers
     */
    public static ObservableList<Customer> getCustomers() {
        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("SELECT * FROM CUSTOMERS ");

            r = s.executeQuery();
            if (r.isBeforeFirst()) {

                while (r.next()) {

                    int CUSTOMER_ID = r.getInt("CUSTOMER_ID");
                    String CUSTOMER_NAME = r.getString("CUSTOMER_NAME");
                    String CUSTOMER_ADDRESS = r.getString("Address");
                    String postal=r.getString("Postal_Code");

                    String PHONE = r.getString("Phone");



                    Date CREATED_ON_DATE = r.getDate("Create_Date");
                    Time CREATED_ON_TIME = r.getTime("Create_Date");

                    ZonedDateTime CREATED_ON = UTCtolocal(UTCDateTime(CREATED_ON_DATE.toLocalDate(), CREATED_ON_TIME.toLocalTime()));

                    Date LAST_UPDATE_ON_DATE = r.getDate("Last_Update");
                    Time LAST_UPDATE_ON_TIME = r.getTime("Last_Update");

                    ZonedDateTime LAST_UPDATE_ON = UTCtolocal(UTCDateTime(LAST_UPDATE_ON_DATE.toLocalDate(), LAST_UPDATE_ON_TIME.toLocalTime()));
                    Division STATE=FindDivisionById(r.getInt("Division_ID"));
                    Country country=Countries.filtered(e->e.getId()==STATE.getCountryId()).get(0);


                    String CREATED_BY  = r.getString("CREATED_BY");
                    String LAST_UPDATED_BY  = r.getString("LAST_UPDATED_BY");
                    Customer customer = new Customer(CUSTOMER_ID, CUSTOMER_NAME, CUSTOMER_ADDRESS, PHONE, STATE,
                            country,postal, CREATED_ON, LAST_UPDATE_ON,LAST_UPDATED_BY,CREATED_BY);

                    c.add(customer);
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return c;

    }

    public static void deleteCustomerAppointmentLocal(int cid){
        for (User u1:Uar) {
            ObservableList<Appointment> a =u1.getAllAppointments().filtered(ap -> ap.getCustomerid()!=cid);
            u1.setAllAppointments(a);
            if(U.getUid()==u1.getUid()){
            U.setAllAppointments(a);
            }
        }

    }
    public static int DeleteAppointmentofCustomer(int customerid ){
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("DELETE FROM Appointments WHERE CUSTOMER_ID = ?");
            s.setInt(1, customerid);
            rs = s.executeUpdate();
            if(rs!=0){
                deleteCustomerAppointmentLocal(customerid);
            }
        } catch (SQLIntegrityConstraintViolationException e) {} catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        }
    }


    /**
     * deletes customer whose id is passed from the db
     * @param SelectedId  customer id who you want to delete
     * @return 1 if successful removal , else 0
     */

    public static int DeleteCustomer(int SelectedId) {
        DeleteAppointmentofCustomer(SelectedId);
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("DELETE FROM CUSTOMERS  WHERE CUSTOMER_ID = ?");
            s.setInt(1, SelectedId);
            rs = s.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {

            new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", l).getString("CANTDELETECUSTOMERSERROR")
            ).showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return rs;
        }
    }

    /**
     * adds a new cusotmer to the db
     * @param c the cusotmer object that you want to add
     * @return 1 if successful adding, else 0
     */
    public static int addCustomers(Customer c) {
        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {


            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("INSERT INTO customers"+
            "(Customer_ID,Customer_Name, Address,Postal_Code, Phone,Create_Date,Created_By,Last_Update, Last_Updated_By,Division_ID)"+
           " VALUES"+ "(?,?,?,?,?,?,?,?,?,?)");

            s.setInt(1,c.getCUSTOMER_ID());
            s.setString(2,c.getCUSTOMER_NAME());
            s.setString(3,c.getCUSTOMER_ADDRESS());
            s.setString(4,c.getPOSTAL());
            s.setString(5,c.getPHONE());
            s.setTimestamp(6,Timestamp.valueOf(localtoUTC(ZonedDateTime.now()).toLocalDateTime()));
            s.setString(7,U.getName());
            s.setTimestamp(8,Timestamp.valueOf(localtoUTC(ZonedDateTime.now()).toLocalDateTime()));
            s.setString(9,U.getName());
            s.setInt(10,c.getSTATE().getId());



            rs = s.executeUpdate();
            return rs;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Internationalization.lang", l).getString("CANTCREATECUSTOMER")
            ).showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            try {
                s.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return rs;
    }

    /**
     * Modifies a customer
     * @param c the new customer
     * @return 1 if successful, else 0
     */
    public static int ModifyCustomer(Customer c) {

        Connection connection = null;
        PreparedStatement s = null;
        int rs = -1;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("UPDATE customers SET  Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                    "Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?; "
            );
            s.setString(1,c.getCUSTOMER_NAME());
            s.setString(2,c.getCUSTOMER_ADDRESS());
            s.setString(3,c.getPOSTAL());
            s.setString(4,c.getPHONE());
            s.setTimestamp(5,Timestamp.valueOf(localtoUTC(ZonedDateTime.now()).toLocalDateTime()));
            s.setString(6,U.getName());
            s.setInt(7,c.getSTATE().getId());
            s.setInt(8,c.getCUSTOMER_ID());
            rs =s.executeUpdate();
            return rs;
        }catch (SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            new Alert (Alert.AlertType.ERROR,ResourceBundle.getBundle("Internationalization.lang",l).getString("CANTUPDATECUSTOMER")
            ).showAndWait();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }  finally {
            try {
                s.close();
            } catch (SQLException ex) {ex.printStackTrace();}

            try {connection.close();} catch (SQLException ex) {ex.printStackTrace();}

        }

        return rs;


    }

    /**
     * gets  division of a specific country id from the db
     * @param country_id the country id
     * @return a list of devisions
     */
    private static ObservableList<Division> getDivision(int country_id){
        Connection connection = null;
        PreparedStatement s = null;
        ObservableList<Division> Divisions=FXCollections.observableArrayList();
        ResultSet r = null;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("SELECT * FROM first_level_divisions where Country_ID=? ");
            s.setInt(1,country_id);
            r = s.executeQuery();

            if (r.isBeforeFirst()) {
                while (r.next())
                Divisions.add(new Division(r.getInt("Division_ID"),r.getString("Division"),country_id));
            }


        }catch (SQLException ex) {
            ex.printStackTrace();

        }
        return Divisions;
    }

    /**
     * gets all country id from the db
     * @return a list of countries
     */
    public static ObservableList<Country> getCountries(){
        Connection connection = null;
        PreparedStatement s = null;
        ResultSet r = null;
        try {
            connection = DriverManager.getConnection(dbconfig, dbuserName, dbpassword);
            s = connection.prepareStatement("SELECT * FROM countries");

            r = s.executeQuery();

            if (r.isBeforeFirst()) {

                while (r.next()) {
                    int Country_ID = r.getInt("Country_ID");
                    String Country = r.getString("Country");

                    Country e=new Country(Country_ID,Country);

                    Countries.add(e);
                }

            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        for (Country c:Countries ) c.setDivisions(getDivision(c.getId()));
//        System.out.println(Countries.get(2).toString());

        return null;


    }

    /**
     * traverses the country array to find the specific division
     * @param divisionId  the id of the division you are looking
     * @return the division whose id was given
     */
    private static Division FindDivisionById(int divisionId){
        return Countries.stream().flatMap(country -> country.getDivisions().stream())
             .collect(Collectors.toList())
             .stream().filter(e->e.getId()==divisionId).collect(Collectors.toList()).get(0);



    }


}

