package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.roles.role.Hunter;
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
        final GameUser hunter = (GameUser) objects[0];
        final GameUser killed = (GameUser) objects[1];
        final int hunterMunition = (int) hunter.getAttr(Hunter.MUNITION);
        killed.kill(hunter);
        hunter.setAttr(Hunter.MUNITION, hunterMunition - 1);
        killed.sendMessage("Você foi morto pelo caçador");

    }

}
