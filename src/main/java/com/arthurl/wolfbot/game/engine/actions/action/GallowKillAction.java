package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.Views.View;
import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public class GallowKillAction extends AAction {

    {
        name = "Matar";
        description = "Matar jogador";
        pattern = new Class[]{GameUser.class};
        priority = ActionPriority.NOW;
    }

    @Override
    public void execute() {
        GameUser killed = (GameUser) objects[0];
        killed.setAlive(false);
        View.gallowKill(game, killed);

    }
}
