package com.arthurl.wolfbot.game.engine;

import com.arthurl.wolfbot.Bootstrap;
import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;
import gnu.trove.map.hash.THashMap;

import java.util.concurrent.atomic.AtomicInteger;

public class Engine {

    public static int DAY_TIMEOUT = 60000;   //1 sec
    public static int NIGHT_TIMEOUT = 60000; //1 sec
    public static int VOTE_TIMEOUT = 60000;  //1 sec

    private volatile AtomicInteger cycleCount = new AtomicInteger();

    private boolean waiting_night = false;
    private boolean waiting_vote = false;

    private final THashMap<String, GameUser> padding = new THashMap<>();

    private final Game game;

    public Engine(final Game game, final int day_timeout, final int night_timeout, final int vote_timeout) {
        this.game = game;
        Engine.DAY_TIMEOUT = day_timeout;
        Engine.NIGHT_TIMEOUT = night_timeout;
        Engine.VOTE_TIMEOUT = vote_timeout;
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
        View.gameNight(game);
        waiting_night = true;
        game.getWolfVoteSelector().start();
        game.getGameUsers().forEach((key, user) -> {
            if (user.isAlive()) {
                padding.put(key, user);
                Bootstrap.getThreadPool().run(() -> user.getRole().night());
            }
        });
        Bootstrap.getThreadPool().run(this::nightOK, NIGHT_TIMEOUT);
        cycleCount.incrementAndGet();
    }

    private void vote() {
        if (!game.isStarted()){return;}
        padding.clear();
        View.gameVote(game);
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
