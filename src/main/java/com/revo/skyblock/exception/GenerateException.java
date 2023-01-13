package com.revo.skyblock.exception;


import org.bukkit.Location;

public class GenerateException extends Exception {

    public GenerateException(final Location location) {
        super("Error while generating structure on location: %s".formatted(location));
    }
}
