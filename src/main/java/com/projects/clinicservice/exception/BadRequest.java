package com.projects.clinicservice.exception;



public class BadRequest extends RuntimeException {
    public BadRequest(String message){
        super(message);
    }

}
