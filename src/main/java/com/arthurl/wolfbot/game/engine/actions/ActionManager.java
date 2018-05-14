package com.arthurl.wolfbot.game.engine.actions;

import com.arthurl.wolfbot.Bootstrap;
import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionManager {

    private final List<AAction> lowPriorityActions = Collections.synchronizedList(new ArrayList<>());
    private final List<AAction> mediumPriorityActions = Collections.synchronizedList(new ArrayList<>());
    private final List<AAction> highPriorityActions = Collections.synchronizedList(new ArrayList<>());
    private Game game;

    public ActionManager(Game game) {
        this.game = game;
    }

    private synchronized void schedule(AAction aAction) {
        if (aAction.priority == ActionPriority.LOW) {
            if (lowPriorityActions.contains(aAction)) {
                return;
            }
            lowPriorityActions.add(aAction);
            return;
        }
        if (aAction.priority == ActionPriority.MEDIUM) {
            if (mediumPriorityActions.contains(aAction)) {
                return;
            }
            mediumPriorityActions.add(aAction);
            return;
        }
        if (aAction.priority == ActionPriority.HIGH) {
            if (highPriorityActions.contains(aAction)) {
                return;
            }
            highPriorityActions.add(aAction);
            return;
        }
        if (aAction.priority == ActionPriority.ASYNC) {
            Bootstrap.getThreadPool().run(aAction::execute);
        }
    }

    public synchronized void execute() {
        for (AAction action : highPriorityActions) {
            action.execute();
        }
        for (AAction action : mediumPriorityActions) {
            action.execute();
        }
        for (AAction action : lowPriorityActions) {
            action.execute();
        }
        lowPriorityActions.clear();
        mediumPriorityActions.clear();
        highPriorityActions.clear();
        game.getEngine().fireActionsExecuted();
    }

    public void call(Class<? extends AAction> actionClass, Object... objects) {

        try {
            final AAction action = actionClass.newInstance();
            //Pattern validation (if needs params)
            if (action.pattern != null) {
                if (action.pattern.length != objects.length) {
                    System.out.println("[ACTION][ERROR]: PATTERN NOT MATCH IN SIZE");
                    return;
                }
                int pos = 0;
                for (Class cl : action.pattern) {
                    if (objects[pos].getClass() != cl) {
                        System.out.println("[ACTION][ERROR]: PATTERN NOT MATCH IN TYPES");
                        return;
                    }
                    pos++;
                }
                action.game = game;
                action.objects = objects;
                schedule(action);
                return;
            }
            schedule(action);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
