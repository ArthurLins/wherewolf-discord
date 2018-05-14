package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;

public class SeeUser extends AAction {

    {
        pattern = new Class[]{GameUser.class, GameUser.class};
        priority = ActionPriority.ASYNC;
    }

    @Override
    public void execute() {
        final GameUser seer = (GameUser) objects[0];
        final GameUser user = (GameUser) objects[1];
        seer.getRoleKnows().add(user);
        View.seeUser(game, seer, user);
    }
}
