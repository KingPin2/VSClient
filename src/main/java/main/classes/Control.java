/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.exceptions.DatabaseConnectionException;
import main.exceptions.DatabaseObjectNotFoundException;
import main.exceptions.UserAuthException;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiconnections.Client;
import main.rmiinterface.NotifyUpdate;

import java.rmi.RemoteException;


/**
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 */
public class Control
{
    private Client c;
    private ObservableList<Group> groups;
    private ObservableList<User> users;
    private ObservableList<Message> messages;
    private NotifyUpdate callback;

    public Client getC()
    {
        return c;
    }

    public void setC(Client c)
    {
        this.c = c;
    }

    public ObservableList<Group> getGroups()
    {
        return groups;
    }

    public ObservableList<User> getUsers()
    {
        return users;
    }

    public ObservableList<Message> getMessages()
    {
        return messages;
    }


    /**
     * @author Jan-Merlin Geuskens, 3580970
     * Initialen Datensatz vom Server laden
     */
    public void getData()
    {
        if (GUIVS.instance.getMe().getLevel() == 2)
        {
            groups = FXCollections.observableArrayList();
            users = FXCollections.observableArrayList();
            messages = FXCollections.observableArrayList();
            try
            {
                for (Group g : c.getGroups())
                {
                    groups.add(g);
                }
                for (User u : c.getUsers())
                {
                    users.add(u);
                }
                for (Message m : c.getMessages())
                {
                    messages.add(m);
                }
            } catch (DatabaseConnectionException e)
            {
                e.printStackTrace();
            } catch (DatabaseObjectNotFoundException e)
            {
                PopUpMessage pm = new PopUpMessage();
                pm.showInformation("Information", "Sie haben noch keine Nachricht veröffentlicht!");
            } catch (RemoteException e)
            {

            } catch (UserAuthException e)
            {
                e.printStackTrace();
            }
        } else if (GUIVS.instance.getMe().getLevel() == 1)
        {
            groups = FXCollections.observableArrayList();
            users = FXCollections.observableArrayList();
            messages = FXCollections.observableArrayList();
            try
            {
                for (Group g : c.getGroupsByUser(GUIVS.instance.getMe()))
                {
                    groups.add(g);
                }
                boolean found = false;
                for (Group g : groups)
                {
                    try
                    {
                        for (Message m : c.getMessagesByGroup(g))
                        {
                            messages.add(m);
                        }
                        found = true;
                    } catch (DatabaseObjectNotFoundException de)
                    {

                    }
                }
                if (!found)
                {
                    PopUpMessage pm = new PopUpMessage();
                    pm.showInformation("Information", "Sie haben noch keine Nachricht veröffentlicht!");
                }
            } catch (DatabaseConnectionException e)
            {
                e.printStackTrace();
            } catch (RemoteException e)
            {
                e.printStackTrace();
            } catch (UserAuthException e)
            {
                e.printStackTrace();
            } catch (DatabaseObjectNotFoundException e)
            {
                e.printStackTrace();
            }
        } else if (GUIVS.instance.getMe().getLevel() == 0)
        {
            groups = FXCollections.observableArrayList();
            users = FXCollections.observableArrayList();
            messages = FXCollections.observableArrayList();

            try
            {
                groups.addAll(GUIVS.instance.getControl().getC().getGroupByName(GUIVS.instance.getMe().getName()));
                messages.addAll(GUIVS.instance.getControl().getC().getMessagesByGroup(GUIVS.instance.getControl().getC().getGroupByName(GUIVS.instance.getMe().getName())));
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
    }


    /**
     * @author Jan-Merlin Geuskens, 3580970
     * hält ein Client Objekt und stellt Schnittstelle zum server zur Verfügung
     */
    public Control()
    {
        try
        {
            c = new Client("localhost");
        } catch (RemoteException rm)
        {
            rm.printStackTrace();
        }
    }
}
