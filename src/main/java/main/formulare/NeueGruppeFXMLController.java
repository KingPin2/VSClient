/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import main.database.*;
import main.objects.Group;
import main.objects.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Laura
 */
public class NeueGruppeFXMLController implements Initializable {

    
    @FXML
    private Button bSpeichern;
    
    @FXML
    private Button bAbbrechen;
    
    @FXML
    private TextField tfGruppenname;
    
    @FXML
    private ComboBox cbMod;
    

    
    private Group group = null;
    
    @FXML
    private void speichern()
    {
        this.group = ObjectFactory.createEmptyGroup(tfGruppenname.getText(),(User) cbMod.getSelectionModel().getSelectedItem());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        bAbbrechen.setCancelButton(true);
        
    }    
    
}
