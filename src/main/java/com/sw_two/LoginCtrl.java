package com.sw_two;

import com.sw_two.database.DbCom;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.sw_two.database.DbCom.l;
/**
 This is a sign in page that Ask a user for user name and page  for logining the user in. 
 It also features a language changing functionality English or french.

@author Ohzecodes
@version 1.0 
 */
public class LoginCtrl implements Initializable {
    public TextField UserNameField;
    public Button LoginButton;
    public PasswordField PasswordField;
    @FXML
    private Text TimeZoneText;
    @FXML
    private ChoiceBox<String> LanguageChoiceBox;
    public Text UPassText, UNameText,LangText, TzText,SchedulerTxt;

    /**
    * used to signing the user
    * @params ScreenName
     */
    private void signIn(ActionEvent e){
     DbCom.loginWithUserNameAndPassword(e,UserNameField.getText(),
        PasswordField.getText());
    
    }
    /**
        * This function is used to handle the language change on the Login page
    */
    public void HandlelocaleChange(){
        ResourceBundle   rr=ResourceBundle.getBundle("Internationalization.lang",l);
        UPassText.setText(rr.getString("PASSWORD"));
        UNameText.setText(rr.getString("USERNAME"));
        LangText.setText(rr.getString("LANGUAGE"));
        TzText.setText(rr.getString("TIMEZONE"));
        LoginButton.setText(rr.getString("LOGIN"));
        SchedulerTxt.setText(rr.getString("SCHEDULER"));

        Stage primStage = (Stage) TimeZoneText.getScene().getWindow();
        primStage.setTitle(rr.getString("SCHEDULER"));
    }

    /**
     * Intialize function that gets initialized when this page launches
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TimeZoneText.setText(util.getmyTZ());
        ObservableList<String> Language= FXCollections.observableArrayList("ENGLISH","FRENCH");
        LanguageChoiceBox.setItems(Language);
        if( DbCom.l== Locale.CANADA_FRENCH) {
            LanguageChoiceBox.setValue(Language.get(1));
        }else{
            LanguageChoiceBox.setValue(Language.get(0));
        }
        LanguageChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(Objects.equals(t1, "ENGLISH")){
                    DbCom.l=new Locale("EN","CA");

                }else{
                    DbCom.l=new Locale("FR","CA");
                }
                HandlelocaleChange();

            }
        });
        LoginButton.setOnAction(e->signIn(e));
    }

}