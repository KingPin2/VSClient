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
public class NoItemSelectedException extends Exception {
    public NoItemSelectedException(){super();}
    public NoItemSelectedException(String message) { super(message); }
    public NoItemSelectedException(String message, Throwable cause) { super(message, cause); }
    public NoItemSelectedException(Throwable cause) { super(cause); }
}
