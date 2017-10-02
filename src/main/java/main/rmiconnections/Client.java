package main.rmiconnections;

/**
 * @author Jan-Merlin Geuskens, 3580970
 * Stellt die RMI Implementierung, die Cached Methoden und Objekte sowie die CallBack-Implementierung.
 * Teileweise noch ungenutzte Methoden wurden bereits vorausschauen für spätere Versionen implementiert
 */

import javafx.application.Platform;
import main.classes.GUIVS;
import main.classes.PopUpMessage;
import main.exceptions.*;
import main.guivs.AdminAnsichtFXMLController;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import main.rmiinterface.CachedFunctions;
import main.rmiinterface.Functions;
import main.rmiinterface.NotifyUpdate;
import main.rmiinterface.UpdateType;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.function.Predicate;

public class Client extends UnicastRemoteObject implements NotifyUpdate
{

    private Registry registry;
    private Functions rmi;
    private CachedFunctions cRMI;

    public String getClientID()
    {
        return clientID;
    }

    private String clientID = "NONE";


    public User getUserById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.cRMI.getUserById(clientID, id);
    }

    public User getUserByName(String username) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.rmi.getUserByName(clientID, username);
    }

    public ArrayList<User> getUsers() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.rmi.getUsers(clientID);
    }

    public ArrayList<User> getUsersByLevel(int level) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.rmi.getUsersByLevel(clientID, level);
    }

    public void saveUser(User user) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException, UserAuthException
    {
        this.rmi.saveUser(clientID, user);
    }

    public Group getGroupById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.cRMI.getGroupById(clientID, id);
    }

    public ArrayList<Group> getGroups() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.rmi.getGroups(clientID);
    }

    public ArrayList<Group> getGroupsByUser(User u) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.rmi.getGroupsByUser(clientID, u);
    }

    public ArrayList<Group> getGroupsByModerator(User u)
    {
        return null;
    }

    public void saveGroup(Group group) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException, UserAuthException
    {
        this.rmi.saveGroup(clientID, group);
    }

    public ArrayList<User> getUsersNotInGroup(Group group) throws RemoteException, UserAuthException
    {
        return this.rmi.getUsersNotInGroup(clientID, group);
    }

    public Message getMessageById(int id) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.cRMI.getMessageById(clientID, id, cRMI);
    }

    public ArrayList<Message> getMessagesByUser(User u) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.rmi.getMessagesByUser(clientID, u, cRMI);
    }

    public ArrayList<Message> getMessages() throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.rmi.getMessages(clientID, cRMI);
    }

    public ArrayList<Message> getMessagesByGroup(Group g) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.rmi.getMessagesByGroup(clientID, g, cRMI);
    }

    public void saveMessage(Message message) throws DatabaseObjectNotSavedException, RemoteException, DatabaseConnectionException, UserAuthException
    {
        this.rmi.saveMessage(clientID, message, cRMI);
    }

    public User loginUser(String username, String password) throws RemoteException
    {
        return this.rmi.login(clientID, username, password);
    }


    public Group getGroupByName(String name) throws DatabaseConnectionException, RemoteException, DatabaseObjectNotFoundException, UserAuthException
    {
        return this.rmi.getGroupByName(clientID, name);
    }

    public void deleteMessage(Message m) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException
    {
        this.rmi.deleteMessage(clientID, m);
    }

    public void deleteUser(User u) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, DatabaseUserIsModException, UserAuthException
    {
        this.rmi.deleteUser(clientID, u, cRMI);
    }

    public void deleteGroup(Group g) throws RemoteException, DatabaseConnectionException, DatabaseObjectNotDeletedException, UserAuthException
    {
        this.rmi.deleteGroup(clientID, g, cRMI);
    }

    public void disconnect() throws RemoteException
    {
        this.rmi.disconnect(clientID);
    }


    public Client(String host) throws RemoteException
    {
        super();
        try
        {
            /*
            if(System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }*/
            this.registry = LocateRegistry.getRegistry(host);
            this.rmi = (Functions) registry.lookup("Functions");
            this.cRMI = new CachedFunctions(rmi);
            clientID = rmi.connect(this);
        } catch (Exception e)
        {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onUpdateGroup(Group g, UpdateType type) throws RemoteException
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (GUIVS.instance.getControl().getGroups())
                {


                    User me = GUIVS.instance.getMe();
                    switch (type)
                    {
                        case SAVE:
                            //Wenn ich Admin bin, oder Member der Gruppe
                            if (me.getLevel() == 2 || (g.getMembers().contains(me)))
                            {
                                GUIVS.instance.getControl().getGroups().add(g);
                            }
                            break;
                        case UPDATE:

                            //Ersetze das Gruppenobjekt durch das neue Objekt (Groupname = unique)
                            if (me.getLevel() == 2 || (g.getMembers().contains(me)))
                            {
                                Group changedGroup = GUIVS.instance.getControl().getGroups().filtered(new Predicate<Group>()
                                {
                                    @Override
                                    public boolean test(Group group)
                                    {
                                        return group.getName().equals(g.getName());
                                    }
                                }).get(0);
                                GUIVS.instance.getControl().getGroups().set(GUIVS.instance.getControl().getGroups().indexOf(changedGroup), g);
                            }
                            break;
                        case DELETE:
                            if (me.getLevel() == 2 || (g.getMembers().contains(me)))
                            {
                                Group oldGroup = GUIVS.instance.getControl().getGroups().filtered(new Predicate<Group>()
                                {
                                    @Override
                                    public boolean test(Group group)
                                    {
                                        return group.getName().equals(g.getName());
                                    }
                                }).get(0);
                                GUIVS.instance.getControl().getGroups().remove(oldGroup);
                                GUIVS.getGroup_messages().remove(g.getName());
                            }
                            break;
                        default:
                            System.out.println("Fehler in Update_group");
                            break;
                    }

                }
            }
        });

    }


    @Override
    public synchronized void onUpdateUser(User u, UpdateType type) throws RemoteException
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (GUIVS.instance.getControl().getUsers())
                {
                    cRMI.onUpdateUser(u, type);

                    User me = GUIVS.instance.getMe();


                    switch (type)
                    {
                        case SAVE:
                            if (me.getLevel() == 2)
                            {
                                GUIVS.instance.getControl().getUsers().add(u);
                            }
                            break;
                        case UPDATE:

                            //Ersetze das Userobjekt durch das neue Objekt (Username = unique)
                            if (me.getLevel() == 2 || u.getID() == me.getID())
                            {
                                User changedUser = GUIVS.instance.getControl().getUsers().filtered(new Predicate<User>()
                                {
                                    @Override
                                    public boolean test(User user)
                                    {
                                        return user.getName().equals(u.getName());
                                    }
                                }).get(0);
                                GUIVS.instance.getControl().getUsers().set(GUIVS.instance.getControl().getUsers().indexOf(changedUser), u);
                            }
                            break;
                        case DELETE:
                            if (me.getLevel() == 2)
                            {
                                User oldUser = GUIVS.instance.getControl().getUsers().filtered(new Predicate<User>()
                                {
                                    @Override
                                    public boolean test(User user)
                                    {
                                        return user.getName().equals(u.getName());
                                    }
                                }).get(0);
                                GUIVS.instance.getControl().getUsers().remove(oldUser);
                            }
                            break;
                        default:
                            System.out.println("Fehler in Update_user");
                            break;
                    }
                }
            }

        });

    }

    @Override
    public synchronized void onUpdateMessage(Message m, UpdateType type) throws RemoteException
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                cRMI.onUpdateMessage(m, type);


                User me = GUIVS.instance.getMe();
                switch (type)
                {
                    case SAVE:
                        if (me.getLevel() == 2 || GUIVS.instance.getControl().getGroups().contains(m.getGroup()))
                        {
                            GUIVS.instance.getControl().getMessages().add(m);
                        }
                        break;
                    case UPDATE:

                        //Ersetze das Messageobjekt durch das neue Objekt (MessageID = unique)
                        if (me.getLevel() == 2 || GUIVS.instance.getControl().getGroups().contains(m.getGroup()))
                        {
                            GUIVS.instance.getControl().getMessages().set(GUIVS.instance.getControl().getMessages().indexOf(AdminAnsichtFXMLController.getSelectedMessage()), m);
                        }
                        break;
                    case DELETE:
                        if (me.getLevel() == 2 || GUIVS.instance.getControl().getGroups().contains(m.getGroup()))
                        {
                            Message oldMessage = GUIVS.instance.getControl().getMessages().filtered(new Predicate<Message>()
                            {
                                @Override
                                public boolean test(Message message)
                                {
                                    return message.getID() == m.getID();
                                }
                            }).get(0);
                            GUIVS.instance.getControl().getMessages().remove(oldMessage);
                        }
                        break;
                    default:
                        System.out.println("Fehler in Update_user");
                        break;
                }
            }
        });
    }
}

