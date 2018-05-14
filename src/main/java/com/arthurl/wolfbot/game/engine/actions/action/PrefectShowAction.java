package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public class PrefectShowAction extends AAction {
    {
        pattern = new Class[]{GameUser.class};
        priority = ActionPriority.MEDIUM;
    }
    @Override
    public void execute() {
        final GameUser prefect = (GameUser) objects[0];
        prefect.setHidden(false);
        game.getBroadcaster().sendLang("prefect.declare", prefect.getUser().getAsMention());
    }
}
