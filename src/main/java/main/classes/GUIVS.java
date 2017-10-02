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
 * stellt static Methoden zum Oeffnen der GUI-Fenster bereit
 * sowie die Referenz auf sich selbst, um auf via Control-Instanz auf RMI-Methoden zuzugreifen
 */
public class GUIVS extends Application
{

    //Referenz auf sich selbst
    public static GUIVS instance;
    private Control control;

    //Filter fuer Tableview (wird bei User und Admin benoetigt)
    public static ObjectProperty<Predicate<Message>> gruppenFilter = new SimpleObjectProperty<>();
    //Hashmap, die Gruppennamen mit Nachrichtenlisten(der jeweiligen Gruppe) verknuepft
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

    //Referenz auf das zuletzt geoeffnete Fenster
    private static Stage previousStage;

    /**
     * Dient dazu, bei Formularen das Elternfenster festzulegen
     //und Owner sowie Modality festzulegen
     * @param stage stage
     */
    public static void setPreviousStage(Stage stage)
    {
        previousStage = stage;
    }

    /**
     * Setzt das Icon oben links fuer eine uebergebene Stage
     * @param stage stage
     */
    public static void setIcon(Stage stage)
    {
        stage.getIcons().add(new Image("pt_logo_x24.png"));
    }


    /**
     * stellt FXML-Loader zur Verfuegung
     *
     * @param path Pfad zum FXML File, das geladen werden soll
     * @return Referenz auf FXMLLoaderobjekt, welches die .FXML laed
     * @throws Exception
     */
    private static Parent loadFXML(String path) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource(path));
        return loader.load();
    }

    private static void formularSettings(Stage vtStage, Scene vtScene)
    {
        setIcon(vtStage);
        vtStage.initOwner(previousStage);
        vtStage.initModality(Modality.WINDOW_MODAL);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    /**
     * Beim Schliessen der Hauptfenster wird die Verbindung zum Server getrennt
     * @param stage uebergebene Stage (Adminansicht, Useransicht und bei Anzeigetafellogin (zb. User Broadcast) auch bei der Anzeigetafel)
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

    /**
     * oeffnet Formular "neue Nachricht"
     * @throws Exception InvocationTargetException, NullpointerException wahrscheinlich
     */
    public static void neueNachricht() throws Exception
    {

        Scene vtScene = new Scene(loadFXML("/neueNachrichtFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("Neue Nachricht");
        formularSettings(vtStage,vtScene);
    }

    /**
     * Oeffnet eine Anzeigetafel-Instanz
     * @param g die Gruppe, deren Anzeigetafel geoeffnet werden soll
     * @throws Exception InvocationTargetException, NullpointerException moeglich
     */
    public static void oeffneAnzeigetafel(Group g) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/Anzeigetafel.fxml"));
        Parent root = loader.load();
        AnzeigetafelFXMLController ac = loader.getController();

        //Gruppenfilter auf Messages initalisieren
        FilteredList<Message> groupFilteredData = new FilteredList<>(GUIVS.instance.getControl().getMessages(), p -> true);
        groupFilteredData.setPredicate(new Predicate<Message>()
        {
            @Override
            public boolean test(Message message)
            {
                return message.getGroup().getID() == g.getID();

            }
        });
        //wrappen der filteredList in einer SortedList
        SortedList<Message> sortedData = new SortedList<Message>(groupFilteredData);
        //Sortierung via Lamba nach abfallendem Zeitstempel
        sortedData.setComparator((a, b) -> a.getTimestamp() < b.getTimestamp() ? -1 : a.getTimestamp() == b.getTimestamp() ? 0 : 1);

        //Injection der Daten in den FXMLController der Anzeigetafel
        ac.setM(sortedData);
        ac.setGroup(g);

        Scene vtScene = new Scene(root);
        Stage vtStage = new Stage();
        vtStage.setTitle("Anzeigetafel der Gruppe " + g.getName());
        //Wenn Level0-User, dann disconnect() beim Schliessen der Anzeigetafel
        if (GUIVS.instance.getMe().getLevel() == 0)
        {
            defaultClose(vtStage);
        }
        setIcon(vtStage);
        vtStage.setScene(vtScene);
        vtStage.showAndWait();
    }

    /**
     * Oeffnet das Formular zum Anlegen eines neuen Users
     * @throws Exception InvocationTargetException, NullpointerException moeglich
     */
    public static void neuerUser() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/userAnlegenFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("Neuer User");
        formularSettings(vtStage,vtScene);
    }
    /**
     * Oeffnet das Formular zum Verwalten bestehender User
     * @throws Exception InvocationTargetException, NullpointerException moeglich
     */
    public static void userVerwalten() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/userVerwaltenFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("User Verwalten");
        formularSettings(vtStage,vtScene);
    }

    /**
     * Oeffnet das Formular zum anlegen neuer Gruppen
     * @throws Exception InvocationTargetException, NullpointerException moeglich
     */
    public static void gruppeAnlegen() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/neueGruppeFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("Gruppe anlegen");
        formularSettings(vtStage,vtScene);
    }
    /**
     * Oeffnet das Formular zum Verwalten bestehender Gruppen
     * @throws Exception InvocationTargetException, NullpointerException moeglich
     */
    public static void gruppeVerwalten() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/gruppeVerwaltenFXML.fxml"));
        Stage vtStage = new Stage();
        vtStage.setTitle("Gruppen verwalten");
        formularSettings(vtStage,vtScene);
    }

    /**
     * Oeffnet das Formular zum bearbeiten einer Nachricht
     * @param m Nachricht, die bearbeitet werden soll (Auswahl via TableView)
     * @throws Exception InvocationTargetException, NullpointerException moeglich
     */
    public static void bearbeiteNachricht(Message m) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/bearbeitenFXML.fxml"));
        Parent p = loader.load();
        BearbeitenFXMLController mc = loader.getController();
        //Uebergabe des Messageobjekts an den FXMLController
        mc.setM(m);
        Scene vtScene = new Scene(p);
        Stage vtStage = new Stage();
        vtStage.setTitle("Nachricht bearbeiten");
        formularSettings(vtStage,vtScene);
    }

    /**
     * Oeffnet die Hauptansicht fuer Level-1 Benutzer
     * @throws Exception Exception
     */
    public static void userAnsicht() throws Exception
    {
        Scene vtScene = new Scene(loadFXML("/userAnsichtFXML.fxml"));
        Stage vtStage = new Stage();
        setPreviousStage(vtStage);
        vtStage.setTitle("Ansicht fuer User");
        defaultClose(vtStage);
        setIcon(vtStage);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    /**
     * Oeffnet die Hauptansicht fuer Level-2 Benutzer
     * @throws Exception Exception
     */
    public static void adminAnsicht() throws Exception
    {
        //fillgroups();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/adminAnsichtFXML.fxml"));
        Parent p = loader.load();

        Scene vtScene = new Scene(p);
        Stage vtStage = new Stage();
        setPreviousStage(vtStage);
        vtStage.setTitle("Ansicht fuer Administratoren");
        defaultClose(vtStage);
        setIcon(vtStage);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    /**
     * Oeffnet das Loginfenster
     * @param stage autogenerierte Start-stage, wird uebegeben, da der Code nicht direkt in start() ausgefuehrt wird
     */
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

    /**
     * die erste Methode die beim Starten des Programms aufgerufen wird
     * @param stage autogenerierte Stage
     * @throws Exception InvocationTargetException, NullpointerException moeglich
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        GUIVS.login(stage);
    }

    /**
     * startet das Programm via launch(), d.h. start() wird ausgefuehrt,
     * @param args Kommandozeilenparameter, werden (noch) ignoriert, spaetere Verwendung moeglich (TUI)
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
