package main.rmiconnections;

public class RMISecurityManager extends SecurityManager{
    public RMISecurityManager(){
        System.setProperty("java.security.policy", "file:/main.rmiconnections/rmi.policy");
    }
}

