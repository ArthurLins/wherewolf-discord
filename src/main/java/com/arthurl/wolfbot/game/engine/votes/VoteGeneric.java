package com.arthurl.wolfbot.game.engine.votes;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.game.engine.util.ListManipulator;
import com.arthurl.wolfbot.views.View;
import gnu.trove.map.hash.THashMap;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

class VoteGeneric {
    //Voted user - qtd of votes
    private final THashMap<String, Integer> votes = new THashMap<>();

    private final Game game;
    private final int timeout;
    private final Consumer<GameUser> ask;
    private final BiConsumer<GameUser, GameUser> onSelect;
    private volatile boolean openToVote = false;

    VoteGeneric(final Game game,
                final int timeout,
                final Consumer<GameUser> ask,
                final BiConsumer<GameUser, GameUser> onSelect) {
        this.game = game;
        this.ask = ask;
        this.onSelect = onSelect;
        this.timeout = timeout;
    }


    void start() {
        if (openToVote) {
            return;
        }
        votes.clear();
        openToVote = true;
    }

    void stop(Consumer<GameUser> success, Runnable noWin, Runnable error) {
        System.out.println("ss");
        if (!openToVote) {
            return;
        }
        openToVote = false;
        final GameUser user = getWinUser();
        //if {
        if (user != null){
            success.accept(user);
        }
        //if not have any votes exit the game (probably afk)
        if (this.qtdVotes() == 0) {
            error.run();
            return;
        }
        //Tie
        if (user == null){
            noWin.run();
        }
        //}

    }

    private GameUser getWinUser() {
        return game.getUserById(ListManipulator.getUniqueBigKey(votes));
    }

    void request(final GameUser gameUser, final int multiplication) {
        if (!openToVote) {
            return;
        }
        game.getDefaultUserSelector().select(gameUser, timeout,
                ask,
                (selected) -> {
                    addVote(selected, gameUser, multiplication);
                    onSelect.accept(gameUser, selected);
                }
        );
    }


    private int qtdVotes() {
        return votes.size();
    }

    private void addVote(final GameUser user, GameUser voted, final int qtd) {
        if (!votes.containsKey(user.getUser().getId())) {
            votes.put(user.getUser().getId(), 1);
        } else {
            int qtdVote = votes.get(user.getUser().getId());
            votes.replace(user.getUser().getId(), qtdVote, qtdVote + qtd);
        }
        View.userVotedIn(user, voted);
        user.getGame().getEngine().fireVoteOK(voted);

    }
}
