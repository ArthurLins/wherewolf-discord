package com.arthurl.wolfbot.game.engine.selections;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.actions.enums.Actions;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.util.ListManipulator;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import gnu.trove.map.hash.THashMap;

import static com.arthurl.wolfbot.game.engine.Engine.NIGHT_TIMEOUT;


public class WolfVoteSelector {

    //Voted user - qtd of votes
    private final THashMap<String, Integer> votes = new THashMap<>();
    //user id - multiplication
    //private final THashMap<String, Integer> waitingToVote = new THashMap<>();

    private final Game game;

    private boolean openToVote = false;

    public WolfVoteSelector(Game game) {
        this.game = game;
    }

    public void start() {
        if (openToVote) {
            return;
        }
        int qtdWolfs = game.getRoleManager().aliveList(Wolf.class, true).size();
        if (qtdWolfs <= 1){
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
            game.getRoleManager().aliveList(Wolf.class, true).forEach((wf)->{
                wf.sendMessageLang("A manda matou " + user.getUser().getAsMention());
            });
        } else {
            //if not have any votes exit the game (probably afk)
            if (game.getVoteSelector().qtdVotes() == 0){
                //View.inactivityForceEnd(game);
                //game.stop();
                return;
            }
        }
        //return getUserToKill();
    }

    private GameUser getUserToKill() {
        try {
            return game.getUserById(ListManipulator.getUniqueBigKey(votes));
        } catch (Exception e) {
            //game.getBroadcaster().send("Jogo finalizado por inatividade...");
            return null;
        }

    }

    public void requestVote(GameUser gameUser, int multiplication) {
        if (!openToVote) {
            return;
        }
        if (!gameUser.isAlive()) {
            return;
        }
        game.getDefaultUserSelector().select(gameUser, NIGHT_TIMEOUT,
                gameUser1 -> gameUser.sendMessage("Vote em quem você quer que os lobos ataquem:"), (selected) -> {
                    addVote(selected, multiplication);
                    gameUser.getRole().finishNight();
                });

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
