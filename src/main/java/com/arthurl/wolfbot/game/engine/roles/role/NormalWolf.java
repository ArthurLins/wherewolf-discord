package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;

public class NormalWolf extends Wolf{
    @Override
    public void init() {
        setName(text("wolf.name"));
        setDescription(text("wolf.description"));
    }
}
