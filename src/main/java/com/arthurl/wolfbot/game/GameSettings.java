package com.arthurl.wolfbot.game;

public class GameSettings {

    private int maxUsers;
    private int minUsers;
    private int dayTime;
    private int voteTime;
    private int nightTime;
    private String lang;

    public GameSettings(){
        this.dayTime = 5000;
        this.voteTime = 20000;
        this.nightTime = 20000;
        this.lang = "pt_br";
        this.minUsers = 2;
        this.maxUsers = 41;
    }


    public int getMaxUsers() {
        return maxUsers;
    }

    public int getMinUsers() {
        return minUsers;
    }

    public String getLang() {
        return lang;
    }

    public int getDayTime() {
        return dayTime;
    }

    public int getVoteTime() {
        return voteTime;
    }

    public int getNightTime() {
        return nightTime;
    }
}
