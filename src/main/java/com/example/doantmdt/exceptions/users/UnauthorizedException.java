package com.example.doantmdt.exceptions.users;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String s){
        super(s);
    }
}
