package com.arthurl.wolfbot.game.engine.roles.role.types;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.action.WolfKill;
import com.arthurl.wolfbot.game.engine.roles.ARole;
import com.arthurl.wolfbot.game.engine.users.Attributes;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.game.engine.votes.VoteTypes;
import com.arthurl.wolfbot.views.View;

public abstract class Wolf extends ARole {

    @Override
    public void init() {
        setName(text("wolf.name"));
        setDescription(text("wolf.description"));
    }

    @Override
    public void night() {
        if (userHasAttr(Attributes.DRUNK)) {
            View.wolfIsDrunk(selfuser);
            removeUserAttr(Attributes.DRUNK);
            return;
        }
        if (game.getRoleManager().aliveList(Wolf.class).size() > 1) {
            game.getVoteManager().requestVote(VoteTypes.WOLF, selfuser, 1);
        } else {
            game.getDefaultUserSelector().select(selfuser, Engine.NIGHT_TIMEOUT,
                    View::wolfKillAsk,
                    (selected) -> {
                        selfuser.setInHouse(false);
                        game.getActionManager().call(WolfKill.class, selfuser, selected);
                        finishNight();
            });
        }
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
