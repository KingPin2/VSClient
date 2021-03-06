/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.database.ObjectFactory;
import main.exceptions.*;
import main.objects.Group;
import main.objects.Message;

/**
 * FXML Controller Klasse des Formulars "neue Nachricht"
 *
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 */
public class NeueNachrichtFXMLController implements Initializable
{

    private PopUpMessage pm;
    @FXML
    private Label lAnzeigeTafel;
    @FXML
    private TextArea taNachricht;
    @FXML
    private Button bAbbrechen;
    @FXML
    private Button bSenden;
    @FXML
    private ChoiceBox cbGroup;
    private Group selectedGroup;

    /**
     * wird getriggert, wenn Gruppe in ComboBox ausgewaehlt wird
     */
    @FXML
    private void onGroupChange()
    {
        try
        {
            //Wenn die ausgewaehlte Gruppe nicht null ist, lade die Gruppe vom Server (bzw. aus dem Cache)
            selectedGroup = cbGroup.getSelectionModel().getSelectedItem().toString() != null ?
                    GUIVS.instance.getControl().getC().getGroupByName(cbGroup.getSelectionModel().getSelectedItem().toString())
                    : selectedGroup;
        } catch (DatabaseConnectionException e)
        {
            e.printStackTrace();
        } catch (RemoteException e)
        {
            pm.showError("Error","Der Server ist momentan nicht zu erreichen, bitte versuchen Sie es spaeter erneut");
            e.printStackTrace();
        } catch (DatabaseObjectNotFoundException e)
        {
            e.printStackTrace();
        } catch (UserAuthException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * schliesst das Formular
     */
    @FXML
    private void abbrechen()
    {
        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        stage.close();
    }

    /**
     * Update fuer die Anzeigetafel-Combobox
     */
    private void updateGroups()
    {
        cbGroup.getItems().clear();
            ArrayList<Group> groups =  new ArrayList<>();
            groups.addAll(GUIVS.instance.getControl().getGroups());
            if (groups != null)
            {
                for (Group g : groups)
                {
                    cbGroup.getItems().add(g.getName());
                }
            }

    }

    /**
     * sendet die erstellte Nachricht an den Server
     */
    @FXML
    private void senden()
    {


        try
        {
            if (taNachricht.getText() == null || taNachricht.getText().equals(""))
            {
                throw new EmptyStringException();
            }
            Message m = ObjectFactory.createGroupMessage(taNachricht.getText(), GUIVS.instance.getMe(), selectedGroup);
            GUIVS.instance.getControl().getC().saveMessage(m);
            pm.showInformation("Information", "Ihre Nachricht wurde gespeichert!");
            //schliessen des Fensters
        } catch (DatabaseObjectNotSavedException e)
        {
            e.printStackTrace();
        } catch (RemoteException e)
        {
            pm.showError("Error","Der Server ist momentan nicht zu erreichen, bitte versuchen Sie es spaeter erneut");
        } catch (DatabaseConnectionException e)
        {
            e.printStackTrace();
        } catch (UserAuthException e)
        {
            e.printStackTrace();
        } catch (EmptyStringException e)
        {
            pm.showError("Fehler", "Nachricht darf nicht leer sein!");
        }
        abbrechen();
    }

    /**
     * Initialisiert die Oberflaeche des Formulars
     */
    private void initGUI()
    {
        updateGroups();
        cbGroup.getSelectionModel().selectFirst();
    }

    /**
     * Initialisiert den FXML-Controller des Formulars
     * @param url default-Uebergabeparameter
     * @param rb default-Uebergabeparameter
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //Focus auf Abbrechen-Button legen
        this.bAbbrechen.requestFocus();
        pm = new PopUpMessage();
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                initGUI();
            }
        });

    }

}
