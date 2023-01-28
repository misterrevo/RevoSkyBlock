package com.revo.skyblock.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.command.argument.AddMemberArgument;
import com.revo.skyblock.command.argument.ChangeOwnerArgument;
import com.revo.skyblock.command.argument.CreateArgument;
import com.revo.skyblock.command.argument.DeleteArgument;
import com.revo.skyblock.command.argument.HelpArgument;
import com.revo.skyblock.command.argument.InfoArgument;
import com.revo.skyblock.command.argument.RemoveMemberArgument;
import com.revo.skyblock.command.argument.SetHomeArgument;
import com.revo.skyblock.command.argument.TeleportToHomeArgument;
import com.revo.skyblock.util.Constants;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class IslandExecutor implements CommandExecutor {

    private final HelpArgument helpArgument;
    private final CreateArgument createArgument;
    private final DeleteArgument deleteArgument;
    private final TeleportToHomeArgument teleportToHomeArgument;
    private final AddMemberArgument addMemberArgument;
    private final RemoveMemberArgument removeMemberArgument;
    private final SetHomeArgument setHomeArgument;
    private final ChangeOwnerArgument changeOwnerArgument;
    private final InfoArgument infoArgument;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().equals(Constants.COMMAND)) {
            if (commandSender instanceof ConsoleCommandSender ) {
                commandSender.sendMessage(Constants.ONLY_FOR_PLAYERS);
                return false;
            }
            if (args.length == 0) {
                return helpArgument.execute(commandSender, args);
            }
            if (args.length == 1) {
                if (args[0].equals(Constants.CREATE_ARGUMENT)) {
                    return createArgument.execute(commandSender, args);
                }
                if (args[0].equals(Constants.DELETE_ARGUMENT)) {
                    return deleteArgument.execute(commandSender, args);
                }
                if (args[0].equals(Constants.HOME_ARGUMENT)) {
                    return teleportToHomeArgument.execute(commandSender, args);
                }
            }
            if (args.length == 2) {
                if (args[0].equals(Constants.ADD_MEMBER_ARGUMENT)) {
                    return addMemberArgument.execute(commandSender, args);
                }
                if (args[0].equals(Constants.REMOVE_MEMBER_ARGUMENT)) {
                    return removeMemberArgument.execute(commandSender, args);
                }
                if (args[0].equals(Constants.HOME_ARGUMENT) && args[1].equals(Constants.SET_ARGUMENT)) {
                    return setHomeArgument.execute(commandSender, args);
                }
                if (args[0].equals(Constants.OWNER_CHANGE_ARGUMENT)) {
                    return changeOwnerArgument.execute(commandSender, args);
                }
                if (args[0].equals(Constants.INFO_ARGUMENT)) {
                    return infoArgument.execute(commandSender, args);
                }
            }
        }
        return false;
    }
}
