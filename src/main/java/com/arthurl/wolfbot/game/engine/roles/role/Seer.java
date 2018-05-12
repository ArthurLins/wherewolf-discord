package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;

public class Seer extends Civilian {
    {
        name = "oieiro";
        description = "Ve tudo";
    }

    @Override
    public void night() {
        game.getDefaultUserSelector().select(selfuser,
                Engine.NIGHT_TIMEOUT,
                (self) -> selfuser.sendMessage("Escolha quem você quer saber:"),
                (selected) -> {
                    action(Actions.SEER_USER, selfuser, selected);
                    finishNight();
                });
    }

}
