package com.jenkins.exception;

public class ItemNotFoundException extends RuntimeException {

    private  Long id;
    public ItemNotFoundException(Long id) {
        super("could not find item " + id);
    }
}
