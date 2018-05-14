package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.game.engine.users.Attribute;
import com.arthurl.wolfbot.views.View;

public class Gunner extends Civilian {

    @Override
    public void init() {
        setName(text("gunner.name"));
        setDescription(text("gunner.description"));
        setUserAttr(Attribute.MUNITION, 2);
    }

    @Override
    public void night() {
        View.gunnerNightMessage(selfuser);
        finishNight();
    }

    @Override
    public void day() {
        final int munition = (int) getUserAttr(Attribute.MUNITION);
        if (munition > 0) {
            userSelector(
                    (ask) -> View.hunterKillAsk(game, selfuser, munition),
                    (response) -> action(Actions.HUNTER_KILL, selfuser, response),
                    Engine.NIGHT_TIMEOUT
            );
        } else {
            View.notHaveMonition(game, selfuser);
        }
    }
}
