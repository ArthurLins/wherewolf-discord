package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;

public class WolfKill extends AAction {
    {
        pattern = new Class[]{GameUser.class, GameUser.class};
        priority = ActionPriority.LOW;
    }

    @Override
    public void execute() {
        final GameUser wolf = (GameUser) objects[0];
        final GameUser vitim = (GameUser) objects[1];
        if (!vitim.inHouse()) {
            View.wolfKillHouseEmpty(game, wolf, vitim);
            return;
        }
        vitim.kill(wolf);
        if (vitim.hasVisitors()) {
            vitim.getRelated().forEach((user) -> {
                user.kill();
                View.wolfRelatedKill(game, wolf, user);
            });
            View.wolfKillAndRelated(game, vitim, wolf);
        }
        View.wolfKill(game, vitim, wolf);
    }
}
