package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.views.View;

public class Seer extends Civilian {

    @Override
    public void init() {
        setName(text("seer.name"));
        setDescription(text("seer.description"));
    }

    @Override
    public void night() {
        userSelector(
                View::seerViewAsk,
                (selected) -> {
                    action(Actions.SEER_USER, selfuser, selected);
                    finishNight();
                },
                Engine.NIGHT_TIMEOUT
        );
    }

}
