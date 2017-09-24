/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.guivs;

import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.classes.Control;
import main.exceptions.EmptyStringException;
import main.exceptions.IllegalCharacterException;
import main.exceptions.*;
import main.exceptions.NoUserFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.objects.*;

/**
 *
 * @author Laura
 */
public class loginFXMLController implements Initializable {
    
    
    @FXML
    private TextField tfUsername;
    
    @FXML
    private PasswordField pfPassword;
    
    @FXML
    private Button bLogin;
    
    @FXML
    private void login()
    {
        
        PopUpMessage pm = new PopUpMessage();
        try
        {
            Control.isLegit(tfUsername.getText());
            Control.isLegit(pfPassword.getText());


            GUIVS.instance.setMe
            (
                    //TODO Client.login gibt userobjekt zurück
                GUIVS.instance.getControl().getC().loginUser(tfUsername.getText(),pfPassword.getText())
                    
            );
            
            if(GUIVS.instance.getMe() == null)
            {
                throw new NoUserFoundException();
            }
            
            if(GUIVS.instance.getMe().getLevel() == 1)
            {
                for(Group g: GUIVS.instance.getControl().getC().getGroups())
                {
                    if(g.getModerator().equals(GUIVS.instance.getMe()))
                    {
                        GUIVS.instance.setIsMod(true);
                        break;
                    }
                }
                if(GUIVS.instance.isMod())
                {
                    Stage stage = (Stage) bLogin.getScene().getWindow();
                    stage.close();
                    GUIVS.adminAnsicht();
                }
                else
                {
                    Stage stage = (Stage) bLogin.getScene().getWindow();
                    stage.close();
                    GUIVS.userAnsicht();
                }
            }
            else if(GUIVS.instance.getMe().getLevel() == 2)
            {
                //TODO: öffne Adminansicht
                Stage stage = (Stage) bLogin.getScene().getWindow();
                stage.close();
                GUIVS.adminAnsicht();
            }
            else
            {
                throw new IllegalPermissionLevelException();
            }

        }
        catch(IllegalCharacterException icex)
        {
            pm.showError("Error", "Eingaben dürfen weder ' noch ` enthalten");
        }
        catch(EmptyStringException esex)
        {
            pm.showError("Error", "Eingaben dürfen nicht leer sein!");
        }
        catch(NoUserFoundException nufex)
        {
            pm.showError("Error", "Username oder Passwort falsch!");
        }
        catch(Exception e)
        {
            pm.showError("Error", "Exception: " + e.toString());
            System.err.println(e.toString());
        }
        finally
        {
            pm = null;
        }

    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
