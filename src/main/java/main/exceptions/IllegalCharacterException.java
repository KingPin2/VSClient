/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.exceptions;

/**
 *
 * @author Laura
 */
public class IllegalCharacterException extends Exception{
    
    public IllegalCharacterException(){super();}
    public IllegalCharacterException(String message) { super(message); }
    public IllegalCharacterException(String message, Throwable cause) { super(message, cause); }
    public IllegalCharacterException(Throwable cause) { super(cause); }
}
