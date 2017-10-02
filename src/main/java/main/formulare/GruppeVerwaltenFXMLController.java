/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.formulare;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.objects.Group;
import main.objects.User;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * FXML Controller Klasse des Formulars Gruppe Vewalten
 *
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 *
 *
 *
 * @author Jan-Merlin Geuskens , 3580970
 * Allgemeine Erklärungen:
 *
 * In den ComboBoxen werden die Datenbankobjekte User und Group gespeichert, lediglich die toString() Methode
 * wird mittels StringConverter überschrieben. Dies hat die Programmierung etwas erschwert, führt aber zu
 * verbessertem Netzwerktraffic und leichter lesbarem Code, da nicht bei jeder Auswahl erneut ein Objekt vom
 * Server abgefragt werden muss.
 *
 * Die Buttons neben den Comboboxen dienen nur der Bereitstellung der Funktionalitäten. Aus Zeitgründen wurde die
 * Umsetzung von IconButtons an Stelle der normalen Buttons gestrichen.
 *
 */
public class GruppeVerwaltenFXMLController implements Initializable
{

    private PopUpMessage pm;
    @FXML
    private ComboBox cbGruppe;
    @FXML
    private ComboBox cbMember;
    @FXML
    private ComboBox cbUser;
    @FXML
    private ComboBox cbMod;

    private Group selectedGroup;
    private User selectedUser;
    private User selectedMod;
    private User selectedMember;

    /**
     * Schliesst das Formular
     */
    @FXML
    private void close()
    {
        Stage stage = (Stage) cbGruppe.getScene().getWindow();
        stage.close();
    }

    /**
     * Methode die beim Klick auf den Button "einladen" getriggert wird
     */
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
            pm.showError("Fehler", "Sie haben keinen User ausgewählt!");
        }
    }

    /**
     * Methode die beim Klick auf den Button "Entfernen" getriggert wird
     */
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

    /**
     * Methode die beim Klick auf den Button "Löschen" getriggert wird
     */
    @FXML
    private void deleteGroup()
    {
        try
        {
            GUIVS.instance.getControl().getC().deleteGroup(selectedGroup);
            pm.showInformation("Information", "Gruppe gelöscht!");
            updateGroups();
        } catch (Exception e)
        {
            pm.showInformation("Information", "Keine Gruppe ausgewählt!");
            e.printStackTrace();
        }
    }

    /**
     * Methode die beim Klick auf den Button "Speichern" getriggert wird.
     * bezieht sich nur auf die Änderung des GruppenModerators
     */
    @FXML
    private void save()
    {
        if (!selectedMod.equals(selectedGroup.getModerator()))
        {
            selectedGroup.setModerator(selectedMod);
            pm.showInformation("Information", "Neuer Moderator gespeichert");
        } else
        {
            pm.showInformation("Information", "Moderator wurde nicht geändert");
        }

        try
        {
            GUIVS.instance.getControl().getC().saveGroup(selectedGroup);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * wird beim Ändern der Auswahl in der Combobox Moderator getriggert
     */
    @FXML
    private void onChangeMod()
    {
        try
        {
            selectedMod = (User) cbMod.getSelectionModel().getSelectedItem();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * wird beim Ändern der Auswahl in der ComboBox Member getriggert
     */
    @FXML
    private void onChangeMember()
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
            } catch (Exception e2)
            {
                pm.showError("Error", "Keine Member in Gruppe!");
                close();
            }
        }
    }

    /**
     * wird beim Ändern der Auswahl in der ComboBox User getriggert
     */
    @FXML
    private void onChangeUser()
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
            } catch (Exception e2)
            {
                pm.showError("Error", "Keine User gefunden!");
                close();
            }
        }
    }

    /**
     * wird beim Ändern der Auswahl in der ComboBox Gruppe getriggert
     */
    @FXML
    private void onChangeGroup()
    {
        try
        {
            selectedGroup = (Group) cbGruppe.getSelectionModel().getSelectedItem();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //Bei Gruppenänderung werden alle anderen ComboBoxen auch aktualisiert
        updateGroupMembers();
        updateUsers();
        updateMods();
    }

    /**
     * ComboBox-update für Gruppen
     */
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

    /**
     * ComboBox-Update für Gruppenmitglieder
     */
    private void updateGroupMembers()
    {
        cbMember.getItems().clear();
        cbMember.getItems().addAll(selectedGroup.getMembers());
    }

    /**
     * ComboBox-Update für nicht-Gruppenmitglieder
     * Anzeigetafel-Benutzer (Level-0) können nicht zu einer Gruppe hinzugefügt werden
     */
    private void updateUsers()
    {
        cbUser.getItems().clear();
        cbUser.getItems().addAll(GUIVS.instance.getControl().getUsers().filtered(new Predicate<User>()
        {
            @Override
            public boolean test(User user)
            {
                if (user.getLevel() > 0)
                {
                    return true;
                } else
                {
                    return false;
                }
            }
        }));
        cbUser.getItems().removeAll(((Group) cbGruppe.getSelectionModel().getSelectedItem()).getMembers());
    }

    /**
     * ComboBox-Update für Moderatoren
     * Nur Level 2 User (Administratoren) können Moderator werden
     */
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
                    if (user.getLevel() == 2)
                    {
                        return true;
                    } else
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

    /**
     * wird beim Aufruf des Formulars ausgeführt.
     * Mit updateGroups() werden auch alle anderen ComboBoxen aktualisiert.
     *
     * Namensgebung nur zur besseren Lesbarkeit der initalize-Methode
     */
    private void initGUI()
    {
        updateGroups();
    }

    /**
     * Wird beim laden des FXMLControllers getriggert
     * @param url default-Übergabeparameter
     * @param rb default-Übergabeparameter
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO

        pm = new PopUpMessage();
        Platform.runLater(new Runnable()
        {
            /**
             * StringConverter für ComboBoxen setzen und initialies füllen der ComboBoxes
             */
            @Override
            public void run()
            {

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

