package com.arthurl.wolfbot.game.engine.roles.role;

import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.action.DectetiveExpose;
import com.arthurl.wolfbot.game.engine.actions.action.SeeUser;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.util.Randomize;

public class Dectetive extends Civilian {

    @Override
    public void init() {
        setName("Detetive");
        setDescription("Ve quem é quem...");
    }

    @Override
    public void day(){
        userSelector(
                (ask) -> ask.sendMessage("Escolha uma pessoa para investigar"),
                (selected)->{
                    action(SeeUser.class, selfuser, selected);
                    Randomize.propabilityCall(40, ()->
                            action(DectetiveExpose.class, selfuser));
                },
                Engine.NIGHT_TIMEOUT
        );
    }
}
