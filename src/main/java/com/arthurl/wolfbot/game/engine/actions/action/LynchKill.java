package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;

public class LynchKill extends AAction {

    {
        pattern = new Class[]{GameUser.class};
        priority = ActionPriority.BLOCK;
    }

    @Override
    public void execute() {
        System.out.println("lynch");
        final GameUser killed = (GameUser) objects[0];
        killed.kill();
        killed.setHidden(false);
        View.lynchKill(game, killed);
        //
    }
}
