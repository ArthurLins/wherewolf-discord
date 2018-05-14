package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;

public class LynchKillAction extends AAction {

    {
        pattern = new Class[]{GameUser.class};
        priority = ActionPriority.NOW;
    }

    @Override
    public void execute() {
        final GameUser killed = (GameUser) objects[0];
        killed.kill();
        View.lynchKill(game, killed);
        //
    }
}
