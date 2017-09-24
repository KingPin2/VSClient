/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.classes;

import main.exceptions.EmptyStringException;
import main.exceptions.IllegalCharacterException;
import main.rmiconnections.*;


/**
 *
 * @author Laura
 */
public class Control {
    
    public static enum permission{ USER, ADMIN };
 
    private Client c;

    public Client getC() {
        return c;
    }

    public void setC(Client c) {
        this.c = c;
    }
    
    public Control()
    {
        c = new Client("192.168.0.93");
    }
    
    public static boolean isLegit(String probe) throws Exception 
    {
        if(probe.contains("'") || probe.contains("`"))
        {
            throw new IllegalCharacterException();      
        }
        else if(probe.isEmpty())
        {
            throw new EmptyStringException();
        }
        else
        {
            return true;
        }
    }
}
