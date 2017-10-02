package main.database;

import main.objects.Group;
import main.objects.Message;
import main.objects.User;

import java.util.ArrayList;

/**
 * Created by Dominik on 18.08.2017.
 */
public class ObjectFactory {

    /**
     * Create user
     * @param name name
     * @param password password
     * @param level level
     * @return user
     */
    public static User createUser(String name, String password, int level){
        return new User(name, password, level);
    }

    /**
     * Create message
     * @param message message
     * @param author author
     * @return message
     */
    public static Message createMessage(String message, User author){
        return new Message(message, author);
    }

    /**
     * Create group message
     * @param message message
     * @param author author
     * @param group group
     * @return message
     */
    public static Message createGroupMessage(String message, User author, Group group){
        return new Message(message, author, group);
    }

    /**
     * Create empty group
     * @param name name
     * @param moderator moderator
     * @return group
     */
    public static Group createEmptyGroup(String name, User moderator){
        return new Group(name, moderator);
    }

    /**
     * Create group with members
     * @param name name
     * @param moderator moderator
     * @param members members
     * @return group
     */
    public static Group createGroupWithMembers(String name, User moderator, ArrayList<User> members){
        return new Group(name, moderator, members);
    }
}
