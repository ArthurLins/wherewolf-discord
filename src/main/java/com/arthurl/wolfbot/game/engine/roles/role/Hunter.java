package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;

public class Hunter extends Civilian {
    @Override
    public void init() {
        setName(text("hunter.name"));
        setDescription(text("hunter.description"));
    }
}
