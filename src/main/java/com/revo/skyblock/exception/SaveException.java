package com.revo.skyblock.exception;

public class SaveException extends Exception {

    public SaveException(final Object object) {
        super("Error while saving to database entity = %s".formatted(object));
    }
}
