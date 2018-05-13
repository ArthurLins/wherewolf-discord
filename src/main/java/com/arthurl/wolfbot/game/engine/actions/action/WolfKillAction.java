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
        final GameUser wolf = (GameUser) objects[0];
        final GameUser vitim = (GameUser) objects[1];
        if (!vitim.inHouse()) {
            wolf.sendMessageLang("wolf.kill-house-empty",
                    vitim.getUser().getDiscriminator());
            return;
        }
        vitim.kill(wolf);
        if (vitim.hasVisitors()) {
            vitim.getRelated().forEach((user) -> {
                user.kill();
                user.sendMessage("Você morreu! Você deu o azar de esta com uma pessoa que foi atacada...");
            });
            vitim.sendMessage("Você foi morto por: " + wolf.getUser().getAsMention() + " e seus visitantes não se slavaram...");
        }
        vitim.sendMessage("Você foi morto por: " + wolf.getUser().getAsMention());
    }
}
