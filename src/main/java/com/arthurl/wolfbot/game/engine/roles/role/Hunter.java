package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;

public class Hunter extends Civilian {
    @Override
    public void night() {
        if (!selfuser.isAlive()) {return;}
        game.getDefaultUserSelector().select(selfuser, Engine.NIGHT_TIMEOUT,
                (ask) -> ask.sendMessage("Selecione al.guem para matar:"),
                (selected)-> action(Actions.HUNTER_KILL, selfuser, selected));
    }
}
