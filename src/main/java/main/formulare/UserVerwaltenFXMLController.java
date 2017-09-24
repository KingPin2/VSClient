/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Laura
 */
public class UserVerwaltenFXMLController implements Initializable
{

   @FXML
   private Button bSpeichern;
   
   @FXML
   private Button bAbbrechen;
    
   @FXML
   private RadioButton rbUser;
   
   @FXML
   private RadioButton rbAdmin;
   
   @FXML
   private ComboBox cbUserwahl;
   
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
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    
    
}
