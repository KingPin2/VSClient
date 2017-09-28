/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.anzeigetafel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import main.objects.Group;
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
 * @author Laura
 */
public class AnzeigetafelFXMLController implements Initializable
{

    @FXML
    private VBox vbox;


    @FXML
    private ScrollPane scrollpane;

    @FXML
    private HBox hbox;

    @FXML
    private Label lTafel;

    public Group getGroup()
    {
        return group;
    }

    public void setGroup(Group group)
    {
        this.group = group;
    }

    private Group group;
    private ObservableList<Message> m;

    public ObservableList<Message> getM()
    {
        return m;
    }

    public void setM(ObservableList<Message> m)
    {
        this.m = m;
    }

    private ObservableList<Nachrichtenbox> nb;

    public void reset()
    {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                nb = FXCollections.observableArrayList();
                if (m != null)
                {

                    for(int i = 0; i <  m.size(); i++ )
                    {
                        nb.add(new Nachrichtenbox(m.get(i)));
                    }
//                    for (Message me : m)
//                    {
//                        nb.add(new Nachrichtenbox(me));
//                    }
                }

                vbox.getChildren().addAll(nb);
                lTafel.setText(group.getName());

                scrollpane.setHbarPolicy(ScrollBarPolicy.NEVER);
                scrollpane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
                scrollpane.setFitToHeight(true);
                scrollpane.setFitToWidth(true);

                m.addListener(new ListChangeListener<Message>()
                {

                    @Override
                    public void onChanged(Change<? extends Message> c)
                    {
                        Platform.runLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                nb.clear();
                                vbox.getChildren().clear();
                                if (m != null)
                                {
                                    for(int i = 0; i <  m.size(); i++ )
                                    {
                                        nb.add(new Nachrichtenbox(m.get(i)));
                                    }
                                }

                                vbox.getChildren().addAll(nb);
                            }
                        });

                    }
                });
            }
        });



    }

}
