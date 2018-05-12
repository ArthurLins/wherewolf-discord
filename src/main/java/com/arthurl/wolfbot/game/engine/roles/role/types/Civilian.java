package com.arthurl.wolfbot.game.engine.roles.role.types;

import com.arthurl.wolfbot.game.engine.roles.ARole;

public abstract class Civilian extends ARole {

    {
        name = "Cidadão";
        description = "Não faz nada não!";
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
}
