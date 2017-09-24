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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Laura
 */
public class NeueNachrichtFXMLController implements Initializable {

    @FXML private Label lAnzeigeTafel;
    @FXML private TextArea taNachricht;
    @FXML private Button bAbbrechen;
    @FXML private Button bSenden;
    
    
    
    @FXML 
    private void abbrechen()
    {
        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        stage.close();
    }        
    
    @FXML 
    private void senden()
    {
        
    }
        
    private void editLabelText()
    {
        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        editLabelText();
        this.bAbbrechen.requestFocus();
    }    
    
}
