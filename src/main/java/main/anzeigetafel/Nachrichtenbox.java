/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.anzeigetafel;

import main.objects.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Laura
 */
public class Nachrichtenbox extends HBox
{
    private Message messagedaten;
    @FXML
    private Label user;
    @FXML
    private Label zeit;
    @FXML
    private TextArea message;
    private static final DateFormat dateFormat = new SimpleDateFormat("<dd.MM> HH:mm");
    
    public Nachrichtenbox()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NachrichtenboxFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
         try
        {
            fxmlLoader.load();   
        }
        catch(Exception e)
        {
            
        }
                
        
        this.messagedaten = null;
        this.user.setText("testuser");
        this.message.setText("hallowelt. die welt ist schön. java ist auch ganz ok. vs nervt allerdings sehr. vor allem wenn man swing benutzt. nicht böse gement, yannik. muss nur testen, ob der text zeilenumbrüche macht. deswegen so viel nonsense. fertig. aus. micky maus.");
        this.zeit.setText("<--.--> --:--");
        
    }
    /*
    
    */
    public Nachrichtenbox(Message messagedaten)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NachrichtenboxFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try
        {
            fxmlLoader.load();   
        }
        catch(Exception e)
        {
            
        }
                
        
        this.messagedaten = messagedaten;
        this.user.setText(this.messagedaten.getAuthor().getName());
        this.message.setText(this.messagedaten.getMessage());
        this.zeit.setText(dateFormat.format(messagedaten.getTimestamp()));
   
    }
    
    
    
  
    
}
