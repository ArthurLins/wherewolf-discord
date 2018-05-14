package com.arthurl.wolfbot.game.engine;

import com.arthurl.wolfbot.Bootstrap;
import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.actions.action.LynchKill;
import com.arthurl.wolfbot.game.engine.actions.action.WolfKill;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.game.engine.votes.VoteTypes;
import com.arthurl.wolfbot.views.View;
import gnu.trove.map.hash.THashMap;

import java.util.concurrent.atomic.AtomicInteger;

public class Engine {

    public static int DAY_TIMEOUT = 60000;   //1 sec
    public static int NIGHT_TIMEOUT = 60000; //1 sec
    public static int VOTE_TIMEOUT = 60000;  //1 sec

    private boolean waiting_night = false;
    private boolean waiting_vote = false;

    private final Game game;

    private final THashMap<String, GameUser> padding = new THashMap<>();
    private volatile AtomicInteger cycleCount = new AtomicInteger(1);

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
        View.cycleSeparator(game, cycleCount);
        View.gameNight(game);
        padding.clear();
        waiting_night = true;
        game.getVoteManager().wolfVoteStart();
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
        View.gameVote(game);
        padding.clear();
        game.getVoteManager().defaultVoteStart();
        game.getGameUsers().forEach((key, user) -> {
            if (user.isAlive()) {
                padding.put(key, user);
                Bootstrap.getThreadPool().run(() -> user.getRole().vote());
            }
        });
        Bootstrap.getThreadPool().run(this::voteOK, VOTE_TIMEOUT);
    }

    public void fireVoteOK(GameUser gameUser) {
        if (!padding.containsKey(gameUser.getUser().getId())) {
            return;
        }
        padding.remove(gameUser.getUser().getId());
        if (padding.isEmpty()) {
            voteOK();
        }
    }

    public void fireNightOK(GameUser gameUser) {
        if (!padding.containsKey(gameUser.getUser().getId())) {
            return;
        }
        padding.remove(gameUser.getUser().getId());
        if (padding.isEmpty()) {
            nightOK();
        }
    }

    public void fireActionsExecuted() {
        checkWin(() -> {
            View.nightResume(game);
            day();
        });
    }

    private void voteOK() {
        game.getVoteManager().stop(
                VoteTypes.DEFAULT,
                (user) -> {
                    game.getActionManager().call(LynchKill.class, user);
                    game.getBroadcaster().send("Votação encerrada");
                    checkWin(this::night);
                },
                () -> View.gameVoteTied(game),
                () -> {
                    View.inactivityForceEnd(game);
                    Bootstrap.getGameManager().stopGame(game);
                }
        );
    }

    private void dayOK() {
        View.gameDayEnd(game);
        vote();
    }

    private void nightOK() {
        if (!waiting_night) {
            return;
        }
        game.getVoteManager().stop(
                VoteTypes.WOLF,
                (win) -> game.getActionManager().call(WolfKill.class, win),
                () -> game.getRoleManager().aliveList(Wolf.class).forEach(View::wolfVoteTied),
                () -> game.getRoleManager().aliveList(Wolf.class).forEach(View::wolfVoteTied)
        );
        waiting_night = false;
        game.getActionManager().execute();
    }

    private void checkWin(Runnable no) {
        game.hasWinner(
                (winRole) -> {
                    if (winRole == Civilian.class) {
                        View.civiliansWins(game);
                    } else if (winRole == Wolf.class) {
                        View.wolfsWins(game);
                    }
                    View.gameEnd(game);
                    Bootstrap.getGameManager().stopGame(game);
                }, no
        );
    }

}
