package com.arthurl.wolfbot.game.engine.selections;

import com.arthurl.wolfbot.Bootstrap;
import com.arthurl.wolfbot.Views.View;
import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.util.ListManipulator;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import gnu.trove.map.hash.THashMap;

import static com.arthurl.wolfbot.game.engine.Engine.VOTE_TIMEOUT;


public class VoteSelector {

    //Voted user - qtd of votes
    private final THashMap<String, Integer> votes = new THashMap<>();
    //user id - multiplication
    //private final THashMap<String, Integer> waitingToVote = new THashMap<>();

    private final Game game;

    private boolean openToVote = false;

    public VoteSelector(Game game) {
        this.game = game;
    }

    public void start() {
        if (openToVote) {
            return;
        }
        votes.clear();
        openToVote = true;
    }

    public void stop() {
        openToVote = false;
        GameUser user = getUserToKill();
        if (user != null) {
            game.getActionManager().call(Actions.KILL, user);
            game.getBroadcaster().send("Votação encerrada");
        } else {
            //if not have any votes exit the game (probably afk)
            if (game.getVoteSelector().qtdVotes() == 0){
                View.inactivityForceEnd(game);
                Bootstrap.getGameManager().stopGame(game);
                return;
            }
            game.getBroadcaster().send("Votação empatada... ng morre");
        }

    }

    private GameUser getUserToKill() {
        return game.getUserById(ListManipulator.getUniqueBigKey(votes));
    }

    public void requestVote(GameUser gameUser, int multiplication) {
        if (!openToVote) {
            return;
        }
        if (!gameUser.isAlive()) {
            gameUser.getRole().finishVote();
            return;
        }
        game.getDefaultUserSelector().select(gameUser, VOTE_TIMEOUT,
                gameUser1 -> gameUser1.sendMessage("Vote em quem você quer levar pra forca:"), (selected) -> {
            game.getBroadcaster().send(gameUser.getUser().getAsMention() + " Votou para matar: "
                    + selected.getUser().getAsMention());
            addVote(selected, multiplication);
            gameUser.getRole().finishVote();
        });

    }


    public int qtdVotes(){
        return votes.size();
    }

    private void addVote(GameUser user, int qtd) {

        if (!votes.containsKey(user.getUser().getId())) {
            votes.put(user.getUser().getId(), 1);
            return;
        }
        int qtdVote = votes.get(user.getUser().getId());
        votes.replace(user.getUser().getId(), qtdVote, qtdVote + qtd);

    }

}
