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
import javafx.beans.binding.ObjectExpression;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.database.exceptions.DatabaseObjectNotFoundException;
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

    ObservableList<Group> gruppen;
    ObservableList<User> member;
    ObservableList<User> users;


    @FXML private void close()
    {
        Stage stage = (Stage) cbGruppe.getScene().getWindow();
        stage.close();
    }

    @FXML private void invite()
    {
        if(cbUser.getSelectionModel().getSelectedItem() != null) {
            try {

                selectedGroup.addMember(selectedUser);
                GUIVS.instance.getControl().getC().saveGroup(selectedGroup);
                pm.showInformation("Einladung", "User " + selectedUser.getName() + " wurde zur Gruppe hinzugefügt");
                updateGroupMembers();
                updateUsers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            pm.showError("Fehler", "Keine User ausgwählt!");
        }

    }

    @FXML private void kick()
    {
        if(cbMember.getSelectionModel().getSelectedItem() != null && !(cbMember.getSelectionModel().getSelectedItem().equals(selectedGroup.getModerator().getName()))) {
            selectedGroup.removeMember(selectedMember);
            try {
                GUIVS.instance.getControl().getC().saveGroup(selectedGroup);
                updateUsers();
                updateGroupMembers();
                pm.showInformation("Information", "der User wurde aus der Gruppe " + selectedGroup.getName() + " entfernt.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(cbMember.getSelectionModel().getSelectedItem() != null && cbMember.getSelectionModel().getSelectedItem().equals(selectedGroup.getModerator().getName()))
        {
            pm.showError("Fehler", "Um dieses Mitglied der Gruppe zu entfernen, müssen Sie vorher einen anderen Moderator festlegen!!");
        }
        else
        {
            pm.showError("Fehler", "Kein Member ausgewählt!");
        }

    }

    @FXML
    private void deleteGroup()
    {
        try {
            if(selectedGroup != null) {
                GUIVS.instance.getControl().getC().deleteGroup(selectedGroup);
                pm.showInformation("Information","Gruppe gelöscht!!");
                updateGroups();
            }
            else
            {
                pm.showInformation("Information","Keine Gruppe ausgewählt!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML private void save()
    {
        if( ! selectedMod.equals(selectedGroup.getModerator()))
        {
            selectedGroup.setModerator(selectedMod);
            pm.showInformation("Information","Neuer Moderator gespeichert");
        }
        else
        {
            pm.showInformation("Information","Moderator wurde nicht geändert");
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
            //selectedMod = GUIVS.getcbMod.getSelectionModel().getSelectedItem());

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML private void onChangeMember()
    {
        try
        {

            if(cbMember.getSelectionModel().getSelectedItem() != null)
            {
                selectedMember = GUIVS.instance.getControl().getC().getUserByName(cbMember.getSelectionModel().getSelectedItem().toString());
            }else
            {
                cbMember.getSelectionModel().selectFirst();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML private void onChangeUser()
    {
        try
        {
            if( cbUser.getSelectionModel().getSelectedItem() != null)
            {
                selectedUser = GUIVS.instance.getControl().getC().getUserByName(cbUser.getSelectionModel().getSelectedItem().toString());
            }
            else
            {
                cbUser.getSelectionModel().selectFirst();
            }
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
    private void updateGroups()
    {
        try
        {
            if(GUIVS.instance.getControl().getC().getGroups() != null) {
                for (Group g : GUIVS.instance.getControl().getC().getGroups()) {
                    cbGruppe.getItems().add(g.getName());
                }

                cbGruppe.getSelectionModel().selectFirst();
                selectedGroup = GUIVS.instance.getControl().getC().getGroupByName(cbGruppe.getSelectionModel().getSelectedItem().toString());
                selectedMod = selectedGroup.getModerator();
                updateGroupMembers();
                updateUsers();
                updateMods();
            }else
            {
                pm.showError("Error","Keine Gruppen angelegt!");
                close();
            }

        }
        catch (DatabaseObjectNotFoundException e)
        {
            pm.showError("Error","Keine Gruppen angelegt!");
            close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateGroupMembers()
    {
        cbMember.getItems().clear();
        try
        {
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
            ArrayList<User> notInGroup = GUIVS.instance.getControl().getC().getUsersNotInGroup(selectedGroup);
                    if(notInGroup != null) {
                        for (User u : notInGroup) {
                            cbUser.getItems().add(u.getName());
                        }
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
            ArrayList<User> possibleMods = selectedGroup.getMembers();
            ArrayList<User> admins = GUIVS.instance.getControl().getC().getUsersByLevel(2);
            for(User u: admins)
            {
                if(! possibleMods.contains(u))
                {
                    possibleMods.add(u);
                }
            }



                if (possibleMods != null)
                {
                    for (User u : possibleMods)
                    {
                        cbMod.getItems().add(u.getName());
                        if (u.getName().equals(selectedGroup.getModerator().getName()))
                        {
                            selectedMod = selectedGroup.getModerator();
                            cbMod.getSelectionModel().select(selectedMod.getName());
                        }
                    }
                }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void initGUI()
    {
            updateGroups();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

            pm = new PopUpMessage();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initGUI();
                }
            });

        }
    }

