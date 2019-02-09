package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.action.PrefectShow;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.views.View;

public class Prefect extends Civilian {

    @Override
    public void init() {
        setName(text("prefect.name"));
        setDescription(text("prefect.description"));
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
                        action(PrefectShow.class, selfuser);
                    }
                    finishNight();
                },
                game.getSettings().getNightTime()
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
