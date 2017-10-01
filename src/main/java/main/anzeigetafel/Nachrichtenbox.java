/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.anzeigetafel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import main.objects.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 *
 * FX-Controller für das Steuerelement Nachrichtenbox
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

    //leerer Konstruktor, wurde nur zum testen benötigt
    /*
    public Nachrichtenbox()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NachrichtenboxFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        this.messagedaten = null;
        this.user.setText("TestUser42");
        this.message.setText("Hallo Welt!");
        this.zeit.setText("<--.--> --:--");
    }
*/

    /**
     *
     * Konstruktor des Nachrichtenbox-objektes
     * @author Jan-Merlin Geuskens, 3580970
     * @param messagedaten einzelnes MessageObjekt
     *
     */
    public Nachrichtenbox(Message messagedaten)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NachrichtenboxFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //initialisiere GUI
        this.messagedaten = messagedaten;
        this.user.setText(this.messagedaten.getAuthor().getName());
        this.message.setText(this.messagedaten.getMessage());
        this.zeit.setText(dateFormat.format(messagedaten.getTimestamp()));

    }


}
