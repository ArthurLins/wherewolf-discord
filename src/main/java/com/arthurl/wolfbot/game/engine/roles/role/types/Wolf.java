package com.arthurl.wolfbot.game.engine.roles.role.types;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.ARole;

public abstract class Wolf extends ARole {

    {
        name = "Lobo";
        description = "Lobo normal";

    }

    @Override
    public void night() {
        if (game.getRoleManager().aliveList(Wolf.class, true).size() > 1){
            game.getWolfVoteSelector().requestVote(selfuser, 1);
        } else {
            game.getDefaultUserSelector().select(selfuser, Engine.NIGHT_TIMEOUT,
                    (self) -> {
                        self.sendMessage("Escolha quem você quer matar: ");
                    },
                    (selected) -> {
                        selfuser.setInHouse(false);
                        game.getActionManager().call(Actions.WOLFKILL, selfuser, selected);
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
}
