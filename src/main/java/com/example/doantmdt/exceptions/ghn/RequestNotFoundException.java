package com.example.doantmdt.exceptions.ghn;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(String s){
        super(s);
    }
}
