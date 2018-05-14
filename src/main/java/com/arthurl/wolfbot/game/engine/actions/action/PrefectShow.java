package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;

public class PrefectShow extends AAction {
    {
        pattern = new Class[]{GameUser.class};
        priority = ActionPriority.MEDIUM;
    }
    @Override
    public void execute() {
        final GameUser prefect = (GameUser) objects[0];
        prefect.setHidden(false);
        View.prefectDeclare(game, prefect);
    }
}
