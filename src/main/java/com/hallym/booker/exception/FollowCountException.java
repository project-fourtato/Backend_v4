package com.hallym.booker.exception;

public class FollowCountException extends RuntimeException{
    public FollowCountException(){
    }
    public FollowCountException(String message){
        super(message);
    }
    public FollowCountException(String message, Throwable cause){
        super(message, cause);
    }
    public FollowCountException(Throwable cause){
        super(cause);
    }
}
