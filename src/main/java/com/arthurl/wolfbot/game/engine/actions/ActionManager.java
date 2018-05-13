package com.arthurl.wolfbot.game.engine.actions;

import com.arthurl.wolfbot.Bootstrap;
import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.actions.action.*;
import com.arthurl.wolfbot.game.engine.actions.enums.ActionPriority;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import gnu.trove.map.hash.THashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionManager {

    private final List<AAction> lowPriorityActions = Collections.synchronizedList(new ArrayList<>());
    private final List<AAction> mediumPriorityActions = Collections.synchronizedList(new ArrayList<>());
    private final List<AAction> highPriorityActions = Collections.synchronizedList(new ArrayList<>());
    private THashMap<Actions, Class<? extends AAction>> actions = new THashMap<>();
    private Game game;

    public ActionManager(Game game) {
        this.game = game;
        registerAction(Actions.KILL, GallowKillAction.class);
        registerAction(Actions.WOLFKILL, WolfKillAction.class);
        registerAction(Actions.HUNTER_KILL, HunterKillAction.class);
        registerAction(Actions.SEER_USER, SeerUserAction.class);
        registerAction(Actions.PREFECT_SHOW, PrefectShowAction.class);
        registerAction(Actions.HARLOT_VISIT, HarlotVisitAction.class);
    }

    private void registerAction(Actions key, Class<? extends AAction> actionClass) {
        if (actions.containsKey(key)) {
            return;
        }
        actions.put(key, actionClass);
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
            return;
        }
        aAction.execute();
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

    public void call(Actions actionKey, Object... objects) {
        if (!actions.containsKey(actionKey)) {
            return;
        }
        try {
            final AAction action = actions.get(actionKey).newInstance();
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
