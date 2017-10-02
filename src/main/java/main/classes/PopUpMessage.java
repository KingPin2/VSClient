/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.classes;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * @author Jan-Merlin Geuskens , 3580970
 *
 * dient dem Einfachen erzeugen von PopUp-Nachrichten/Dialogen
 *
 */
public class PopUpMessage
{

    /**
     * erzeugt eine PopUp-Nachricht informativen Charakters
     * @param title Fenstertitel der Nachricht
     * @param text Informationstext der Nachricht
     */
    public void showInformation(String title, String text)
    {
        Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * erzeugt eine PopUp-Nachricht, die einen Fehler signalisiert
     * @param title Fenstertitel der Nachricht
     * @param text Errortext, Fehlermeldunng
     */
    public void showError(String title, String text)
    {
        Alert alert;
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Erzeugt eine PopUp-Nachricht mit Dialogoption
     * @param question Die Frage, die dem Nutzer gestellt wird
     * @return die Antwort, die der Nutzer gegeben hat (JA=true,NEIN=False)
     */
    public boolean showDialog(String question)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Achtung!");
        alert.setHeaderText("");
        alert.setContentText(question);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;

    }
}
