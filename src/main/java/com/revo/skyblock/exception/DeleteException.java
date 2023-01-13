package com.revo.skyblock.exception;

public class DeleteException extends Exception {

    public DeleteException(final String key) {
        super("Error while deleting from database entity by key = %s".formatted(key));
    }
}
