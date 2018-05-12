package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public class SeerUserAction extends AAction {

    {
        name="Seer";
        description = "Revels to users";
        pattern = new Class[]{GameUser.class, GameUser.class};
        priority = ActionPriority.ASYNC;
    }

    @Override
    public void execute() {
        GameUser seer = (GameUser) objects[0];
        GameUser user = (GameUser) objects[1];

        seer.sendMessage(user.getUser().getName() + " é "
                + user.getRole().name);
        seer.getRoleKnows().add(user);
    }
}
