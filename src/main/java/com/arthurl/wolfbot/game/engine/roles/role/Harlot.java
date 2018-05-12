package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;

public class Harlot extends Civilian {
    {
        name = "Prostituta";
        description = "Faz uma visita";
    }

    @Override
    public void night() {
        game.getDefaultUserSelector().select(selfuser, Engine.NIGHT_TIMEOUT, (ask) ->{

        }, (selected) -> {

        });
    }
}
