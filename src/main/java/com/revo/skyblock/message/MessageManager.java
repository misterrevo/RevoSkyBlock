package com.revo.skyblock.message;

import com.google.inject.Singleton;

@Singleton
public class MessageManager {
    public String getCreateIslandSuccess() {
        return "Pomyslnie utworzno wyspe!";
    }

    public String getCreateIslandFailure() {
        return "Nie udalo sie utworzyc wyspy!";
    }

    public String getDeleteIslandFailure() {
        return "Nie udalo sie usunac wyspy!";
    }

    public String getDeleteIslandSuccess() {
        return "Pomyslnie usunieto wyspe!";
    }

    public String getAddMemberIslandNotFound() {
        return "Nie posiadasz wyspy!";
    }

    public String getAddMemberSuccess() {
        return "Pomyslnie dodano gracza do wyspy!";
    }

    public String getAddMemberIsMember() {
        return "Ten gracz nalezy juz do twojej wyspy!";
    }

    public String getRemoveMemberIslandNotFound() {
        return "Nie posiadasz wyspy!";
    }

    public String getRemoveMemberSuccess() {
        return "Pomyslnie usunieto gracza z wyspy";
    }

    public String getRemoveMemberIsNotMember() {
        return "Ten gracz nie jest czlonkiem twojej wyspy!";
    }

    public String getCreateIslandHasIsland() {
        return "Masz juz wyspe!";
    }
}
