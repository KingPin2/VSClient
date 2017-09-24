/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;
import main.objects.*;
import main.database.*;
import main.classes.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Laura
 */



public class UserAnlegenFXMLController implements Initializable
{

   private User neuerUser;
    
   @FXML
   private Button bSpeichern;
   
   @FXML
   private Button bAbbrechen;
    
   @FXML
   private RadioButton rbUser;
   
   @FXML
   private RadioButton rbAdmin;
   
   @FXML
   private TextField tfUsername;
   
   @FXML
   private TextField tfPasswort;

   private final ToggleGroup berechtigung = new ToggleGroup();
    
       @FXML
    private void close()
    {
        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        stage.close();
    }
   
    @FXML
    private void speichereUser()
    {
       neuerUser = ObjectFactory.createUser(tfUsername.getText(), 
               tfPasswort.getText(), 
               rbUser.isSelected() ? Control.permission.USER.ordinal():Control.permission.ADMIN.ordinal());
       try{
        //GUIVS.instance.control.getC().saveUser(neuerUser);
       }catch(Exception e){}
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        rbUser.setToggleGroup(berechtigung);
        rbAdmin.setToggleGroup(berechtigung);
        rbUser.setSelected(true);
    }    
    
}
