package com.revo.skyblock.command.argument;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.message.MessageManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class HelpArgument implements Argument {

    private final MessageManager messageManager;

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        messageManager.getHelpCommands().forEach(commandSender::sendMessage);
        return true;
    }
}
