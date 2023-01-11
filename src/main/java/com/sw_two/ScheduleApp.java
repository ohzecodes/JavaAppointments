package com.sw_two;

import com.sw_two.database.DbCom;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
/**
This class runs the main application
@author Ohzecodes
@version 1.0  
*/
public class ScheduleApp extends Application {

    /**
    This function starts the program
     If you change you your language you will need to restart the program
    @param stage
     */
    @Override
    public void start(Stage stage) throws IOException {
        if(Locale.getDefault().getLanguage()=="fr"){
            DbCom.l= Locale.CANADA_FRENCH;
        }
        ResourceBundle rr=ResourceBundle.getBundle("Internationalization.Lang", DbCom.l);
        FXMLLoader fxmlLoader = new FXMLLoader(ScheduleApp.class.getResource("Login.fxml"),rr);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(rr.getString("SCHEDULER"));
        stage.setScene(scene);
        stage.show();
    }

    /**
    The main function of the application
    @param args
    */
    public static void main(String[] args) {


        launch();
    }
}