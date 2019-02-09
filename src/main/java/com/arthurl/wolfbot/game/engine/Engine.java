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

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Engine {

    private AtomicBoolean waiting = new AtomicBoolean(false);

    private final Game game;

    private ScheduledFuture cancellableTask;

    private final THashMap<String, GameUser> padding = new THashMap<>();
    private volatile AtomicInteger cycleCount = new AtomicInteger(1);

    public Engine(final Game game) {
        this.game = game;
    }

    public void startCycle() {
        if (!game.isStarted()){return;}
        night();
    }

    private void day() {
        if (waiting.get()){
            return;
        }
        waiting.set(true);

        View.gameDay(game);
        padding.clear();
        game.getGameUsers().forEach((key, user) -> {
            if (user.isAlive()) {
                padding.put(key, user);
                Bootstrap.getThreadPool().run(() -> user.getRole().day());
            }
        });
        Bootstrap.getThreadPool().run(this::dayOK, game.getSettings().getDayTime());
        //cycler.append(this::day, DAY_TIMEOUT);
    }

    private void night() {
        waiting.set(true);
        View.cycleSeparator(game, cycleCount);
        View.gameNight(game);
        padding.clear();
        if (game.getRoleManager().aliveList(Wolf.class).size() > 1){
            game.getVoteManager().wolfVoteStart();
        }
        game.getGameUsers().forEach((key, user) -> {
            if (user.isAlive()) {
                padding.put(key, user);
                Bootstrap.getThreadPool().run(() -> user.getRole().night());
            }
        });
        Bootstrap.getThreadPool().run(this::nightOK, game.getSettings().getNightTime());
        //cycler.append(this::nightOK, NIGHT_TIMEOUT);
        cycleCount.incrementAndGet();
    }

    private void vote() {
        if (waiting.get()){
            return;
        }
        waiting.set(true);

        View.gameVote(game);
        padding.clear();
        game.getVoteManager().defaultVoteStart();
        game.getGameUsers().forEach((key, user) -> {
            if (user.isAlive()) {
                padding.put(key, user);
                Bootstrap.getThreadPool().run(() -> user.getRole().vote());
            }
        });
        cancellableTask = Bootstrap.getThreadPool().run(this::voteOK, game.getSettings().getVoteTime());
    }

    public void fireVoteOK(GameUser gameUser) {
        if (!padding.containsKey(gameUser.getUser().getId())) {
            return;
        }
        padding.remove(gameUser.getUser().getId());
        System.out.println("User voted  | " + padding.size());
        if (padding.isEmpty()) {
            cancellableTask.cancel(false);
            voteOK();
        }
    }


    private void voteOK() {
        if (!waiting.get()){
            return;
        }
        waiting.set(false);
        game.getVoteManager().stop(
            VoteTypes.DEFAULT,
            (user) -> {
                game.getActionManager().call(LynchKill.class, user);
                game.getBroadcaster().send("Votação encerrada");
                checkWin(this::night);
            },
            () -> {
                View.gameVoteTied(game);
                checkWin(this::night);
            },
            () -> {
                View.inactivityForceEnd(game);
                Bootstrap.getGameManager().stopGame(game);
            }
        );
    }


    public void fireNightOK(GameUser gameUser) {
        if (!padding.containsKey(gameUser.getUser().getId())) {
            return;
        }
        padding.remove(gameUser.getUser().getId());
    }

    public void fireActionsExecuted() {
        checkWin(() -> {
            View.nightResume(game);
            day();
        });
    }

    private void dayOK() {
        if (!waiting.get()){
            return;
        }
        waiting.set(false);
        View.gameDayEnd(game);
        vote();
    }

    private void nightOK() {
        if (!waiting.get()){
            return;
        }
        waiting.set(false);
        game.getVoteManager().stop(
                VoteTypes.WOLF,
                (win) -> game.getActionManager().call(WolfKill.class, win),
                () -> game.getRoleManager().aliveList(Wolf.class).forEach(View::wolfVoteTied),
                () -> game.getRoleManager().aliveList(Wolf.class).forEach(View::wolfVoteTied)
        );
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
