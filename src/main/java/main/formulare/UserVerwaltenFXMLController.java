/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.exceptions.DatabaseConnectionException;
import main.exceptions.DatabaseObjectNotFoundException;
import main.exceptions.UserAuthException;
import main.objects.User;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * FXML Controller Klasse des Formulars "User verwalten"
 *
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 *
 * User koennen Ueber die ComboBox ausgewaehlt werden.
 * Der Username ist nicht aenderbar.
 * Aenderbar sind Passwort und Berechtigung.
 *
 */
public class UserVerwaltenFXMLController implements Initializable
{
    private PopUpMessage pm;
    private ObservableList<User> user;
    private final ToggleGroup berechtigung = new ToggleGroup();

    @FXML
    private Button bSpeichern;
    @FXML
    private Button bDelete;
    @FXML
    private Button bAbbrechen;
    @FXML
    private RadioButton rbUser;
    @FXML
    private RadioButton rbAdmin;
    @FXML
    private ComboBox cbUserwahl;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPasswort;

    /**
     * loescht den ausgewaehlten User, sofern der Anwender es bestaetigt
     */
    @FXML
    private void deleteUser()
    {
        try
        {
            User user = (User) cbUserwahl.getSelectionModel().getSelectedItem();
            boolean auswahl = pm.showDialog("Moechten Sie den User " + ((User) cbUserwahl.getSelectionModel().getSelectedItem()).getName() + " wirklich loeschen?");
            if (auswahl)
            {
                GUIVS.instance.getControl().getC().deleteUser(user);
                pm.showInformation("Information", "Der User wurde geloescht.");
                this.user.clear();
                initGUI();
            } else
            {
                pm.showInformation("Information", "Der User wurde nicht geloescht.");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

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
     * speichert den geaenderten User
     */
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
            User user = (User) cbUserwahl.getSelectionModel().getSelectedItem();
            user.setPassword(tfPasswort.getText());
            user.setLevel(level);

            GUIVS.instance.getControl().getC().saveUser(user);
            pm.showInformation("Information", "User erfolgreich geaendert!");
            close();

        } catch (Exception e)
        {
        }
    }

    /**
     * Initialisiert die Benutzeroberflaeche
     */
    private void initGUI()
    {
        try
        {
            if (user != null)
            {
                user.clear();
            }
            for (User u : GUIVS.instance.getControl().getC().getUsers())
            {
                user.add(u);
            }
            cbUserwahl.setItems(user);
            cbUserwahl.getSelectionModel().selectFirst();
        } catch (DatabaseConnectionException e)
        {
            e.printStackTrace();
        } catch (RemoteException e)
        {
            pm.showError("Error","Der Server ist momentan nicht zu erreichen, bitte versuchen Sie es spaeter erneut");
        } catch (DatabaseObjectNotFoundException e)
        {
            e.printStackTrace();
        } catch (UserAuthException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * wird getrigget, sobald in der ComboBox User eine Auswahl erfolgt
     */
    @FXML
    private void selectUser()
    {
        try
        {
            if (cbUserwahl.getSelectionModel().getSelectedItem() != null)
            {
                User u = (User) cbUserwahl.getSelectionModel().getSelectedItem();
                if (u != null)
                {
                    tfUsername.setText(u.getName());
                    tfPasswort.setText(u.getPassword());
                    switch (u.getLevel())
                    {
                        case 1:
                            rbUser.setSelected(true);
                            break;
                        case 2:
                            rbAdmin.setSelected(true);
                            break;
                        default:
                            rbUser.setSelected(true);
                    }
                } else
                {
                    // void
                }
            } else
            {
                cbUserwahl.getSelectionModel().selectFirst();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Wird beim Laden des FXML-Controller ausgefuehrt
     * @param url default-Uebergabeparameter
     * @param rb default-Uebergabeparameter
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        pm = new PopUpMessage();
        user = FXCollections.observableArrayList();

        rbUser.setToggleGroup(berechtigung);
        rbAdmin.setToggleGroup(berechtigung);
        /**
         * StringCoverter setzen
         */
        cbUserwahl.setConverter(new StringConverter()
        {
            @Override
            public String toString(Object object)
            {
                return ((User) object).getName();
            }

            @Override
            public Object fromString(String string)
            {
                return null;
            }
        });
        cbUserwahl.setItems(user);

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
