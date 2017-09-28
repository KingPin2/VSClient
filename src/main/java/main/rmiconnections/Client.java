package main.rmiconnections;

/**
 * Merlin, 16.08.2017
 */

import javafx.collections.FXCollections;
import main.classes.GUIVS;
import main.exceptions.*;
import main.exceptions.NoItemSelectedException;
import main.guivs.AdminAnsichtFXMLController;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import main.rmiinterface.*;

public class Client extends UnicastRemoteObject implements NotifyUpdate{

    private Registry registry;
    private Functions rmi;

    public String getClientID()
    {
        return clientID;
    }

    private String clientID = "NONE";

    
    public User getUserById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getUserById(clientID,id);
    }

    public User getUserByName(String username) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException , UserAuthException{
        return this.rmi.getUserByName(clientID,username);
    }

    public ArrayList<User> getUsers() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getUsers(clientID);
    }

    public ArrayList<User> getUsersByLevel(int level) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getUsersByLevel(clientID,level);
    }

    public void saveUser(User user) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException, UserAuthException {
        this.rmi.saveUser(clientID,user);
    }

    public Group getGroupById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getGroupById(clientID,id);
    }
    public ArrayList<Group> getGroups() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getGroups(clientID);
    }

    public ArrayList<Group> getGroupsByUser(User u) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getGroupsByUser(clientID,u);
    }

    public ArrayList<Group> getGroupsByModerator(User u)
    {
        return null;
    }

    public void saveGroup(Group group) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException, UserAuthException {
        this.rmi.saveGroup(clientID,group);
    }

    public ArrayList<User> getUsersNotInGroup(Group group) throws RemoteException, UserAuthException {
        return this.rmi.getUsersNotInGroup(clientID,group);
    }

    public Message getMessageById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getMessageById(clientID,id, rmi);
    }

    public ArrayList<Message> getMessagesByUser(User u) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getMessagesByUser(clientID,u, rmi);
    }

    public ArrayList<Message> getMessages() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getMessages(clientID,rmi);
    }

    public ArrayList<Message> getMessagesByGroup(Group g) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getMessagesByGroup(clientID,g,rmi);
    }

    public void saveMessage(Message message) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException, UserAuthException {
        this.rmi.saveMessage(clientID,message,rmi);
    }

    public User loginUser(String username, String password) throws RemoteException {
        return this.rmi.login(clientID,username, password);
    }
    public String test(int testID) throws RemoteException {
        return this.rmi.test(42);
    }

    public Group getGroupByName(String name) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException {
        return this.rmi.getGroupByName(clientID,name);
    }

    public void deleteMessage(Message m) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException {
         this.rmi.deleteMessage(clientID,m);
    }

    public void deleteUser(User u) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, DatabaseUserIsModException, UserAuthException {
        this.rmi.deleteUser(clientID,u, rmi);
    }

    public void deleteGroup(Group g) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException {
        this.rmi.deleteGroup(clientID,g, rmi);
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
//                try
//                {
//                    Thread.sleep(200);
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
                    switch (type)
                    {
                        case SAVE:
                            GUIVS.instance.getControl().getGroups().add(g);
//                            GUIVS.getGroup_messages().put(g.getName(), FXCollections.observableArrayList());
                            break;
                        case UPDATE:

                            //Ersetze das Gruppenobjekt durch das neue Objekt (Groupname = unique)
                            Group changedGroup = GUIVS.instance.getControl().getGroups().filtered(new Predicate<Group>()
                            {
                                @Override
                                public boolean test(Group group)
                                {
                                    if (group.getName().equals(g.getName()))
                                    {
                                        return true;
                                    } else
                                    {
                                        return false;
                                    }
                                }
                            }).get(0);
                            GUIVS.instance.getControl().getGroups().set(GUIVS.instance.getControl().getGroups().indexOf(changedGroup), g);
                            break;
                        case DELETE:
                            Group oldGroup = GUIVS.instance.getControl().getGroups().filtered(new Predicate<Group>()
                            {
                                @Override
                                public boolean test(Group group)
                                {
                                    if (group.getName().equals(g.getName()))
                                    {
                                        return true;
                                    } else
                                    {
                                        return false;
                                    }
                                }
                            }).get(0);
                            GUIVS.instance.getControl().getGroups().remove(oldGroup);
                            GUIVS.getGroup_messages().remove(g.getName());
                            break;
                        default:
                            System.out.println("Fehler in Update_group");
                            break;
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
//                try
//                {
//                    Thread.sleep(200);
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
                switch (type)
                {
                    case SAVE:
                        GUIVS.instance.getControl().getUsers().add(u);
                        break;
                    case UPDATE:

                        //Ersetze das Userobjekt durch das neue Objekt (Username = unique)
                        User changedUser = GUIVS.instance.getControl().getUsers().filtered(new Predicate<User>()
                        {
                            @Override
                            public boolean test(User user)
                            {
                                if (user.getName().equals(u.getName()))
                                {
                                    return true;
                                } else
                                {
                                    return false;
                                }
                            }
                        }).get(0);
                        GUIVS.instance.getControl().getUsers().set(GUIVS.instance.getControl().getUsers().indexOf(changedUser), u);
                        break;
                    case DELETE:
                        User oldUser = GUIVS.instance.getControl().getUsers().filtered(new Predicate<User>()
                        {
                            @Override
                            public boolean test(User user)
                            {
                                if (user.getName().equals(u.getName()))
                                {
                                    return true;
                                } else
                                {
                                    return false;
                                }
                            }
                        }).get(0);
                        GUIVS.instance.getControl().getUsers().remove(oldUser);
                        break;
                    default:
                        System.out.println("Fehler in Update_user");
                        break;
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
//                try
//                {
//                    Thread.sleep(200);
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
                switch (type)
                {
                    case SAVE:
                        GUIVS.instance.getControl().getMessages().add(m);
                        break;
                    case UPDATE:

                        //Ersetze das Messageobjekt durch das neue Objekt (MessageID = unique)
                        GUIVS.instance.getControl().getMessages().set(GUIVS.instance.getControl().getMessages().indexOf(AdminAnsichtFXMLController.getSelectedMessage()),m);

                        break;
                    case DELETE:
                        Message oldMessage = GUIVS.instance.getControl().getMessages().filtered(new Predicate<Message>()
                        {
                            @Override
                            public boolean test(Message message)
                            {
                                if (message.getID() == m.getID())
                                {
                                    return true;
                                } else
                                {
                                    return false;
                                }
                            }
                        }).get(0);
                        GUIVS.instance.getControl().getMessages().remove(oldMessage);
                        break;
                    default:
                        System.out.println("Fehler in Update_user");
                        break;
                }
            }
        });
        t.start();
    }
}
