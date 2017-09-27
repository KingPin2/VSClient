/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.classes;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.database.exceptions.DatabaseConnectionException;
import main.database.exceptions.DatabaseObjectNotFoundException;
import main.objects.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import javafx.stage.Modality;
import main.anzeigetafel.*;
import main.formulare.BearbeitenFXMLController;

/**
 *
 * @author Laura
 */
public class GUIVS extends Application {
    
    public static GUIVS instance;
    private Control control;

    public static HashMap<String, ObservableList<Message>> getGroup_messages()
    {
        return group_messages;
    }

    private static HashMap<String, ObservableList<Message>> group_messages = new HashMap<String, ObservableList<Message>>();
    public Control getControl() {
        return control;
    }
    public GUIVS()
    {
            instance = this;
            control = new Control();
            
    }
    private User me = null;
    private boolean isMod = false;

    public boolean isMod() {
        return isMod;
    }

    public void setIsMod(boolean isMod) {
        this.isMod = isMod;
    }
    
    public void setMe(User me)
    {
        this.me = me;
    }
    public User getMe()
    {
        return this.me;
    }
    
    private static Stage previousStage;
    public static void setPreviousStage(Stage stage)
    {
        instance.previousStage = stage;
    }
    
    public static Stage getPreviousStage()
    {
        return instance.previousStage;
    }

    public static void setIcon(Stage stage)
    {
        stage.getIcons().add(new Image("pt_logo_x24.png"));
    }

    public static void neueNachricht() throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/neueNachrichtFXML.fxml"));
        Parent p = loader.load();

        Scene vtScene = new Scene(p);
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
     //   ObservableList<Message> messages = group_messages.get(g.getName());
        ac.setM(GUIVS.group_messages.get(g.getName()));
//        ac.setM(GUIVS.instance.getControl().getMessages().filtered(new Predicate<Message>()
//        {
//            @Override
//            public boolean test(Message message)
//            {
//                if(message.getGroup().getName().equals(g.getName()))
//                {
//                    return true;
//                }else
//                {return false;}
//            }
//        }));
        ac.setGroup(g);

        Scene vtScene = new Scene(root);
        Stage vtStage = new Stage();
        vtStage.setTitle("Anzeigetafel der Gruppe " + g.getName());
        setIcon(vtStage);
        vtStage.setScene(vtScene);
        vtStage.showAndWait();
    }

    public static void neuerUser() throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/userAnlegenFXML.fxml"));
        Parent p = loader.load();

        Scene vtScene = new Scene(p);
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/userVerwaltenFXML.fxml"));
        Parent p = loader.load();

        Scene vtScene = new Scene(p);
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/neueGruppeFXML.fxml"));
        Parent p = loader.load();

        Scene vtScene = new Scene(p);
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/gruppeVerwaltenFXML.fxml"));
        Parent p = loader.load();

        Scene vtScene = new Scene(p);
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
        fillgroups();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/userAnsichtFXML.fxml"));
        Parent p = loader.load();
        
        Scene vtScene = new Scene(p);
        Stage vtStage = new Stage();
        setPreviousStage(vtStage);
        vtStage.setTitle("Ansicht für User");
        setIcon(vtStage);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

        public static void adminAnsicht() throws Exception
    {
        fillgroups();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/adminAnsichtFXML.fxml"));
        Parent p = loader.load();
        
        Scene vtScene = new Scene(p);
        Stage vtStage = new Stage();
        setPreviousStage(vtStage);
        vtStage.setTitle("Ansicht für Administratoren");
        setIcon(vtStage);
        vtStage.setScene(vtScene);
        vtStage.setResizable(false);
        vtStage.showAndWait();
    }

    private static void fillgroups()
    {
        ArrayList<Group> groups;

        if(GUIVS.instance.getMe().getLevel() == 1) {
            try {
                groups = GUIVS.instance.getControl().getC().getGroupsByUser(GUIVS.instance.getMe());
                if(groups != null) {
                    for (Group g : groups) {
                        group_messages.put(g.getName(), FXCollections.observableArrayList());
                    }
                }

            } catch (DatabaseConnectionException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (DatabaseObjectNotFoundException e) {
                e.printStackTrace();
            }

        }
        else if( GUIVS.instance.getMe().getLevel() == 2)
        {
            try
            {
                groups = GUIVS.instance.getControl().getC().getGroups();
                if (groups != null)
                {
                    for (Group g : groups)
                    {
                        group_messages.put(g.getName(), FXCollections.observableArrayList());

                        if (GUIVS.instance.getControl().getC().getMessagesByGroup(g) != null)
                        {
                            group_messages.get(g.getName()).addAll(GUIVS.instance.getControl().getC().getMessagesByGroup(g));
                        }//TODO warum springt der hier raus und durchläuft schleife nicht erneut?
                    }
                }

            }
            catch (DatabaseConnectionException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (DatabaseObjectNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }
    /*
        public static void oeffneAnzeigetafel(Map.Entry<Group, ObservableList<Message>> me) throws Exception
        {
            me.getValue().addAll(GUIVS.instance.getControl().getC().getMessagesByGroup(me.getKey()));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIVS.class.getResource("/Anzeigetafel.fxml"));
        
        Parent p = loader.load();
        
        //Übergeben von ArrayList<Messages> an Anzeigetafel
        AnzeigetafelFXMLController mc = loader.<AnzeigetafelFXMLController>getController();
        mc.setM(me.getValue());
        

        Scene vtScene = new Scene(p);
        Stage vtStage = new Stage();
        
        vtStage.setTitle("Anzeigetafel: " + g.getName());
        setIcon(vtStage);
        
        vtStage.setScene(vtScene);
        vtStage.showAndWait();
        }
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

    @Override
    public void start(Stage stage) throws Exception {

        GUIVS.login(stage);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
