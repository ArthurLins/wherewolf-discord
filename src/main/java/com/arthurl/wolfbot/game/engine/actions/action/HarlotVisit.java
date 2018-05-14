package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;

public class HarlotVisit extends AAction {

    {
        pattern = new Class[]{GameUser.class, GameUser.class};
        priority = ActionPriority.ASYNC;
    }

    @Override
    public void execute() {
        final GameUser harlot = (GameUser) objects[0];
        final GameUser visited = (GameUser) objects[1];
        if (visited.inHouse()){
            harlot.setInHouse(false);
            if (visited.hasRole(Wolf.class)) {
                harlot.kill();
                View.harlotVisitWolf(game, harlot, visited);
                return;
            }
            visited.getRelated().add(harlot);
            View.harlotVisit(game, harlot, visited);
        }

    }
}
