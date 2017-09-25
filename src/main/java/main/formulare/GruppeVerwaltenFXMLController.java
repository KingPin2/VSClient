/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.objects.Group;
import main.objects.User;

/**
 * FXML Controller class
 *
 * @author Laura
 */
public class GruppeVerwaltenFXMLController implements Initializable
{

    /**
     * Initializes the controller class.
     */

    private PopUpMessage pm;
    @FXML private ComboBox cbGruppe;
    @FXML private ComboBox cbMember;
    @FXML private ComboBox cbUser;
    @FXML private ComboBox cbMod;

    @FXML private Button bKick;
    @FXML private Button bInvite;
    @FXML private Button bSave;
    @FXML private Button bCancel;


    private Group selectedGroup;
    private User selectedUser;
    private User selectedMod;
    private User selectedMember;


    @FXML private void close()
    {
        Stage stage = (Stage) bCancel.getScene().getWindow();
        stage.close();
    }

    @FXML private void invite()
    {
        try
        {
            ArrayList<User> user = new ArrayList<>();
            user.add(selectedUser);
          //  GUIVS.instance.getControl().getC().saveGroupMembers(selectedGroup.getID(), user);
            pm.showInformation("Einladung","User " + selectedUser.getName() + " wurde zur Gruppe hinzugefügt");
            updateGroupMembers();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @FXML private void kick()
    {
        //GUIVS.instance.getControl().getC().;

    }

    @FXML private void save()
    {
        if( ! selectedMod.equals(selectedGroup.getModerator()))
        {
            selectedGroup.setModerator(selectedMod);
        }

        try
        {
            GUIVS.instance.getControl().getC().saveGroup(selectedGroup);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML private void onChangeMod()
    {
        try
        {
            selectedMod = GUIVS.instance.getControl().getC().getUserByName( cbMod.getSelectionModel().getSelectedItem().toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML private void onChangeMember()
    {
        try
        {
            selectedMember = GUIVS.instance.getControl().getC().getUserByName( cbMember.getSelectionModel().getSelectedItem().toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML private void onChangeUser()
    {
        try
        {
            selectedUser = GUIVS.instance.getControl().getC().getUserByName( cbUser.getSelectionModel().getSelectedItem().toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @FXML private void onChangeGroup()
    {
        try
        {
            selectedGroup = GUIVS.instance.getControl().getC().getGroupByName( cbGruppe.getSelectionModel().getSelectedItem().toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        updateGroupMembers();
        updateUsers();
        updateMods();
    }

    private void updateGroupMembers()
    {
        cbMember.getItems().clear();
        try
        {

            selectedGroup.getMembers();
            if(selectedGroup.getMembers() != null)
            {
                for (User u : selectedGroup.getMembers())
                {
                    if (u != null)
                    {
                         cbMember.getItems().add(u.getName());
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void updateUsers()
    {
        cbUser.getItems().clear();
        try
        {
                for (User u : GUIVS.instance.getControl().getC().getUsersNotInGroup(selectedGroup))
                {
                    cbUser.getItems().add(u.getName());
                }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void updateMods()
    {
        try
        {
            cbMod.getItems().clear();
            //Mögliche Mods sind:

            //Alle Mitglieder der Gruppe
            ArrayList<User> members = selectedGroup.getMembers();
            selectedMod = selectedGroup.getModerator();
            if(members != null)
            {
                for (User u : members)
                {
                    cbMod.getItems().add(u.getName());
                    if(u.getName().equals(selectedMod.getName()))
                    {
                       cbMod.getSelectionModel().select(selectedMod.getName());
                    }
                }
            }
            //ohne den aktuellen Moderator
         //   cbMod.getItems().remove(GUIVS.instance.getControl().getC().getUserById(GUIVS.instance.getControl().getC().getGroupByName(cbGruppe.getSelectionModel().getSelectedItem().toString()).getModerator().getID()).getName());

            //+ Alle Administratoren
            for(User u: GUIVS.instance.getControl().getC().getUsersByLevel(2))
            {
                //wenn sie nicht bereits Member der Gruppe sind
                if(! cbMod.getItems().toString().contains(u.getName()))
                {
                    cbMod.getItems().add(u.getName());
                }

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void initGUI()
    {
        try
        {
            for (Group g : GUIVS.instance.getControl().getC().getGroups())
            {
                cbGruppe.getItems().add(g.getName());
            }
            cbGruppe.getSelectionModel().selectFirst();
            selectedGroup = GUIVS.instance.getControl().getC().getGroupByName( cbGruppe.getSelectionModel().getSelectedItem().toString());
            selectedMod = selectedGroup.getModerator();
            updateGroupMembers();
            updateUsers();
            updateMods();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
