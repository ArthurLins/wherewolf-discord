package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.users.Attribute;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;

public class Drunk extends Civilian {

    @Override
    public void init() {
        setName(text("drunk.name"));
        setDescription(text("drunk.description"));

    }

    @Override
    public void night() {
        View.drunkNightMessage(selfuser);
        finishNight();
    }

    @Override
    public void kill(GameUser killer) {
        if (killer == null) {
            return;
        }
        if (killer.hasRole(Wolf.class)) {
            killer.setAttr(Attribute.DRUNK);
            View.wolfKillDrunk(killer);
        }
    }
}
