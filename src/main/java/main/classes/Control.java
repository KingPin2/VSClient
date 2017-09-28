/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.exceptions.DatabaseConnectionException;
import main.database.exceptions.DatabaseObjectNotFoundException;
import main.exceptions.EmptyStringException;
import main.exceptions.IllegalCharacterException;
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
            e.printStackTrace();
        } catch (RemoteException e)
        {
            e.printStackTrace();
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
