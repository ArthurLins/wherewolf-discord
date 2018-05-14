package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.action.HarlotVisit;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.views.View;

public class Harlot extends Civilian {

    @Override
    public void init() {
        setName(text("harlot.name"));
        setDescription(text("harlot.description"));
    }

    @Override
    public void night() {
        userSelector(
                View::harlotVisitAsk,
                (response) -> {
                    action(HarlotVisit.class, selfuser, response);
                    finishVote();
                },
                Engine.NIGHT_TIMEOUT
        );
    }
}
