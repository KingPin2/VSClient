/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.classes;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.anzeigetafel.AnzeigetafelFXMLController;
import main.formulare.BearbeitenFXMLController;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.function.Predicate;

import static java.lang.System.exit;

/**
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 *
 * Hauptverwaltungsklasse,
 * stellt static Methoden zum Öffnen der GUI-Fenster bereit
 * sowie die Referenz auf sich selbst, um auf via Control-Instanz auf RMI-Methoden zuzugreifen
 */
public class GUIVS extends Application
{

    //Referenz auf sich selbst
    public static GUIVS instance;
    private Control control;

    //Filter für Tableview (wird bei User und Admin benötigt)
    public static ObjectProperty<Predicate<Message>> gruppenFilter = new SimpleObjectProperty<>();
    //Hashmap, die Gruppennamen mit Nachrichtenlisten(der jeweiligen Gruppe) verknüpft
    private static HashMap<String, ObservableList<Message>> group_messages = new HashMap<String, ObservableList<Message>>();

    public static HashMap<String, ObservableList<Message>> getGroup_messages()
    {
        return group_messages;
    }
    public Control getControl()
    {
        return control;
    }
    public GUIVS()
    {
        instance = this;
        control = new Control();
    }

    //eigenes User-Objekt wird lokal gespeichert
    private User me = null;

    /**
     *
     * @param me das eigene Userobjekt
     */
    public void setMe(User me)
    {
        this.me = me;
    }
    public User getMe()
    {
        return this.me;
    }

    //Referenz auf das zuletzt geöffnete Fenster
    private static Stage previousStage;

    /**
     * Dient dazu, bei Formularen das Elternfenster festzulegen
     //und Owner sowie Modality festzulegen
     * @param stage
     */
    public static void setPreviousStage(Stage stage)
    {
        instance.previousStage = stage;
    }

    /**
     * Setzt das Icon oben links für eine übergebene Stage
     * @param stage
     */
    public static void setIcon(Stage stage)
    {
        stage.getIcons().add(new Image("pt_logo_x24.png"));
    }


    /**
     * stellt FXML-Loader zur Verfügung
     *
     * @param path Pfad zum FXML File, das geladen werden soll
     * @return Referenz auf FXMLLoaderobjekt, welches die .FXML läd
     * @throws Exception
     */
    private static Parent loadFXML(String path) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource(path));
        return loader.load();
    }

    /**
     * Beim Schließen der Hauptfenster wird die Verbindung zum Server getrennt
     * @param stage übergebene Stage (Adminansicht, Useransicht und bei Anzeigetafellogin (zb. User Broadcast) auch bei der Anzeigetafel)
     */
    public static void defaultClose(Stage stage)
    {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent t)
            {
                try
                {
                    GUIVS.instance.getControl().getC().disconnect();
                } catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                Platform.exit();
                exit(0);
            }
        });
    }

    public static void neueNachricht() throws Exception
    {

        Scene vtScene = new Scene(loadFXML("/neueNachrichtFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("Neue Nachricht");
        setIcon(vtStage);
        vtStage.initOwner(previousStage);
        vtStage.initModality(Modality.WINDOW_MODAL);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    public static void oeffneAnzeigetafel(Group g) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/Anzeigetafel.fxml"));
        Parent root = loader.load();
        AnzeigetafelFXMLController ac = loader.<AnzeigetafelFXMLController>getController();

        //Gruppenfilter auf Messages initalisieren
        FilteredList<Message> groupFilteredData = new FilteredList<>(GUIVS.instance.getControl().getMessages(), p -> true);
        groupFilteredData.setPredicate(new Predicate<Message>()
        {
            @Override
            public boolean test(Message message)
            {
                if (message.getGroup().getID() == g.getID())
                {
                    return true;
                } else
                {
                    return false;
                }

            }
        });
        SortedList<Message> sortedData = new SortedList<Message>(groupFilteredData);
        sortedData.setComparator((a, b) -> a.getTimestamp() < b.getTimestamp() ? -1 : a.getTimestamp() == b.getTimestamp() ? 0 : 1);
        ac.setM(sortedData);
        ac.setGroup(g);
        Scene vtScene = new Scene(root);
        Stage vtStage = new Stage();
        vtStage.setTitle("Anzeigetafel der Gruppe " + g.getName());
        if (GUIVS.instance.getMe().getLevel() == 0)
        {
            defaultClose(vtStage);
        }
        setIcon(vtStage);
        vtStage.setScene(vtScene);
        vtStage.showAndWait();
    }

    public static void neuerUser() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/userAnlegenFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("Neuer User");
        setIcon(vtStage);
        vtStage.initOwner(previousStage);
        vtStage.initModality(Modality.WINDOW_MODAL);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    public static void userVerwalten() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/userVerwaltenFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("User Verwalten");
        setIcon(vtStage);
        vtStage.initOwner(previousStage);
        vtStage.initModality(Modality.WINDOW_MODAL);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    public static void gruppeAnlegen() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/neueGruppeFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("Gruppe anlegen");
        setIcon(vtStage);
        vtStage.initOwner(previousStage);
        vtStage.initModality(Modality.WINDOW_MODAL);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    public static void gruppeVerwalten() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/gruppeVerwaltenFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("Gruppen verwalten");
        setIcon(vtStage);
        vtStage.initOwner(previousStage);
        vtStage.initModality(Modality.WINDOW_MODAL);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    public static void bearbeiteNachricht(Message m) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/bearbeitenFXML.fxml"));
        Parent p = loader.load();
        BearbeitenFXMLController mc = loader.<BearbeitenFXMLController>getController();
        mc.setM(m);
        Scene vtScene = new Scene(p);
        Stage vtStage = new Stage();
        vtStage.setTitle("Nachricht bearbeiten");
        setIcon(vtStage);
        vtStage.initOwner(previousStage);
        vtStage.initModality(Modality.WINDOW_MODAL);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }


    public static void userAnsicht() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/userAnsichtFXML.fxml"));
        Stage vtStage = new Stage();
        setPreviousStage(vtStage);
        vtStage.setTitle("Ansicht für User");
        defaultClose(vtStage);
        setIcon(vtStage);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    public static void adminAnsicht() throws Exception
    {
        //fillgroups();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/adminAnsichtFXML.fxml"));
        Parent p = loader.load();

        Scene vtScene = new Scene(p);
        Stage vtStage = new Stage();
        setPreviousStage(vtStage);
        vtStage.setTitle("Ansicht für Administratoren");
        defaultClose(vtStage);
        setIcon(vtStage);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }


    public static void login(Stage stage)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/loginFXML.fxml"));
        Parent root = null;
        try
        {
            root = loader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        stage.setTitle("Login");
        setIcon(stage);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    @Override
    public void start(Stage stage) throws Exception
    {
        GUIVS.login(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
