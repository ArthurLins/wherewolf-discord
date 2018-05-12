package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.views.View;
import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;

public class Prefect extends Civilian {

    {
        name = "Prefeito";
        description = "Assim que se expoe, seu voto vale 2";
    }

    @Override
    public void night() {
        game.getDefaultOptionSelector().select(selfuser,
                Engine.NIGHT_TIMEOUT,
                (ask)->{
                    View.askPrefectToShowAll(selfuser);
                }, new String[]{
                        text("yes"),
                        text("no")
                }, (index, selection) ->{
                    if (index == 1){
                        action(Actions.PREFECT_SHOW, selfuser);
                    }
                    finishNight();
                });
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
