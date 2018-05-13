package com.arthurl.wolfbot.game.engine.actions.action;

import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.users.GameUser;

public class HarlotVisitAction extends AAction {

    {
        name = "visit";
        description = "visit user";
        pattern = new Class[]{GameUser.class, GameUser.class};
        priority = ActionPriority.ASYNC;
    }

    @Override
    public void execute() {
        final GameUser harlot = (GameUser) objects[0];
        final GameUser visited = (GameUser) objects[1];
        if (visited.inHouse()){
            harlot.setInHouse(false);
            if (visited.getRole().getClass().getSuperclass() == Wolf.class){
                harlot.kill();
                harlot.sendMessage("Você {{1}} que é um lobo, e morreu!");
                return;
            }
            visited.getRelated().add(harlot);
            harlot.sendMessage("Você visitou {{1}} e descobriu que ele é {{2}}");
            visited.sendMessage("Você recebeu uma visita de {{1}} que é uma porstituta!");
        }

    }
}
