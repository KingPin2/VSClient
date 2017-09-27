/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.classes.Control;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.database.ObjectFactory;
import main.database.exceptions.DatabaseConnectionException;
import main.database.exceptions.DatabaseObjectNotFoundException;
import main.objects.Group;
import main.objects.User;

/**
 * FXML Controller class
 *
 * @author Laura
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


    @FXML
    private void deleteUser()
    {
        try
        {
            User user = (User) cbUserwahl.getSelectionModel().getSelectedItem();
            boolean auswahl = pm.showDialog("Möchten Sie den User " + ((User) cbUserwahl.getSelectionModel().getSelectedItem()).getName() + " wirklich löschen?");
            if (auswahl)
            {
                GUIVS.instance.getControl().getC().deleteUser(user);
                pm.showInformation("Information", "Der User wurde gelöscht.");
                this.user.clear();
                initGUI();
            } else
            {
                pm.showInformation("Information", "Der User wurde nicht gelöscht.");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

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
            User neuerUser = (User) cbUserwahl.getSelectionModel().getSelectedItem();
            neuerUser.setPassword(tfPasswort.getText());
            neuerUser.setLevel(level);

            GUIVS.instance.getControl().getC().saveUser(neuerUser);
            pm.showInformation("Information", "User erfolgreich geändert!");
            close();

        } catch (Exception e)
        {
        }
    }

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
            e.printStackTrace();
        } catch (DatabaseObjectNotFoundException e)
        {
            e.printStackTrace();
        }

    }

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
                    }
                } else
                {
                    //TODO
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
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        pm = new PopUpMessage();
        user = FXCollections.observableArrayList();
        rbUser.setToggleGroup(berechtigung);
        rbAdmin.setToggleGroup(berechtigung);

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


        // TODO
    }

}
