package com.project.househealth.exception;

public class UnauthorizedFamilyActionException extends RuntimeException{
    public UnauthorizedFamilyActionException(String message){
        super(message);
    }
}
