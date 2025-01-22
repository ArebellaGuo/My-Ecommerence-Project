package com.project.Springboot_ecom_project.exception;

public class APIException extends RuntimeException{
    String fileName;
    String message;

    public APIException(String fileName, String message) {
        super(String.format("%s %s",fileName,message));
        this.fileName = fileName;
        this.message = message;
    }

    public APIException(String message) {
        super(String.format("%s",message));
        this.message = message;
    }


}
