/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.guivs;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import main.classes.GUIVS;
import main.classes.IconButtonFXMLController;
import main.classes.PopUpMessage;
import main.database.ObjectFactory;
import main.database.exceptions.DatabaseConnectionException;
import main.database.exceptions.DatabaseObjectNotFoundException;
import main.objects.Group;
import main.objects.Message;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import main.objects.User;
import main.rmiinterface.NotifyUpdate;

/**
 * FXML Controller class
 *
 * @author Laura
 */
public class AdminAnsichtFXMLController implements Initializable
{
    private PopUpMessage pm;

    private ObservableList<Message> nachrichten;
    private ObservableList<Group> groups;

    public static Message getSelectedMessage()
    {
        return selectedMessage;
    }

    private static Message selectedMessage;


    // private ArrayList<ChoiceBox <KeyValuePair > > cbEntries;
    //Immer sichtbar in Navigation
    @FXML
    private Button bNeueNachricht;
    @FXML
    private Button bBearbeiten;
    @FXML
    private Button bLoeschen;
    @FXML
    private ChoiceBox cbAnzeigetafel;
    @FXML
    private Button bAnzeigetafel;

    //Tabelle
    @FXML
    private TableView<Message> tTabelle;
    @FXML
    private TableColumn tcNachrichten;
    @FXML
    private TableColumn tcUser;
    @FXML
    private TableColumn tcGruppe;
    @FXML
    private TableColumn tcZeitstempel;

    //MenuBar

    //Datei...
    @FXML
    private MenuItem miAktualisieren;
    @FXML
    private Menu mAnzeigetafel;

    //Seperator...
    @FXML
    private MenuItem miAbmelden;
    @FXML
    private MenuItem miSchliessen;


    //Bearbeiten...
    @FXML
    private MenuItem miNeueNachricht;
    @FXML
    private MenuItem miBearbeiten;
    @FXML
    private MenuItem miLoeschen;

    //Seperator...
    @FXML
    private MenuItem miNeuerUser;
    @FXML
    private MenuItem miUserBearbeiten;

    //Seperator...
    @FXML
    private MenuItem miNeueGruppe;
    @FXML
    private MenuItem miGruppeBearbeiten;

    //Seperator...
    @FXML
    private MenuItem miNeueAnzeigetafel;
    @FXML
    private MenuItem miAnzeigetafelBearbeiten;


    //Hilfe...
    @FXML
    private MenuItem miAbout;

    @FXML
    private ToolBar menubar;

    //Acchordion
    //User...
    @FXML
    private Button bUserAnlegen;
    @FXML
    private Button bUserBearbeiten;

    //Gruppe...
    @FXML
    private Button bGruppeAnlegen;
    @FXML
    private Button bGruppeBearbeiten;

    //Anzeigetafel...
    @FXML
    private Button bAnzeigetafelAnlegen;
    @FXML
    private Button bAnzeigetafelBearbeiten;

    private Group selectedGroup;

    @FXML
    private void onBoardChange()
    {

            selectedGroup = cbAnzeigetafel.getSelectionModel().getSelectedItem() != null ? (Group) cbAnzeigetafel.getSelectionModel().getSelectedItem() : selectedGroup;

    }


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

    @FXML
    private void abmelden()
    {
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

    }

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

    @FXML
    private void neuerUser()
    {
        try
        {
            GUIVS.neuerUser();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

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

    @FXML
    private void bearbeiteNachricht()
    {
        try
        {
            selectedMessage= (Message) tTabelle.getSelectionModel().getSelectedItem();
            GUIVS.bearbeiteNachricht(selectedMessage);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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

        Stage stage = (Stage) tTabelle.getScene().getWindow();
        stage.close();
    }

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
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        pm = new PopUpMessage();
        nachrichten = GUIVS.instance.getControl().getMessages();
        groups = GUIVS.instance.getControl().getGroups();

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


        tcNachrichten.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));
        tTabelle.setItems(nachrichten);

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
                    selectedGroup = (Group )cbAnzeigetafel.getSelectionModel().getSelectedItem();
            }
        });


    }
}


