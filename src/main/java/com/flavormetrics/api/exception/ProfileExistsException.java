package com.flavormetrics.api.exception;

public class ProfileExistsException extends RuntimeException {

    public ProfileExistsException() {
        super("User has a profile associated");
    }

}
