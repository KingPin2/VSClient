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
public class NoUserFoundException extends Exception{
    public NoUserFoundException(){super();}
    public NoUserFoundException(String message) { super(message); }
    public NoUserFoundException(String message, Throwable cause) { super(message, cause); }
    public NoUserFoundException(Throwable cause) { super(cause); }  
}
