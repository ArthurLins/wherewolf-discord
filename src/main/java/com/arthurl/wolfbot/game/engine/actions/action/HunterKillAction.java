package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public class HunterKillAction extends AAction{
    {
        name = "Hunter kill";
        description = "Kill";
        pattern = new Class[]{GameUser.class, GameUser.class};
        priority = ActionPriority.MEDIUM;
    }

    @Override
    public void execute() {
        GameUser hunter = (GameUser) objects[0];
        GameUser killed = (GameUser) objects[1];

        killed.setAlive(false);
        killed.sendMessage("Você foi morto pelo caçador");

    }

}
