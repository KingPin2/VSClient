/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.database.ObjectFactory;
import main.exceptions.EmptyStringException;
import main.objects.User;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
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
        int level;
        if (rbUser.isSelected())
        {

            level = 1;
        } else
        {
            level = 2;
        }

        try
        {
            if (tfUsername.getText().equals("") || tfPasswort.getText().equals(""))
            {
                throw new EmptyStringException();
            }
            neuerUser = ObjectFactory.createUser(tfUsername.getText(), tfPasswort.getText(), level);
            GUIVS.instance.getControl().getC().saveUser(neuerUser);
            pm.showInformation("Information", "User erfolgreich angelegt!");
            close();
        } catch (EmptyStringException es)
        {
            pm.showError("Fehler", "Username und Passwort dürfen nicht leer sein!");
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
