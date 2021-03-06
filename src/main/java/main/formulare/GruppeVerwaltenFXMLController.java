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
import main.exceptions.DatabaseConnectionException;
import main.exceptions.DatabaseObjectNotFoundException;
import main.exceptions.UserAuthException;
import main.objects.Group;
import main.objects.User;
import java.net.URL;
import java.rmi.RemoteException;
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
 * Allgemeine Erklaerungen:
 *
 * In den ComboBoxen werden die Datenbankobjekte User und Group gespeichert, lediglich die toString() Methode
 * wird mittels StringConverter ueberschrieben. Dies hat die Programmierung etwas erschwert, fuehrt aber zu
 * verbessertem Netzwerktraffic und leichter lesbarem Code, da nicht bei jeder Auswahl erneut ein Objekt vom
 * Server abgefragt werden muss.
 *
 * Die Buttons neben den Comboboxen dienen nur der Bereitstellung der Funktionalitaeten. Aus Zeitgruenden wurde die
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
            pm.showInformation("Einladung", "User " + selectedUser.getName() + " wurde zur Gruppe hinzugefuegt");
            updateGroupMembers();
            updateUsers();
        } catch (Exception e)
        {
            pm.showError("Fehler", "Sie haben keinen User ausgewaehlt!");
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
                pm.showError("Fehler", "Kein Member ausgewaehlt!");
            }
        }
    }

    /**
     * Methode die beim Klick auf den Button "Loeschen" getriggert wird
     */
    @FXML
    private void deleteGroup()
    {
        try
        {
            GUIVS.instance.getControl().getC().deleteGroup(selectedGroup);
            pm.showInformation("Information", "Gruppe geloescht!");
            updateGroups();
        } catch (Exception e)
        {
            pm.showInformation("Information", "Keine Gruppe ausgewaehlt!");
            e.printStackTrace();
        }
    }

    /**
     * Methode die beim Klick auf den Button "Speichern" getriggert wird.
     * bezieht sich nur auf die Aenderung des GruppenModerators
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
            pm.showInformation("Information", "Moderator wurde nicht geaendert");
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
     * wird beim Aendern der Auswahl in der Combobox Moderator getriggert
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
     * wird beim Aendern der Auswahl in der ComboBox Member getriggert
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
     * wird beim Aendern der Auswahl in der ComboBox User getriggert
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
     * wird beim Aendern der Auswahl in der ComboBox Gruppe getriggert
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
        //Bei Gruppenaenderung werden alle anderen ComboBoxen auch aktualisiert
        updateGroupMembers();
        updateUsers();
        updateMods();
    }

    /**
     * ComboBox-update fuer Gruppen
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
     * ComboBox-Update fuer Gruppenmitglieder
     */
    private void updateGroupMembers()
    {
        cbMember.getItems().clear();
        cbMember.getItems().addAll(selectedGroup.getMembers());
        //Der GruppenUser soll nicht angezeigt werden
        try
        {
            cbMember.getItems().remove(GUIVS.instance.getControl().getC().getUserByName(selectedGroup.getName()));
        } catch (DatabaseConnectionException e)
        {
            e.printStackTrace();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (DatabaseObjectNotFoundException e)
        {
            e.printStackTrace();
        } catch (UserAuthException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * ComboBox-Update fuer nicht-Gruppenmitglieder
     * Anzeigetafel-Benutzer (Level-0) koennen nicht zu einer Gruppe hinzugefuegt werden
     */
    private void updateUsers()
    {
        cbUser.getItems().clear();
        cbUser.getItems().addAll(GUIVS.instance.getControl().getUsers().filtered(new Predicate<User>()
        {
            @Override
            public boolean test(User user)
            {
                return user.getLevel() > 0;
            }
        }));
        cbUser.getItems().removeAll(((Group) cbGruppe.getSelectionModel().getSelectedItem()).getMembers());
    }

    /**
     * ComboBox-Update fuer Moderatoren
     * Nur Level 2 User (Administratoren) koennen Moderator werden
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
                    return user.getLevel() == 2;
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
     * wird beim Aufruf des Formulars ausgefuehrt.
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
     * @param url default-Uebergabeparameter
     * @param rb default-Uebergabeparameter
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO

        pm = new PopUpMessage();
        Platform.runLater(new Runnable()
        {
            /**
             * StringConverter fuer ComboBoxen setzen und initialies fuellen der ComboBoxes
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

