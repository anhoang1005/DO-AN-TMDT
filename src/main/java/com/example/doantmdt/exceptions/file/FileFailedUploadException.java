package com.example.doantmdt.exceptions.file;

public class FileFailedUploadException extends RuntimeException{
    public FileFailedUploadException(String message){
        super(message);
    }
}
