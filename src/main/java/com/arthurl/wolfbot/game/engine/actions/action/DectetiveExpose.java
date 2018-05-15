package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public class DectetiveExpose extends AAction {
    {
        pattern = new Class[]{GameUser.class};
        priority = ActionPriority.MEDIUM;
    }

    @Override
    public void execute() {
        final GameUser detective = (GameUser) objects[0];
        game.getRoleManager().aliveList(Wolf.class).forEach((wolf)->{
            wolf.getRoleKnows().add(detective);
            wolf.sendMessage("Agora toda a ... sabe que é"
                    + detective.getUser().getAsMention() + " é um detetive");
        });
    }
}
