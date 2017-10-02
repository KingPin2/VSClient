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
 * FXML Controller Klasse
 *
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 */
public class NeueGruppeFXMLController implements Initializable {

    private PopUpMessage pm;
    @FXML
    private Button bSpeichern;

    @FXML
    private Button bAbbrechen;
    
    @FXML
    private TextField tfGruppenname;
    
    @FXML
    private ComboBox cbMod;
    

    
    private Group group = null;
    private User user;

    /**
     * schliesst das Formular
     */
    @FXML
    private void close()
    {

        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        stage.close();
    }

    /**
     * Speichert die konfigurierte Gruppe
     */
    @FXML
    private void speichern()
    {
        try
        {
            this.group = ObjectFactory.createEmptyGroup(tfGruppenname.getText(),
                    GUIVS.instance.getControl().getC().getUserByName(cbMod.getSelectionModel().getSelectedItem().toString()));
            GUIVS.instance.getControl().getC().saveGroup(this.group);
            pm.showInformation("Information","Gruppe erfolgreich gespeichert!");
            close();
        }catch(Exception e)
        {
            pm.showError("Error","Die Gruppe konnte nicht gespeichert werden!");
        }
    }

    /**
     * initialisert die ComboBox fuer die Moderatorauswahl
     */
    private void initGUI()
    {

        try
        {
            for (User u : GUIVS.instance.getControl().getC().getUsers())
            {
                if(u.getLevel() == 2)
                {
                    cbMod.getItems().add(u.getName());
                }
            }
            cbMod.getSelectionModel().selectFirst();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Wird beim Laden des Controllers getriggert.
     * @param url default-Uebergabeparameter
     * @param rb default-Uebergabeparameter
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        pm = new PopUpMessage();
        bAbbrechen.setCancelButton(true);
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
