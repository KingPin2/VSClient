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

import static java.lang.System.exit;

/**
 * FXML Controller Klasse der Hauptansicht fuer Administratoren
 *
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 *
 *
 * @author Jan-Merlin Geuskens, 3580970
 *
 * Stellt im Vergleich zur Useransicht zusaetzliche Funktionalitaeten zur Verfuegung um administrationsspezifsche
 * Formulare zu oeffnen. Im Gegensatz zur Useransicht werden in der zentralen TableView alle Nachrichten,
 * unabhaengig vom Autor angezeigt.
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
     * @return in der TableView ausgewaehlte Nachricht
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
     * Wird getriggert, wenn die ComboBox zur Anzeigetafelauswahl eine Aenderung registriert
     */
    @FXML
    private void onBoardChange()
    {
        selectedGroup = cbAnzeigetafel.getSelectionModel().getSelectedItem() != null ?
                (Group) cbAnzeigetafel.getSelectionModel().getSelectedItem()
                : selectedGroup;
    }

    /**
     * Instanziiert die ausgewaehlte Anzeigetafel
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
     * oeffnet das "Gruppe anlegen" Formular
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
     * abmelden und zurueck zum LoginScreen
     */
    @FXML
    private void about()
    {
        pm.showInformation("Autoren","Diese Software wurde entwickelt von: " +"\n" + "\n" +

                "Jan-Merlin Geuskens,"+"\t" + "\t" + "3580970" +"\n"+
                "Dominik Bergum,"+ "\t" + "\t" + "\t" + "3603490" + "\n" +
                "Laura-Ann Schiestel,"+ "\t" + "\t"+ "3686779" + "\n" +
                "Yannick Peter Neumann,"+"\t"+"\t"+ "3690024" + "\n" + "\n" +

                "Wenn Sie Fehler finden, duerfen Sie sie behalten." + "\n" +
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
     * Oeffnet das Formular "Gruppe bearbeiten"
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
     * Oeffnet das Formular "User verwalten"
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
     * Oeffnet das Formular "Neuer User"
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
     * Oeffnet das Formular "Neue Nachricht"
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
     * Oeffnet das Formular "Bearbeite Nachricht"
     */
    @FXML
    private void bearbeiteNachricht()
    {
        try
        {
            selectedMessage = tTabelle.getSelectionModel().getSelectedItem();
            GUIVS.bearbeiteNachricht(selectedMessage);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Abmelden vom Server und schliessen des Fensters
     */
    @FXML
    private void schliessen()
    {
        try
        {
            GUIVS.instance.getControl().getC().disconnect();
        } catch (RemoteException e)
        {
            pm.showError("Error","Der Server ist momentan nicht zu erreichen, bitte versuchen Sie es spaeter erneut");
        }
        GUIVS.instance.getControl().setC(null);
        GUIVS.instance.setMe(null);
        Platform.exit();
        exit(0);
    }

    /**
     * loescht die in der TableView ausgewaehlte Nachricht, sofern der Benutzer zustimmt.
     */
    @FXML
    private void loeschen()
    {
            if (tTabelle.getSelectionModel().getSelectedItem() != null)
            {
                boolean b = pm.showDialog("Die ausgewaehlte Nachricht wird unwiderruflich geloescht!");
                if (b == true)
                {
                    try
                    {
                        GUIVS.instance.getControl().getC().deleteMessage(tTabelle.getSelectionModel().getSelectedItem());
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }else
            {
                pm.showError("Error", "Sie haben keine Nachricht ausgewaehlt!");
            }
    }



    /**
     * Wird ausgefuehrt, wenn der FXML-Controller geladen wird.
     * im Vergleich zu anderen Methoden etwas komplizierter, siehe lokale Kommentierung
     *
     * @author Jan-Merlin Geuskens, 3580970
     *
     * @param url default-Uebergabeparameter
     * @param rb default-Uebergabeparameter
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
        //muss die Message zuerst entpackt werden. Der Autorname wird via Callback an die TableColumn zurueckgeben
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


        //RowListener fuer die Tableview
        //Wenn auf eine Zeile doppelt geklickt wird, wird die ausgewaehlte Nachricht bearbeitet
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
                    //StringConverter fuer Anzeigetafel-ComboBox
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

                //Filter fuer die TableView
                //Ausdruck als Lambda mit innerem Lambda und integriertem Fehlerabfang, da der Filter implizit in einem
                //seperaten Thread laeuft und somit etwaige Exceptions nur von der Runtime gefangen werden koennen
                //--> Vermeidung von Exceptions bevor sie entstehen
                GUIVS.gruppenFilter.bind(Bindings.createObjectBinding(() ->
                        {
                            return (message -> (message != null && message.getGroup() != null && cbAnzeigetafel != null
                                    && cbAnzeigetafel.getValue() instanceof Group
                                    && cbAnzeigetafel.getValue() != null) && ((Group) cbAnzeigetafel.getValue()).getID() == message.getGroup().getID());

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

                //dreht die Sortierung fuer Zeitstempel um ( ASC --> DESC)
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
                        //Wenn sich die Liste aendert, sortiere die Tabelle erneut
                        //(Elemente werden auf Grund der Eigenschaften der ObservableList
                        //automatisch hinzugefuegt, jedoch am Ende der Tabelle eingefuegt)
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


