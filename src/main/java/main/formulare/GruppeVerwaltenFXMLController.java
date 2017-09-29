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
import javafx.util.StringConverter;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.exceptions.DatabaseObjectNotFoundException;
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

    @FXML
    private void invite()
    {

        try
        {
            selectedGroup.addMember(selectedUser);
            GUIVS.instance.getControl().getC().saveGroup(selectedGroup);
            pm.showInformation("Einladung", "User " + selectedUser.getName() + " wurde zur Gruppe hinzugefügt");
            updateGroupMembers();
            updateUsers();
        } catch (Exception e)
        {
            pm.showError("Fehler", "Keine User ausgewählt!");
        }
    }

    @FXML
    private void kick()
    {
        if (cbMember.getSelectionModel().getSelectedItem() != null && !(cbMember.getSelectionModel().getSelectedItem().equals(selectedGroup.getModerator().getName())))
        {
            selectedGroup.removeMember(selectedMember);
            try
            {
                selectedGroup.removeMember(selectedMember);
                GUIVS.instance.getControl().getC().saveGroup(selectedGroup);
                updateUsers();
                updateGroupMembers();
                pm.showInformation("Information", "der User wurde aus der Gruppe " + selectedGroup.getName() + " entfernt.");
            } catch (Exception e)
            {
                e.printStackTrace();
                pm.showError("Fehler", "Kein Member ausgewählt!");
            }
        }
    }

    @FXML
    private void deleteGroup()
    {
        try
        {
                GUIVS.instance.getControl().getC().deleteGroup(selectedGroup);
                pm.showInformation("Information","Gruppe gelöscht!!");
                updateGroups();
        } catch (Exception e)
        {
            pm.showInformation("Information","Keine Gruppe ausgewählt!");
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
            selectedMod = (User) cbMod.getSelectionModel().getSelectedItem();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML private void onChangeMember()
    {
        try
        {

            selectedMember = (User) cbMember.getSelectionModel().getSelectedItem();

        } catch (Exception e)
        {
            try
            {


                cbMember.getSelectionModel().selectFirst();
                selectedMember = (User) cbMember.getSelectionModel().getSelectedItem();
            }catch(Exception e2)
            {
                pm.showError("Error", "Keine Member in Gruppe!");
                close();
            }
        }
    }

    @FXML private void onChangeUser()
    {
        try
        {

            selectedUser = (User) cbUser.getSelectionModel().getSelectedItem();

        } catch (Exception e)
        {
            try
            {


                cbUser.getSelectionModel().selectFirst();
                selectedUser = (User) cbUser.getSelectionModel().getSelectedItem();
            }catch(Exception e2)
            {
                pm.showError("Error", "Keine User gefunden!");
                close();
            }
        }
    }

    @FXML private void onChangeGroup()
    {
        try
        {
            selectedGroup = (Group) cbGruppe.getSelectionModel().getSelectedItem();
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
            cbGruppe.getItems().clear();
            cbGruppe.getItems().addAll(GUIVS.instance.getControl().getGroups());
            cbGruppe.getSelectionModel().selectFirst();
            selectedGroup = (Group) cbGruppe.getSelectionModel().getSelectedItem();
            selectedMod = selectedGroup.getModerator();
            updateGroupMembers();
            updateUsers();
            updateMods();
        } catch (NullPointerException e)
            {
            pm.showError("Error", "Keine Gruppen angelegt!");
            close();
        }

    }

    private void updateGroupMembers()
    {
        cbMember.getItems().clear();
        cbMember.getItems().addAll(selectedGroup.getMembers());
    }
    private void updateUsers()
    {
        cbUser.getItems().clear();
        cbUser.getItems().addAll(GUIVS.instance.getControl().getUsers().filtered(new Predicate<User>()
        {
            @Override
            public boolean test(User user)
            {
                if(user.getLevel() > 0)
                {
                    return true;
                }
                else
                {
                   return false;
                }
            }
        }));
        cbUser.getItems().removeAll(((Group) cbGruppe.getSelectionModel().getSelectedItem()).getMembers());
    }
    private void updateMods()
    {
        try
        {
            cbMod.getItems().clear();
            cbMod.getItems().addAll(GUIVS.instance.getControl().getUsers().filtered(new Predicate<User>()
            {
                @Override
                public boolean test(User user)
                {
                    if(user.getLevel() == 2)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }));
            cbMod.getSelectionModel().select(selectedGroup.getModerator());
            selectedMod = (User) cbMod.getSelectionModel().getSelectedItem();


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

                    cbGruppe.setConverter(new StringConverter()
                    {
                        @Override
                        public String toString(Object object)
                        {
                            return ((Group) object).getName();
                        }

                        @Override
                        public Object fromString(String string)
                        {
                            return null;
                        }
                    });
                    cbMember.setConverter(new StringConverter()
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
                    cbUser.setConverter(new StringConverter()
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
                    cbMod.setConverter(new StringConverter()
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
                    initGUI();
                }
            });

        }
    }

