package com.example.doantmdt.exceptions.mail;

public class SendMailFailedException extends RuntimeException{
    public SendMailFailedException(String s){
        super(s);
    }
}
