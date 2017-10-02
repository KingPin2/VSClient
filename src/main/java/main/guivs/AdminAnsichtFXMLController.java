/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.guivs;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.objects.Group;
import main.objects.Message;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * FXML Controller Klasse der Hauptansicht für Administratoren
 *
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 *
 *
 * @author Jan-Merlin Geuskens, 3580970
 *
 * Stellt im Vergleich zur Useransicht zusätzliche Funktionalitäten zur Verfügung um administrationsspezifsche
 * Forumulare zu öffnen. Im Gegensatz zur Useransicht werden in der zentralen TableView alle Nachrichten,
 * unabhängig vom Autor angezeigt.
 *
 *
 */
public class AdminAnsichtFXMLController implements Initializable
{
    private PopUpMessage pm;

    private ObservableList<Message> nachrichten;
    private ObservableList<Group> groups;

    /**
     * getter
     * @return in der TableView ausgewählte Nachricht
     */
    public static Message getSelectedMessage()
    {
        return selectedMessage;
    }
    private static Message selectedMessage;


    @FXML
    private ChoiceBox cbAnzeigetafel;

    //Tabelle
    @FXML
    private TableView<Message> tTabelle;
    @FXML
    private TableColumn tcNachrichten;
    @FXML
    private TableColumn tcUser;
    @FXML
    private TableColumn tcZeitstempel;
    private Group selectedGroup;

    /**
     * Wird getriggert, wenn die ComboBox zur Anzeigetafelauswahl eine Änderung registriert
     */
    @FXML
    private void onBoardChange()
    {
        selectedGroup = cbAnzeigetafel.getSelectionModel().getSelectedItem() != null ?
                (Group) cbAnzeigetafel.getSelectionModel().getSelectedItem()
                : selectedGroup;
    }

    /**
     * Instanziiert die ausgewählte Anzeigetafel
     */
    @FXML
    private void anzeigetafel()
    {
        try
        {
            GUIVS.oeffneAnzeigetafel(selectedGroup);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * öffnet das "Gruppe anlegen" Formular
     */
    @FXML
    private void gruppeAnlegen()
    {
        try
        {
            GUIVS.gruppeAnlegen();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * abmelden und zurück zum LoginScreen
     */
    @FXML
    private void about()
    {
        pm.showInformation("Autoren","Diese Software wurde entwickelt von: " +"\n" + "\n" +

                "Jan-Merlin Geuskens, 3580970" +"\n"+
                "Dominik Bergum, 3603490" + "\n" +
                "Laura-Ann Schiestel, 3686779" + "\n" +
                "Yannick Peter Neumann, 3690024" + "\n" + "\n" +

                "Wenn Sie Fehler finden, dürfen Sie sie behalten." + "\n" +
                "Bei Fragen schlagen Sie bitte Ihren Systemadministrator."
        );
    }

    @FXML
    private void abmelden()
    {
        //TODO BugFix NullPointerException bei Relog

        pm.showError("Achtung","Diese Funktion ist leider noch nicht (fehlerfrei) implementiert");
        /*
        GUIVS.instance.setMe(null);
        GUIVS.setPreviousStage(null);
        try
        {
            GUIVS.instance.getControl().getC().disconnect();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        schliessen();
        Stage stage = new Stage();
        GUIVS.login(stage);
        */

    }

    /**
     * Öffnet das Formular "Gruppe bearbeiten"
     */
    @FXML
    private void gruppeBearbeiten()
    {
        try
        {
            GUIVS.gruppeVerwalten();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Öffnet das Formular "User verwalten"
     */
    @FXML
    private void userVerwalten()
    {
        try
        {
            GUIVS.userVerwalten();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Öffnet das Formular "Neuer User"
     */
    @FXML
    private void neuerUser()
    {
        try
        {
            GUIVS.neuerUser();
        } catch (Exception e)
        {
            System.out.println("TEST4");
            e.printStackTrace();
        }

    }

    /**
     * Öffnet das Formular "Neue Nachricht"
     */
    @FXML
    private void neueNachricht()
    {
        try
        {
            GUIVS.neueNachricht();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Öffnet das Formular "Bearbeite Nachricht"
     */
    @FXML
    private void bearbeiteNachricht()
    {
        try
        {
            selectedMessage = (Message) tTabelle.getSelectionModel().getSelectedItem();
            GUIVS.bearbeiteNachricht(selectedMessage);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Abmelden vom Server und schließen des Fensters
     */
    @FXML
    private void schliessen()
    {
        try
        {
            GUIVS.instance.getControl().getC().disconnect();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        GUIVS.instance.getControl().setC(null);
        GUIVS.instance.setMe(null);
        Stage stage = (Stage) tTabelle.getScene().getWindow();
        stage.close();
    }

    /**
     * löscht die in der TableView ausgewählte Nachricht, sofern der Benutzer zustimmt.
     */
    @FXML
    private void loeschen()
    {


        boolean b = pm.showDialog("Die ausgewählte Nachricht wird unwiderruflich gelöscht!");
        if (b == true)
        {
            if (tTabelle.getSelectionModel().getSelectedItem() != null)
            {
                try
                {
                    GUIVS.instance.getControl().getC().deleteMessage(tTabelle.getSelectionModel().getSelectedItem());
                } catch (Exception e)
                {
                    pm.showError("Error", "Die Tabelle ist leer!");
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Wird ausgeführt, wenn der FXML-Controller geladen wird.
     * im Vergleich zu anderen Methoden etwas komplizierter, siehe lokale Kommentierung
     *
     * @author Jan-Merlin Geuskens, 3580970
     *
     * @param url default-Übergabeparameter
     * @param rb default-Übergabeparameter
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        pm = new PopUpMessage();

        //Lade alle Nachrichten und Gruppen vor (Admin)
        nachrichten = GUIVS.instance.getControl().getMessages();
        groups = GUIVS.instance.getControl().getGroups();

        //Komplizierte CellValueFactory mit anonymem innerem Callback zum Extrahieren des Namens des Autors der Message
        //Da das MessageObjekt ein UserObjekt beinhaltet und kein direkter Zugriff auf den Usernamen besteht,
        //muss die Message zuerst entpackt werden. Der Autorname wird via Callback an die TableColumn zurückgeben
        tcUser.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>()
                {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> param)
                    {
                        String username = param.getValue().getAuthor().getName();
                        SimpleStringProperty name = new SimpleStringProperty(username);
                        return name;
                    }
                });
        //simple CellValueFactory zum extrahieren der Nachricht
        tcNachrichten.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));

        //CellValueFactory mit innerem Callback zum extrahieren und Formatieren des Zeitstempels
        tcZeitstempel.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>()
                {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> message)
                    {
                        SimpleStringProperty property = new SimpleStringProperty();
                        DateFormat dateFormat = new SimpleDateFormat("<dd.MM> HH:mm");
                        property.setValue(dateFormat.format(message.getValue().getTimestamp()));
                        return property;
                    }
                });


        //RowListener für die Tableview
        //Wenn auf eine Zeile doppelt geklickt wird, wird die ausgewählte Nachricht bearbeitet
        //ein einfacher Klick selektiert lediglich
        tTabelle.setRowFactory(tv ->
        {
            TableRow<Message> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                {
                    Message rowData = row.getItem();
                    try
                    {
                        selectedMessage = rowData;
                        GUIVS.bearbeiteNachricht(rowData);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });



        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                if (groups != null)
                {
                    //StringConverter für Anzeigetafel-ComboBox
                    cbAnzeigetafel.setConverter(new StringConverter()
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
                    cbAnzeigetafel.setItems(groups);
                }
                cbAnzeigetafel.getSelectionModel().selectFirst();
                selectedGroup = (Group) cbAnzeigetafel.getSelectionModel().getSelectedItem();

                //Filter für die TableView
                //Ausdruck als Lambda mit innerem Lambda und integriertem Fehlerabfang, da der Filter implizit in einem
                //seperaten Thread läuft und somit etwaige Exceptions nur von der Runtime gefangen werden können
                //--> Vermeidung von Exceptions bevor sie entstehen
                GUIVS.gruppenFilter.bind(Bindings.createObjectBinding(() ->
                        {
                            return (message -> message != null &&  message.getGroup() != null && cbAnzeigetafel != null
                                    && cbAnzeigetafel.getValue() instanceof Group
                                    && ((Group) cbAnzeigetafel.getValue()) != null ?
                                        ((Group) cbAnzeigetafel.getValue()).getID() == message.getGroup().getID()
                                        : false);

                        },
                        cbAnzeigetafel.valueProperty()));
                //wrappen der ObservableList in einer FilteredList
                FilteredList<Message> gefilterteNachrichten = new FilteredList<Message>(nachrichten, p -> true);
                //setzen des Filters
                gefilterteNachrichten.predicateProperty().bind(Bindings.createObjectBinding
                (
                        () -> GUIVS.gruppenFilter.get(), GUIVS.gruppenFilter)
                );

                //wrappen der filteredList in einer SortedList
                SortedList<Message> sortierteNachrichten = new SortedList<Message>(gefilterteNachrichten);

                //zuweisen der Daten zur Tablle
                tTabelle.setItems(sortierteNachrichten);

                //binden des Comperators der Nachrichten an den Comperator der TableView
                sortierteNachrichten.comparatorProperty().bind(tTabelle.comparatorProperty());

                //dreht die Sortierung für Zeitstempel um ( ASC --> DESC)
                tcZeitstempel.setComparator(tcZeitstempel.getComparator().reversed());

                //setze Die Sortierung in der TableView
                tTabelle.getSortOrder().add(tcZeitstempel);

                //registriere ChangeListener auf die FilteredList
                //(und damit impliziet auf die zu Grunde liegende externe ObservableList)
                gefilterteNachrichten.addListener(new ListChangeListener<Message>()
                {
                    @Override
                    public void onChanged(Change<? extends Message> c)
                    {
                        //Wenn sich die Liste ändert, sortiere die Tabelle erneut
                        //(Elemente werden auf Grund der Eigenschaften der ObservableList
                        //automatisch hinzugefügt, jedoch am Ende der Tabelle eingefügt)
                        Platform.runLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                tTabelle.sort();
                            }
                        });
                    }
                });

            }
        });


    }
}


