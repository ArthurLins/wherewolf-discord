package com.arthurl.wolfbot.game.engine.votes;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;
import gnu.trove.map.hash.THashMap;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class VoteManager {

    private final Game game;
    private final THashMap<VoteTypes, VoteGeneric> votePools = new THashMap<>();

    public VoteManager(Game game) {
        this.game = game;
    }

    private void start(final VoteTypes type,
                       final int tout,
                       final Consumer<GameUser> ask,
                       final BiConsumer<GameUser, GameUser> onSelect) {
        if (votePools.containsKey(type)) {
            votePools.get(type).start();
            return;
        }
        final VoteGeneric voteGeneric = new VoteGeneric(game, tout, ask, onSelect);
        votePools.put(type, voteGeneric);
        voteGeneric.start();
    }

    public void requestVote(VoteTypes types, GameUser user, int mult) {
        if (!votePools.containsKey(types)) {
            return;
        }
        votePools.get(types).request(user, mult);
    }

    public void wolfVoteStart() {
        this.start(VoteTypes.WOLF, Engine.NIGHT_TIMEOUT, View::wolfVoteAsk, View::wolfVoteSelect);
    }


    public void defaultVoteStart() {
        this.start(VoteTypes.DEFAULT, Engine.VOTE_TIMEOUT, View::defaultVoteAsk, View::defaultVoteSelection);
    }

    public void stop(VoteTypes type, Consumer<GameUser> winner, Runnable noWin, Runnable error) {
        if (votePools.containsKey(type)) {
            votePools.get(type).stop(winner, noWin, error);
            votePools.remove(type);
        }
    }
}
