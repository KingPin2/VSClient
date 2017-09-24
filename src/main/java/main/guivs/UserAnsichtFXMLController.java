/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.guivs;

import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.objects.Message;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Laura
 */
public class UserAnsichtFXMLController implements Initializable
{
    private PopUpMessage pm;
   
    
    
    @FXML
    private Button bNeueNachricht;
    
    @FXML
    private Button bBearbeiten;
    
    @FXML
    private Button bLoeschen;
    
    @FXML
    private Button bAnzeigetafel;
    
    @FXML
    private TableView<Message> tTabelle;
    
    @FXML
    private TableColumn tcNachrichten;
    
    @FXML
    private MenuItem miAktualisieren;
    
    @FXML
    private MenuItem miAnzeigetafel;

    @FXML
    private MenuItem miAbmelden;
 
    @FXML
    private MenuItem miSchliessen;
 
    @FXML
    private MenuItem miNeueNachricht;
   
    @FXML
    private MenuItem miBearbeiten;

    @FXML
    private MenuItem miLoeschen;

    @FXML
    private MenuItem miAbout;
 
    @FXML
    private void neueNachricht()
    {
        try{
          GUIVS.neueNachricht(); 
        }
        catch( Exception e){}
    }
    
    @FXML 
    private void bearbeiteNachricht()
    {
        try
        {
            GUIVS.bearbeiteNachricht((Message) tTabelle.getSelectionModel().getSelectedItem());
        }
        catch(Exception e){} 
    }
    
    @FXML
    private void schliessen()
    {
        Stage stage = (Stage) tTabelle.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void loeschen()
    {
        pm.showDialog("Die ausgewählte Nachricht wird unwiderruflich gelöscht!");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        pm = new PopUpMessage();
        //Row-Listener
        tTabelle.setRowFactory( tv -> {
        TableRow<Message> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
            Message rowData = row.getItem();
            try{
                GUIVS.bearbeiteNachricht(rowData);
            }catch(Exception e)
            {
                pm.showError("Exception", e.toString());
            }
        }
        });
        return row ;
});
    }    
    
}
