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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;
import main.objects.Group;
import main.objects.Message;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Jan-Merlin Geuskens , 3580970
 * @author Laura-Ann Schiestel, 3686779
 * @author Yannick Peter Neumann, 3690024
 *
 * Verwaltet Anzeigetafel-GUI und kuemmert sich um die Initialisierung und das
 * Aktualisieren der NachrichtenBox-Elemente.
 *
 *
 */
public class AnzeigetafelFXMLController implements Initializable
{
    /**
     * Funktionsweise: Der AnzeigetafelFXMLController instanziiert fuer jedes
     * MessageObjekt in einer Gruppe eine NachrichtenBox (selbst erstelles FX-Steuerelement).
     * Um die Referenz auf die ObservableList m (kommt von aussen) zu uebergeben, wird die Methode setM()
     * noch vor Ausfuehren der initialize Methode in der Klasse GUIVS aufgerufen.
     *
     */

    @FXML
    private VBox vbox;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private Label lTafel;
    //Privates GruppenObjekt, dient dem setzen des Labels
    private Group group;
    private ObservableList<Message> m;

    public void setM(ObservableList<Message> m)
    {
        this.m = m;
    }
    public Group getGroup()
    {
        return group;
    }
    public void setGroup(Group group)
    {
        this.group = group;
    }

    //nb haelt die NachrichtenBox Steuerelemente
    private ObservableList<Nachrichtenbox> nb;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                /**
                 * Initialisiert die GUI der Anzeigetafel
                 */

                nb = FXCollections.observableArrayList();
                //Wenn die uebergebene Referenz nicht null ist bzw. die Liste nicht leer ist...
                if (m != null)
                {
                    //...durchlaufe die Liste rueckwaerts (neuste Message zuerst)
                    for (int i = m.size() - 1; i >= 0; i--)
                    {
                        //und instanziiere ein Nachrichtenboxelement fuer jede Nachricht
                        nb.add(new Nachrichtenbox(m.get(i)));
                    }
                }
                //Fuege die Nachrichtenboxelemente zur VBox hinzu
                vbox.getChildren().addAll(nb);
                lTafel.setText("Anzeigetafel der Gruppe " + group.getName());

                //scrollpane settings
                scrollpane.setHbarPolicy(ScrollBarPolicy.NEVER);
                scrollpane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
                scrollpane.setFitToHeight(true);
                scrollpane.setFitToWidth(true);

                //Registriere Changelistener auf die Listenreferenz
                //wird ausgefuehrt, sobald sich irgendetwas aendert (Nachricht geloescht, bearbeitet, hinzugefuegt)
                m.addListener(new ListChangeListener<Message>()
                {

                    @Override
                    public void onChanged(Change<? extends Message> c)
                    {
                        /**
                         * Update der GUI bei Aenderung in der Message-Liste
                         */
                        //Platform.runLater ist notwendig, da Aenderungen an der GUI nur FX-Threads gestattet sind.
                        Platform.runLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //entferne alle NachrichtenBoxen
                                nb.clear();
                                vbox.getChildren().clear();
                                //Baue GUI neu auf
                                if (m != null)
                                {
                                    for (int i = m.size() - 1; i >= 0; i--)
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
