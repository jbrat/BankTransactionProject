package com.ujm.xmltech.exception;

/**
 * Unitary exception for validate a transaction, message correspond to an errorCode
 * 
 * @author UJM's students
 */
public class UnitaryException extends Exception {
    
    public UnitaryException(String message) {
        super(message);
    }
}
