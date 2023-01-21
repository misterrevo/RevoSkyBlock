package com.revo.skyblock.message;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.util.Constants;
import com.revo.skyblock.util.Utils;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MessageManager {

    private final Utils utils;

    public String getCreateIslandSuccess() {
        return utils.replaceColors("&aPomyslnie utworzno wyspe!");
    }

    public String databaseExceptionMessage() {
        return utils.replaceColors("&cInternal error, contact with administrator!");
    }

    public String getDeleteIslandFailure() {
        return utils.replaceColors("&cNie udalo sie usunac wyspy!");
    }

    public String getDeleteIslandSuccess() {
        return utils.replaceColors("&aPomyslnie usunieto wyspe!");
    }

    public String getAddMemberIslandNotFound() {
        return utils.replaceColors("&cNie posiadasz wyspy!");
    }

    public String getAddMemberSuccess() {
        return utils.replaceColors("&aPomyslnie dodano gracza do wyspy!");
    }

    public String getAddMemberIsMember() {
        return utils.replaceColors("&cTen gracz nalezy juz do twojej wyspy!");
    }

    public String getRemoveMemberIslandNotFound() {
        return utils.replaceColors("&cNie posiadasz wyspy!");
    }

    public String getRemoveMemberSuccess() {
        return utils.replaceColors("&aPomyslnie usunieto gracza z wyspy");
    }

    public String getRemoveMemberIsNotMember() {
        return utils.replaceColors("&cTen gracz nie jest czlonkiem twojej wyspy!");
    }

    public String getCreateIslandHasIsland() {
        return utils.replaceColors("&cMasz juz wyspe!");
    }

    public List<String> getHelpCommands(){
        return Arrays.asList(
                utils.replaceColors("&a]----------[RSB]----------["),
                utils.replaceColors("&c/" + Constants.COMMAND + " " + Constants.CREATE_ARGUMENT + " &8- &atworzy nowa wyspe"),
                utils.replaceColors("&c/" + Constants.COMMAND + " " + Constants.DELETE_ARGUMENT + " &8- &ausuwa wyspe"),
                utils.replaceColors("&c/" + Constants.COMMAND + " " + Constants.ADD_MEMBER_ARGUMENT + " " + Constants.NICKNAME +" &8- &adodaje gracza do wyspy"),
                utils.replaceColors("&c/" + Constants.COMMAND + " " + Constants.REMOVE_MEMBER_ARGUMENT + " " + Constants.NICKNAME +" &8- &ausuwa gracza Z wyspy"),
                utils.replaceColors("&c/" + Constants.COMMAND + " " + Constants.HOME_ARGUMENT + " &8- &ateleportuje na wyspe"),
                utils.replaceColors("&c/" + Constants.COMMAND + " " + Constants.HOME_ARGUMENT + " " + Constants.SET_ARGUMENT + " &8- &austawia dom wyspy")
        );
    }

    public String getSetHomeIslandNotFound() {
        return utils.replaceColors("&cMusisz miec wyspe zeby ustawic dom!");
    }

    public String getSetHomeSuccess() {
        return utils.replaceColors("&aUstawiono dom wyspy!");
    }

    public String getTeleportToHomeSuccess() {
        return utils.replaceColors("&aTeleportuje na wyspe!");
    }

    public String getTeleportToHomeFailure() {
        return utils.replaceColors("&cMusisz miec wyspe zeby sie teleportowac!");
    }

    public String getSetHomeFailure() {
        return utils.replaceColors("&cMusisz byc na wyspie, zeby ustawic dom!");
    }

    public String getCreateIslandSchedule() {
        return utils.replaceColors("&cNie dawno utworzyles wyspe, musisz chwile poczekac!");
    }
}
