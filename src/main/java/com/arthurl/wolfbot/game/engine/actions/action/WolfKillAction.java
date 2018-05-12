package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public class WolfKillAction extends AAction {
    {
        name = "Lobo mata alguém";
        description = "Lobo mata alguem";
        pattern = new Class[]{GameUser.class, GameUser.class};
        priority = ActionPriority.LOW;
    }

    @Override
    public void execute() {
        GameUser vitim = (GameUser) objects[0];
        GameUser wolf = (GameUser) objects[1];
        if (!vitim.inHouse()) {
            wolf.sendMessageLang("wolf.kill-house-empty",
                    vitim.getUser().getDiscriminator());
            return;
        }
        vitim.setAlive(false);
        vitim.sendMessage("Você foi morto por: " + wolf.getUser().getAsMention());
    }
}
