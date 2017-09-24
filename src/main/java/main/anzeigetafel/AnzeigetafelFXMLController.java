/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.anzeigetafel;

import main.objects.Message;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Laura
 */
public class AnzeigetafelFXMLController implements Initializable {
    
    @FXML
    private VBox vbox;
   
    
    @FXML
    private ScrollPane scrollpane;
    
    @FXML
    private HBox hbox;
    
    @FXML
    private Label lTafel;
    
    private ArrayList<Message> m;

    public ArrayList<Message> getM() {
        return m;
    }

    public void setM(ArrayList<Message> m) {
        this.m = m;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ArrayList<Nachrichtenbox> c = new ArrayList();
        
        for(Message me: m)
        {
            c.add(new Nachrichtenbox(me));
        }
        
        vbox.getChildren().addAll(c);
        
        scrollpane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollpane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
        scrollpane.setFitToHeight(true);
        scrollpane.setFitToWidth(true);
        
        
    }    
    
}
