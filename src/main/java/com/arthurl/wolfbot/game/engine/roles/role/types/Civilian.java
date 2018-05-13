package com.arthurl.wolfbot.game.engine.roles.role.types;

import com.arthurl.wolfbot.game.engine.roles.ARole;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public abstract class Civilian extends ARole {

    @Override
    public void init() {
        setName(text("villager.name"));
        setDescription(text("villager.description"));
    }

    @Override
    public void night() {
        selfuser.sendMessage("Como um bom civil você não faz nada a noite.");
        finishNight();
    }

    @Override
    public void day() {
    }

    @Override
    public void vote() {
        defaultVote(1);
    }

    @Override
    public void kill(GameUser killer) {
        defaultDie();
    }
}
