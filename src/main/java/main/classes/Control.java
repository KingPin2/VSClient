/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.exceptions.*;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiconnections.*;
import main.rmiinterface.NotifyUpdate;

import java.rmi.RemoteException;


/**
 *
 * @author Laura
 */
public class Control {
 
    private Client c;
    private NotifyUpdate callback;

    public Client getC() {
        return c;
    }

    public void setC(Client c)
    {
        this.c = c;
    }

    private ObservableList<Group> groups;
    private ObservableList<User> users;
    private ObservableList<Message> messages;

    public ObservableList<Message> getMyMessages()
    {
        return myMessages;
    }

    private ObservableList<Message> myMessages;

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


    public void getData()
    {
        if(GUIVS.instance.getMe().getLevel() == 2)
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
                e.printStackTrace();
            } catch (RemoteException e)
            {
                e.printStackTrace();
            } catch (UserAuthException e)
            {
                e.printStackTrace();
            }
        }if(GUIVS.instance.getMe().getLevel() == 1)
    {
        groups = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();
        messages = FXCollections.observableArrayList();
        myMessages = FXCollections.observableArrayList();
        try
        {
            for (Group g : c.getGroupsByUser(GUIVS.instance.getMe()))
            {
                groups.add(g);
            }
            boolean found = false;
            for(Group g:groups)
            {
                try
                {
                    for (Message m : c.getMessagesByGroup(g))
                    {
                        messages.add(m);
                    }
                    found = true;
                }catch(DatabaseObjectNotFoundException de)
                {

                }
            }
            if(!found)
            {
                PopUpMessage pm = new PopUpMessage();
                pm.showInformation("Information", "Sie haben noch keine Nachricht veröffentlicht!");
            }
        } catch (DatabaseConnectionException e)
        {
            e.printStackTrace();
        }  catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (UserAuthException e)
        {
            e.printStackTrace();
        } catch (DatabaseObjectNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    }


    
    public Control()
    {
        try {
            c = new Client("localhost");
        } catch (RemoteException rm)
        {
            rm.printStackTrace();
        }
    }
}
