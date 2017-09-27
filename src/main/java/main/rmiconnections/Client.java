package main.rmiconnections;

/**
 * Merlin, 16.08.2017
 */

import main.classes.GUIVS;
import main.database.exceptions.DatabaseConnectionException;
import main.database.exceptions.DatabaseObjectNotDeletedException;
import main.database.exceptions.DatabaseObjectNotFoundException;
import main.database.exceptions.DatabaseObjectNotSavedException;
import main.objects.Board;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import main.rmiinterface.*;

public class Client extends UnicastRemoteObject implements NotifyUpdate{

    private Registry registry;
    private Functions rmi;

    public String getClientID()
    {
        return clientID;
    }

    private String clientID = "NONE";

    
    public User getUserById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getUserById(id);
    }

    public User getUserByName(String username) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getUserByName(username);
    }

    public ArrayList<User> getUsers() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getUsers();
    }

    public ArrayList<User> getUsersByLevel(int level) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getUsersByLevel(level);
    }

    public void saveUser(User user) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException {
        this.rmi.saveUser(user);
    }

    public Board getBoardById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getBoardById(id);
    }

    public ArrayList<Board> getBoards() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getBoards();
    }

    public void saveBoard(Board board) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException {
        this.rmi.saveBoard(board);
    }

    public Group getGroupById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getGroupById(id);
    }
    public ArrayList<Group> getGroups() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getGroups();
    }

    public ArrayList<Group> getGroupsByUser(User u) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getGroupsByUser(u);
    }

    public ArrayList<Group> getGroupsByModerator(User u)
    {
        return null;
    }

    public void saveGroup(Group group) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException {
        this.rmi.saveGroup(group);
    }

    public ArrayList<User> getUsersNotInGroup(Group group) throws RemoteException {
        return this.rmi.getUsersNotInGroup(group);
    }

    public Message getMessageById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getMessageById(id);
    }

    public ArrayList<Message> getMessagesByUser(User u) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getMessagesByUser(u);
    }

    public ArrayList<Message> getMessages() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getMessages();
    }

    public ArrayList<Message> getMessagesByGroup(Group g) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getMessagesByGroup(g);
    }

    public void saveMessage(Message message) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException {
        this.rmi.saveMessage(message);
    }

    public User loginUser(String username, String password) throws RemoteException {
        return this.rmi.loginUser(username, password);
    }
    public String test(int testID) throws RemoteException {
        return this.rmi.test(42);
    }

    public Group getGroupByName(String name) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException {
        return this.rmi.getGroupByName(name);
    }

    public void deleteMessage(Message m) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
         this.rmi.deleteMessage(m);
    }

    public void deleteUser(User u) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
        this.rmi.deleteUser(u);
    }

    public void deleteBoard(Board b) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
        this.rmi.deleteBoard(b);
    }

    public void deleteGroup(Group g) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException {
        this.rmi.deleteGroup(g);
    }

    public void disconnect() throws RemoteException {
        this.rmi.disconnect(clientID);
    }



    public Client(String host) throws RemoteException {
        super();
        try {
            /*
            if(System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }*/
            this.registry = LocateRegistry.getRegistry(host);
            this.rmi = (Functions) registry.lookup("Functions");
            clientID = rmi.connect(this);
        }
        catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
}

    @Override
    public void onUpdateGroup(Group g, UpdateType type) throws RemoteException {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                GUIVS.instance.getControl().getGroups().clear();
                try
                {
                    for(Group g: rmi.getGroups())
                    {
                        GUIVS.instance.getControl().getGroups().add(g);
                    }
                } catch (DatabaseObjectNotFoundException e)
                {
                    e.printStackTrace();
                } catch (DatabaseConnectionException e)
                {
                    e.printStackTrace();
                } catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    @Override
    public void onUpdateUser(User u, UpdateType type) throws RemoteException
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                GUIVS.instance.getControl().getUsers().clear();
                try
                {
                    try
                    {
                        for (User u : rmi.getUsers())
                        {
                            GUIVS.instance.getControl().getUsers().add(u);
                        }
                    } catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                } catch (DatabaseObjectNotFoundException e)
                {
                    e.printStackTrace();
                } catch (DatabaseConnectionException e)
                {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }




    @Override
    public void onUpdateMessage(Message m, UpdateType type) throws RemoteException
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println(m + type.toString());
                GUIVS.instance.getControl().getMessages().clear();
                try
                {
                    ArrayList<Message> temp = rmi.getMessages();
                    for (Message m : temp)
                    {
                        GUIVS.instance.getControl().getMessages().add(m);
                    }
                } catch (DatabaseObjectNotFoundException e)
                {
                    e.printStackTrace();
                } catch (DatabaseConnectionException e)
                {
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
