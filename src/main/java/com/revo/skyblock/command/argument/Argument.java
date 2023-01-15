package com.revo.skyblock.command.argument;

import org.bukkit.command.CommandSender;

public interface Argument {
    boolean execute (final CommandSender commandSender, final String[] args);
}
