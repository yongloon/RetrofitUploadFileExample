package com.questdot.retrofituploadfileexample;


public class Respond {

    private String message;
    private Boolean error;

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}