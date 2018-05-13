package com.arthurl.wolfbot.game.engine;

import com.arthurl.wolfbot.Bootstrap;
import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;
import gnu.trove.map.hash.THashMap;

public class Engine {

    public static final int DAY_TIMEOUT = 60000;   //1 sec
    public static final int NIGHT_TIMEOUT = 60000; //1 sec
    public static final int VOTE_TIMEOUT = 60000;  //1 sec

    private boolean waiting_night = false;
    private boolean waiting_vote = false;

    private THashMap<String, GameUser> padding = new THashMap<>();

    private Game game;

    public Engine(Game game) {
        this.game = game;
    }

    public void startCycle() {
        if (!game.isStarted()){return;}
        night();
    }

    private void day() {
        if (!game.isStarted()){return;}
        View.gameDay(game);
        game.getGameUsers().forEach((key, user) -> {
            if (user.isAlive()) {
                padding.put(key, user);
                Bootstrap.getThreadPool().run(() -> user.getRole().day());
            }
        });
        Bootstrap.getThreadPool().run(this::dayOK, DAY_TIMEOUT);
    }

    private void night() {
        if (!game.isStarted()){return;}
        padding.clear();
        game.getBroadcaster().send(game.getLang().get("game.night"));
        waiting_night = true;
        game.getWolfVoteSelector().start();
        game.getGameUsers().forEach((key, user) -> {
            if (user.isAlive()) {
                padding.put(key, user);
                Bootstrap.getThreadPool().run(() -> user.getRole().night());
            }
        });
        Bootstrap.getThreadPool().run(this::nightOK, NIGHT_TIMEOUT);
    }

    private void vote() {
        if (!game.isStarted()){return;}
        padding.clear();
        game.getBroadcaster().send(game.getLang().get("game.vote"));
        game.getVoteSelector().start();
        waiting_vote = true;
        game.getGameUsers().forEach((key, user) -> {
            if (user.isAlive()) {
                padding.put(key, user);
                Bootstrap.getThreadPool().run(() -> user.getRole().vote());
            }
        });
        Bootstrap.getThreadPool().run(this::voteOK, VOTE_TIMEOUT);
    }

    public void fireVoteOK(GameUser gameUser) {
        if (!game.isStarted()){return;}
        if (!padding.containsKey(gameUser.getUser().getId())) {
            return;
        }
        padding.remove(gameUser.getUser().getId());
        if (padding.isEmpty()) {
            voteOK();
        }
    }

    public void fireNightOK(GameUser gameUser) {
        if (!game.isStarted()){return;}
        if (!padding.containsKey(gameUser.getUser().getId())) {
            return;
        }
        padding.remove(gameUser.getUser().getId());
        if (padding.isEmpty()) {
            nightOK();
        }
    }


    public void fireActionsExecuted() {
        if (!game.isStarted()){return;}
        if (!game.hasWinner()) {
            View.nightResume(game);
            day();
        }
    }

    private void voteOK() {
        if (!game.isStarted()){return;}
        if (!waiting_vote) {
            return;
        }
        if (!game.hasWinner()) {
            waiting_vote = false;
            game.getVoteSelector().stop();
            night();
        }
    }

    private void dayOK() {
        if (!game.isStarted()){return;}
        View.gameDayEnd(game);
        vote();
    }

    private void nightOK() {
        if (!game.isStarted()){return;}
        if (!waiting_night) {
            return;
        }
        waiting_night = false;
        game.getActionManager().execute();
    }

}
