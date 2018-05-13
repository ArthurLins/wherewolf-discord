package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.views.View;

public class Harlot extends Civilian {

    @Override
    public void init() {
        setName("Puta");
        setDescription("desc");
    }

    @Override
    public void night() {
        userSelector(
                View::harlotVisitAsk,
                (response) -> {
                    action(Actions.HARLOT_VISIT, selfuser, response);
                    finishVote();
                },
                Engine.NIGHT_TIMEOUT
        );
    }
}
