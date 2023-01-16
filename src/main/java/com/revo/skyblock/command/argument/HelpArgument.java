package com.revo.skyblock.command.argument;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.util.Constants;
import com.revo.skyblock.util.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class HelpArgument implements Argument {

    private final Utils utils;

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        commandSender.sendMessage(Utils.replaceColors("&a]----------[RSB]----------["));
        commandSender.sendMessage(Utils.replaceColors("&c/" + Constants.COMMAND + " " + Constants.CREATE_ARGUMENT + " &8- &atworzy nowa wyspe"));
        return true;
    }
}
