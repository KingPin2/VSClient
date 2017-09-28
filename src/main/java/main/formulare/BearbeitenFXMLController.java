/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import javafx.application.Platform;
import javafx.stage.Stage;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.database.ObjectFactory;
import main.exceptions.DatabaseConnectionException;
import main.exceptions.DatabaseObjectNotSavedException;
import main.exceptions.UserAuthException;
import main.objects.Message;
import java.net.URL;
import java.rmi.RemoteException;
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
    @FXML private Button bVeroeffentlichen;
    public Message m;
    private PopUpMessage pm;


    public Message getM() {
        return m;
    }

    public void setM(Message m) {
        this.m = m;
    }
    

    @FXML
    private void veroeffentlichen()
    {
        m.setMessage(taNachricht.getText());
        m.setGroupId(1);
        try
        {
            GUIVS.instance.getControl().getC().saveMessage(m);
            pm.showInformation("Information", "Nachricht wurde veröffentlicht!");
            abbrechen();
        } catch (DatabaseObjectNotSavedException e)
        {
            e.printStackTrace();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (DatabaseConnectionException e)
        {
            e.printStackTrace();
        } catch (UserAuthException e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML 
    private void speichern()
    {
        m.setMessage(taNachricht.getText());
        try {
            GUIVS.instance.getControl().getC().saveMessage(m);
            pm.showInformation("Nachricht geändert", "Nachricht erfolgreich geändert!");
            abbrechen();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML private void abbrechen()
    {

        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        stage.close();
    }
    /**
     * Initializes the controller class.
     */
    private void ladeNachricht()
    {
        try{
            taNachricht.setText(this.m.getMessage());
        }
        catch (NullPointerException e)
        {
            pm.showError("Fehler", "Keine Nachricht ausgewählt oder Nachricht konnte nicht geladen werden!");
            abbrechen();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        pm = new PopUpMessage();
        Platform.runLater(new Runnable(){@Override public void run(){ladeNachricht();}});

    }    
    
}
