package com.arthurl.wolfbot.game.engine.roles;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public abstract class ARole {

    public String name;
    public String description;

    public GameUser selfuser;
    public Game game;

    public String text(String prop) {
        return game.getLang().get(prop);
    }

    public String text(String prop, String... strs) {
        return game.getLang().get(prop, strs);
    }

    public void finishVote() {
        game.getEngine().fireVoteOK(selfuser);
    }

    public void finishNight() {
        game.getEngine().fireNightOK(selfuser);
    }

    public void defaultVote(int mult) {
        game.getVoteSelector().requestVote(selfuser, mult);
    }

    public void action(Actions action, Object... objs){
        game.getActionManager().call(action, objs);
    }

    public abstract void night();

    public abstract void day();

    public abstract void vote();


}
