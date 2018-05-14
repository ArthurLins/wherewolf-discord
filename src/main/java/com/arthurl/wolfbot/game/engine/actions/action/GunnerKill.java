package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.Attributes;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;

public class GunnerKill extends AAction {

    {
        pattern = new Class[]{GameUser.class, GameUser.class};
        priority = ActionPriority.MEDIUM;
    }

    @Override
    public void execute() {
        final GameUser hunter = (GameUser) objects[0];
        final GameUser killed = (GameUser) objects[1];
        final int hunterMunition = (int) hunter.getAttr(Attributes.MUNITION);
        if ((int) hunter.getAttr(Attributes.MUNITION) > 0) {
            hunter.setAttr(Attributes.MUNITION, hunterMunition - 1);
            killed.kill(hunter);
            View.killedByGunner(game, killed);
        }
    }

}
