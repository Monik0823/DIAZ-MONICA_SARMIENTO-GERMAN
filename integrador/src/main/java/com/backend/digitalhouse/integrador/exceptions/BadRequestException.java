package com.backend.digitalhouse.integrador.exceptions;

public class BadRequestException extends Exception{
    public BadRequestException(String message){
        super(message);
    }
}
