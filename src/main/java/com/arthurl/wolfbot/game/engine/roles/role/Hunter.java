package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.views.View;

public class Hunter extends Civilian {

    public static final String MUNITION = "munition";

    @Override
    public void init() {
        setName("Caçador");
        setDescription("desc");
        setUserAttr(MUNITION, 3);
    }

    @Override
    public void night() {
        final int munition = (int) getUserAttr(MUNITION);
        if (munition > 0) {
            userSelector((ask) -> {
                View.hunterKillAsk(game, selfuser, munition);
            }, (response) -> {
                action(Actions.HUNTER_KILL, selfuser, response);
            }, Engine.NIGHT_TIMEOUT);
        } else {
            View.notHaveMonition(game, selfuser);
        }
    }
}
