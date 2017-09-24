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
public class EmptyStringException extends Exception{
    public EmptyStringException(){super();}
    public EmptyStringException(String message) { super(message); }
    public EmptyStringException(String message, Throwable cause) { super(message, cause); }
    public EmptyStringException(Throwable cause) { super(cause); }   
}
