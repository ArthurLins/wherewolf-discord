package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.Attribute;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public class GunnerKillAction extends AAction {

    {
        pattern = new Class[]{GameUser.class, GameUser.class};
        priority = ActionPriority.MEDIUM;
    }

    @Override
    public void execute() {
        final GameUser hunter = (GameUser) objects[0];
        final GameUser killed = (GameUser) objects[1];
        final int hunterMunition = (int) hunter.getAttr(Attribute.MUNITION);
        killed.kill(hunter);
        hunter.setAttr(Attribute.MUNITION, hunterMunition - 1);
        killed.sendMessage("Você foi morto pelo caçador");

    }

}
