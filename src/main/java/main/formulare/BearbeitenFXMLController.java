/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import javafx.stage.Stage;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.database.ObjectFactory;
import main.objects.Message;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Laura
 */
public class BearbeitenFXMLController implements Initializable {

    @FXML private Label lAnzeigeTafel;
    @FXML private TextArea taNachricht;
    @FXML private Button bAbbrechen;
    @FXML private Button bSpeichern;
    private Message m;

    public Message getM() {
        return m;
    }

    public void setM(Message m) {
        this.m = m;
    }
    
   
    
    @FXML 
    private void speichern()
    {
        m.setMessage(taNachricht.getText());
        try {
            GUIVS.instance.getControl().getC().saveMessage(m);
            PopUpMessage pm = new PopUpMessage();
            pm.showInformation("Nachricht geändert", "Nachricht erfolgreich geändert!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        
    private void editLabelText()
    {
        
    }
    @FXML private void abbrechen()
    {

        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        stage.close();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(m.equals(null))
        {
            m = ObjectFactory.createMessage("Nachricht konnte nicht geladen werden", GUIVS.instance.getMe());
        }
        bAbbrechen.setCancelButton(true);
        editLabelText();
        taNachricht.setText(m.getMessage());
    }    
    
}
