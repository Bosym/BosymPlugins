package me.mcmainiac.ArenaManager.api.utils;

@SuppressWarnings("unused")
public enum ArenaType {
    TEAM21(2, 1),
    TEAM22(2, 2),
    TEAM24(2, 4),
    TEAM28(2, 8),
    TEAM41(4, 1),
    TEAM42(4, 2),
    TEAM44(4, 4),
    TEAM48(4, 8),
    TEAM81(8, 1),
    UNSET(0, 0);

    private final int playersperteam;
    private final int numteams;

    ArenaType(int numteams, int playersperteam) {
        this.numteams = numteams;
        this.playersperteam = playersperteam;
    }

    public int getTeamCount() {
        return this.numteams;
    }

    public int getPlayersPerTeam() {
        return this.playersperteam;
    }

    public int getTotalPlayers() {
        return this.numteams * this.playersperteam;
    }
}
