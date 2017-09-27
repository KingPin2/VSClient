/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import javafx.scene.control.*;
import main.classes.Control;
import main.objects.*;
import main.database.*;
import main.classes.*;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Laura
 */


public class UserAnlegenFXMLController implements Initializable
{

    private PopUpMessage pm;
    private User neuerUser;
    private final ToggleGroup berechtigung = new ToggleGroup();
    ;

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
    private PasswordField tfPasswort;

    @FXML
    private void close()
    {
        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void speichereUser()
    {
        int level = 0;
        if (rbUser.isSelected())
        {

            level = 1;
        } else
        {
            level = 2;
        }

        try
        {
            neuerUser = ObjectFactory.createUser(tfUsername.getText(), tfPasswort.getText(), level);
            GUIVS.instance.getControl().getC().saveUser(neuerUser);
            pm.showInformation("Information", "User erfolgreich angelegt!");
            close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        rbUser.setToggleGroup(berechtigung);
        rbAdmin.setToggleGroup(berechtigung);
        rbUser.setSelected(true);
        pm = new PopUpMessage();
    }

}
