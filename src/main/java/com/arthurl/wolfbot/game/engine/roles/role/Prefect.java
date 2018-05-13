package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.views.View;

public class Prefect extends Civilian {

    @Override
    public void init() {
        setName("Prefeito");
        setDescription("desc");
    }

    @Override
    public void night() {
        optionSelector(
                new String[]{
                        text("yes"),
                        text("no")
                },
                View::askPrefectToShowAll,
                (index, selection) -> {
                    if (index == 1) {
                        action(Actions.PREFECT_SHOW, selfuser);
                    }
                    finishNight();
                },
                Engine.NIGHT_TIMEOUT
        );
    }

    @Override
    public void vote() {
        if (selfuser.isHidden()) {
            defaultVote(1);
        } else {
            defaultVote(2);
        }
    }
}
