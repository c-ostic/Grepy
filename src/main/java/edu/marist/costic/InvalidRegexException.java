package edu.marist.costic;

/**
 * Exception for regex parse errors in the NFA class.
 */
public class InvalidRegexException extends Exception {
    public InvalidRegexException(String message) {
        super(message);
    }
}
