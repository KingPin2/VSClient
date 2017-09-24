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
public class IllegalPermissionLevelException extends Exception{
    public IllegalPermissionLevelException(){super();}
    public IllegalPermissionLevelException(String message) { super(message); }
    public IllegalPermissionLevelException(String message, Throwable cause) { super(message, cause); }
    public IllegalPermissionLevelException(Throwable cause) { super(cause); }
    
}
