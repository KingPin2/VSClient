/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.guivs;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import main.classes.GUIVS;
import main.classes.IconButtonFXMLController;
import main.classes.PopUpMessage;
import main.database.ObjectFactory;
import main.exceptions.DatabaseConnectionException;
import main.exceptions.DatabaseObjectNotFoundException;
import main.exceptions.UserAuthException;
import main.objects.Group;
import main.objects.Message;

import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

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
public class UserAnsichtFXMLController implements Initializable
{
    private PopUpMessage pm;

    private ObservableList<Message> nachrichten;
    private ObservableList<Group> groups;

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
    private TableColumn tcZeitstempel;



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

        pm = new PopUpMessage();
        try
        {
            nachrichten = FXCollections.observableArrayList( GUIVS.instance.getControl().getC().getMessagesByUser(GUIVS.instance.getMe()));
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
        groups = GUIVS.instance.getControl().getGroups();




        tcNachrichten.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));
        tcZeitstempel.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> message) {
                        SimpleStringProperty property = new SimpleStringProperty();
                        DateFormat dateFormat = new SimpleDateFormat("<dd.MM> HH:mm");
                        property.setValue(dateFormat.format(message.getValue().getTimestamp()));
                        return property;
                    }
                });


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
                ObjectProperty<Predicate<Message>> gruppenFilter = new SimpleObjectProperty<>();
                gruppenFilter.bind(Bindings.createObjectBinding(() ->
                                message -> ((Group) cbAnzeigetafel.getValue()).getName().equals(message.getGroup().getName()),
                        cbAnzeigetafel.valueProperty()));
                FilteredList<Message> gefilterteNachrichten = new FilteredList<Message>(nachrichten, p -> true);

                gefilterteNachrichten.predicateProperty().bind(Bindings.createObjectBinding(
                        () -> gruppenFilter.get(), gruppenFilter));

                SortedList<Message> sortierteNachrichten = new SortedList<Message>(gefilterteNachrichten);
                tTabelle.setItems(sortierteNachrichten);

                sortierteNachrichten.comparatorProperty().bind(tTabelle.comparatorProperty());

                tcZeitstempel.setComparator(tcZeitstempel.getComparator().reversed());
                tTabelle.getSortOrder().add(tcZeitstempel);

                gefilterteNachrichten.addListener(new ListChangeListener<Message>()
                {
                    @Override
                    public void onChanged(Change<? extends Message> c)
                    {
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


