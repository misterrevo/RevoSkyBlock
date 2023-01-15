package com.revo.skyblock.command.argument;

import com.google.inject.Singleton;
import com.revo.skyblock.util.Constants;
import org.bukkit.command.CommandSender;

@Singleton
public class HelpArgument implements Argument {
    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("[RSB]");
        commandSender.sendMessage("/" + Constants.COMMAND + " " + Constants.CREATE_ARGUMENT + " - tworzy nowa wyspe");
        return true;
    }
}
