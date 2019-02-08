package com.arthurl.wolfbot.game.engine.actions;


import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;

public abstract class AAction {

    protected Class[] pattern;
    protected ActionPriority priority;
    protected Object[] objects;
    protected Game game;

    public String text(String prop) {
        return game.getLang().get(prop);
    }

    public String text(String prop, String... strs) {
        return game.getLang().get(prop, strs);
    }


    public abstract void execute();
}
