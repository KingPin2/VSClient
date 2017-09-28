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
 * FXML Controller class
 *
 * @author Laura
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

    @FXML
    private void onGroupChange()
    {
        try
        {
            selectedGroup = cbGroup.getSelectionModel().getSelectedItem().toString() != null ? GUIVS.instance.getControl().getC().getGroupByName(cbGroup.getSelectionModel().getSelectedItem().toString()) : selectedGroup;
        } catch (DatabaseConnectionException e)
        {
            e.printStackTrace();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (DatabaseObjectNotFoundException e)
        {
            e.printStackTrace();
        } catch (UserAuthException e)
        {
            e.printStackTrace();
        }
    }


    @FXML
    private void abbrechen()
    {
        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        stage.close();
    }

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
        } catch (EmptyStringException e)
        {
            pm.showError("Fehler", "Nachricht darf nicht leer sein!");
        }
        abbrechen();
    }


    private void initGUI()
    {
        updateGroups();
        cbGroup.getSelectionModel().selectFirst();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
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
