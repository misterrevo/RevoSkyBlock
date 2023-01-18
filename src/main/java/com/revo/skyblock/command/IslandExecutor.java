package com.revo.skyblock.command;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.command.argument.AddMemberArgument;
import com.revo.skyblock.command.argument.CreateArgument;
import com.revo.skyblock.command.argument.DeleteArgument;
import com.revo.skyblock.command.argument.HelpArgument;
import com.revo.skyblock.command.argument.RemoveMemberArgument;
import com.revo.skyblock.util.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

@Singleton
public class IslandExecutor implements CommandExecutor {

    private final Injector injector = Plugin.getApplicationContext().getInjector();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().equals(Constants.COMMAND)) {
            if (commandSender instanceof ConsoleCommandSender ) {
                return false;
            }
            if (args.length == 0) {
                return injector.getInstance(HelpArgument.class).execute(commandSender, args);
            }
            if (args.length == 1) {
                if (args[0].equals(Constants.CREATE_ARGUMENT)) {
                    return injector.getInstance(CreateArgument.class).execute(commandSender, args);
                }
                if (args[0].equals(Constants.DELETE_ARGUMENT)) {
                    return injector.getInstance(DeleteArgument.class).execute(commandSender, args);
                }
            }
            if (args.length == 2) {
                if (args[0].equals(Constants.ADD_MEMBER_ARGUMENT)) {
                    return injector.getInstance(AddMemberArgument.class).execute(commandSender, args);
                }
                if (args[0].equals(Constants.REMOVE_MEMBER_ARGUMENT)) {
                    return injector.getInstance(RemoveMemberArgument.class).execute(commandSender, args);
                }
            }
        }
        return false;
    }
}
