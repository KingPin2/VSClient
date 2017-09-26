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

/**
 * FXML Controller class
 *
 * @author Laura
 */
public class AdminAnsichtFXMLController implements Initializable
{
    private PopUpMessage pm;

    public ObservableList<Message> nachrichten;

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

    @FXML private void onBoardChange()
    {
        try {
            selectedGroup = cbAnzeigetafel.getSelectionModel().getSelectedItem().toString() != null?  GUIVS.instance.getControl().getC().getGroupByName( cbAnzeigetafel.getSelectionModel().getSelectedItem().toString()) : selectedGroup;
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (DatabaseObjectNotFoundException e) {
            e.printStackTrace();
        }
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
        try {
            GUIVS.neuerUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void neueNachricht() {
        try {
            GUIVS.neueNachricht();
        } catch (Exception e) {
        }
    }

    @FXML
    private void bearbeiteNachricht() {
        try {
            Message m = ObjectFactory.createMessage("Test, die Welt ist schön, es gibt ja threads", ObjectFactory.createUser("Merlin", "blubb", 2));
            //GUIVS.bearbeiteNachricht((Message) tTabelle.getSelectionModel().getSelectedItem());
            GUIVS.bearbeiteNachricht(m);
        } catch (Exception e) {
        }
    }

    @FXML
    private void schliessen() {
        Stage stage = (Stage) tTabelle.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loeschen() {
        if (tcNachrichten.getColumns().isEmpty())
        {
            pm.showInformation("Meldung", "Die Tabelle ist leer");
        }
        else
        {
            boolean b = pm.showDialog("Die ausgewählte Nachricht wird unwiderruflich gelöscht!");
            if (b == true) {
                if (!tTabelle.getSelectionModel().getSelectedItem().equals(null)) {
                    try {
                        GUIVS.instance.getControl().getC().deleteMessage(tTabelle.getSelectionModel().getSelectedItem());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

        /**
         * Initializes the controller class.
         */
        @Override
        public void initialize (URL url, ResourceBundle rb)
        {
            // TODO
            pm = new PopUpMessage();
            nachrichten = FXCollections.observableArrayList() ;
            try {
                for(Message m: GUIVS.instance.getControl().getC().getMessages())
                {
                    nachrichten.add(m);

                }
            } catch (DatabaseConnectionException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (DatabaseObjectNotFoundException e) {
                e.printStackTrace();
            }


            //tcUser.setCellValueFactory(new PropertyValueFactory<User, String>("author");
            tcUser.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>()
            {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> param) {
//                    int dritteshochkommata = 0;
//                    int vierteshochkommata = 0;
//                    dritteshochkommata = param.getValue().toString().
//
//                    String username = param.getValue().toString().
                    String username = param.getValue().getAuthor().getName();
                    SimpleStringProperty name = new SimpleStringProperty(username);
                    return name;
                }
            });



            tcNachrichten.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));
            tTabelle.setItems(nachrichten);


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        ArrayList<Group> g ;
                        if(GUIVS.instance.getMe().getLevel()== 2)
                        {
                            g = GUIVS.instance.getControl().getC().getGroups();
                        }
                        else
                        {
                            g = GUIVS.instance.getControl().getC().getGroupsByUser(GUIVS.instance.getMe());
                        }
                        if(g != null){
                            for(Group group: g)
                            {
                                cbAnzeigetafel.getItems().add(group.getName());
                            }
                        }
                        cbAnzeigetafel.getSelectionModel().selectFirst();
                        selectedGroup = GUIVS.instance.getControl().getC().getGroupByName(cbAnzeigetafel.getSelectionModel().getSelectedItem().toString());
                    } catch (DatabaseConnectionException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (DatabaseObjectNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            // menubar.getItems().add(new IconButtonFXMLController(new Image("pt_logo_x24.png")));
// Message m = ObjectFactory.createMessage("Test", ObjectFactory.createUser("Merlin", "blubb", 2));
//            try {
//                //   GUIVS.bearbeiteNachricht(m);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//Wenn ich Moderator bin, dann
//        if (GUIVS.instance.getMe().getLevel() == 1 && GUIVS.instance.isMod()) {
//            //schalte Adminfunktionen ab
//            bAnzeigetafelAnlegen.setDisable(true);
//            bAnzeigetafelBearbeiten.setDisable(true);
//            bUserAnlegen.setDisable(true);
//            bUserBearbeiten.setDisable(true);
//            bGruppeAnlegen.setDisable(true);
//            miNeueAnzeigetafel.setDisable(true);
//            miAnzeigetafelBearbeiten.setDisable(true);
//            miNeuerUser.setDisable(true);
//            miUserBearbeiten.setDisable(true);
//            miNeueGruppe.setDisable(true);
//
//            //und
//            try {
//                int i = 0;
//                //Für alle Gruppen
//                for (Group g : GUIVS.instance.getControl().getC().getGroups()) {
//                    //Überprüfe, ob ich Member der Gruppe bin
//                    if (g.equals(GUIVS.instance.getControl().getC().getGroupsByUser(GUIVS.instance.getMe()))) {
//                        //Wenn ja, füge ein MenuItem mit dem Gruppennamen hinzu
//                        mAnzeigetafel.getItems().add(new MenuItem(g.getName()));
//
//                        //cbAnzeigetafel.getItems().add(new )
//                        //und registriere einen Eventhandler für dieses Item
//                        mAnzeigetafel.getItems().get(i).setOnAction(new EventHandler<ActionEvent>() {
//
//                            //,der eine Anzeigetafel für diese Gruppe instanziiert
//                            @Override
//                            public void handle(ActionEvent event) {
//                                try {
//                                    Group g = GUIVS.instance.getControl().getC().getGroupByName(((MenuItem) (event.getSource())).getText());
//                                    GUIVS.oeffneAnzeigetafel(g, GUIVS.instance.getControl().getC().getMessagesByGroup(g));
//                                } catch (Exception e) {
//                                    pm.showError("Exception", e.toString());
//                                }
//                            }
//                        });
//                        //Für Index bei getItems().get(i)
//                        i++;
//                    }
//                }
//            } catch (Exception e) {
//                pm.showError("Exception", e.toString());
//            }
//        }
//        else //ansonsten bin ich Admin
//        {
//            try {
//                int i = 0;
//                //Für alle Gruppen
//                for (Group g : GUIVS.instance.getControl().getC().getGroups()) {
//                    //füge ein MenuItem mit dem Gruppennamen hinzu
//                    mAnzeigetafel.getItems().add(new MenuItem(g.getName()));
//                    //und registriere einen Eventhandler für dieses Item
//                    mAnzeigetafel.getItems().get(i).setOnAction(new EventHandler<ActionEvent>() {
//
//                        //,der eine Anzeigetafel für diese Gruppe instanziiert
//                        @Override
//                        public void handle(ActionEvent event) {
//                            try {
//                                Group g = GUIVS.instance.getControl().getC().getGroupByName(((MenuItem) (event.getSource())).getText());
//                                GUIVS.oeffneAnzeigetafel(g, GUIVS.instance.getControl().getC().getMessagesByGroup(g));
//                            } catch (Exception e) {
//                                pm.showError("Exception", e.toString());
//                            }
//                        }
//                    });
//                    //Für Index bei getItems().get(i)
//                    i++;
//                }
//            } catch (Exception e) {
//                pm.showError("Exception", e.toString());
//            }
//        }
//            Thread t = new Thread(
//                    () -> {
//                        try {
//                            while(true)
//                            {
//                                 Thread.sleep(2000);
//                                 try {
//                                     System.out.println("test");
//                                    nachrichten.add(ObjectFactory.createGroupMessage("Hallo Welt 42", GUIVS.instance.getMe(), GUIVS.instance.getControl().getC().getGroupByName("Broadcast")));
//                                     } catch (DatabaseConnectionException e1) {
//                                      e1.printStackTrace();
//                                 } catch (RemoteException e1) {
//                                     e1.printStackTrace();
//                                     } catch (DatabaseObjectNotFoundException e1) {
//                                     e1.printStackTrace();
//                                    }
//                                catch (InterruptedException e1){
//                                e1.printStackTrace();
//                            }}
//                        }
//
//                    }
//            );
//            t.start();
        }
}

