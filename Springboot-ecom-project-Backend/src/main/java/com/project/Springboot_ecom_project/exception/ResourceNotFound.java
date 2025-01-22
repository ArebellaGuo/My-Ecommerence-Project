package com.project.Springboot_ecom_project.exception;


public class ResourceNotFound extends RuntimeException{
    private String fieldName;
    private Long fieldId;


    public ResourceNotFound(String fieldName, Long fildId) {
        super(String.format("%s not found with id: %s",fieldName,fildId));
        this.fieldName = fieldName;
        this.fieldId = fildId;
    }
}
