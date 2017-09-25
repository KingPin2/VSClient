package main.rmiconnections;

/**
 * Merlin, 16.08.2017
 */

import main.objects.Board;
import main.objects.Group;
import main.objects.Message;
import main.objects.User;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import main.rmiinterface.*;

public class Client implements Functions{

    private Registry registry;
    private Functions rmi;

    
    public User getUserById(int id) throws Exception {
        return this.rmi.getUserById(id);
    }

    public User getUserByName(String username) throws Exception {
        return this.rmi.getUserByName(username);
    }

    public ArrayList<User> getUsers() throws Exception {
        return this.rmi.getUsers();
    }

    public ArrayList<User> getUsersByLevel(int level) throws Exception {
        return this.rmi.getUsersByLevel(level);
    }

    public void saveUser(User user) throws Exception {
        this.rmi.saveUser(user);
    }

    public Board getBoardById(int id) throws Exception {
        return this.rmi.getBoardById(id);
    }

    public ArrayList<Board> getBoards() throws Exception {
        return this.rmi.getBoards();
    }

    public void saveBoard(Board board) throws Exception {
        this.rmi.saveBoard(board);
    }

    public Group getGroupById(int id) throws Exception {
        return this.rmi.getGroupById(id);
    }

    public ArrayList<Group> getGroups() throws Exception {
        return this.rmi.getGroups();
    }

    public ArrayList<Group> getGroupsByUser(User u) throws Exception {
        return this.rmi.getGroupsByUser(u);
    }

    @Override
    public ArrayList<Group> getGroupsByModerator(User u) throws Exception
    {
        return null;
    }

    public void saveGroup(Group group) throws Exception {
        this.rmi.saveGroup(group);
    }

    public ArrayList<User> getUsersNotInGroup(Group group) throws Exception {
        return this.rmi.getUsersNotInGroup(group);
    }

    /*
    public ArrayList<User> getGroupMembers(int groupId) throws Exception {
        return this.rmi.getGroupMembers(groupId);
    }
*/
    /*
    public void deleteGroupMembers(int groupId) throws Exception {
        this.rmi.deleteGroupMembers(groupId);
    }
*/
    /*
    public void saveGroupMembers(int groupId, ArrayList<User> groupMembers) throws Exception {
        this.rmi.saveGroupMembers(groupId, groupMembers);
    }
*/
    
    public Message getMessageById(int id) throws Exception {
        return this.rmi.getMessageById(id);
    }

    @Override
    public ArrayList<Message> getMessagesByUser(User u) throws Exception
    {
        return null;
    }

    public ArrayList<Message> getMessageByUser(User u) throws Exception {
        return this.rmi.getMessagesByUser(u);
    }

    public ArrayList<Message> getMessages() throws Exception {
        return this.rmi.getMessages();
    }

    public ArrayList<Message> getMessagesByGroup(Group g) throws Exception {
        return this.rmi.getMessagesByGroup(g);
    }

    public void saveMessage(Message message) throws Exception {
        this.rmi.saveMessage(message);
    }

    public User loginUser(String username, String password) throws Exception {
        return this.rmi.loginUser(username, password);
    }
    public String test(int testID) throws Exception{
        return this.rmi.test(42);
    }

    public Group getGroupByName(String name) throws Exception
    {
        return this.rmi.getGroupByName(name);
    }

    public void deleteMessage(Message m) throws Exception{
         this.rmi.deleteMessage(m);
    }

    @Override
    public void deleteUser(User u) throws Exception
    {
        this.rmi.deleteUser(u);
    }

    @Override
    public void deleteBoard(Board b) throws Exception
    {
        this.rmi.deleteBoard(b);
    }

    @Override
    public void deleteGroup(Group g) throws Exception
    {
        this.rmi.deleteGroup(g);
    }

    public Client(String host) {
        try {
            /*
            if(System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }*/
            this.registry = LocateRegistry.getRegistry(host);
            this.rmi = (Functions) registry.lookup("Functions");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    //Main zum Testen
    //public static void main(String[] args)
    // {
    //   Client c = new Client((args.length < 1) ? null : args[0]);
    //  System.out.println(c.addBoard("Küche", 42));
    //}
}
