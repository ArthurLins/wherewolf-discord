package com.arthurl.wolfbot.game.engine.selections;

import com.arthurl.wolfbot.Views.View;
import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.requests.IRequest;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DefaultUserSelector {

    private THashMap<Integer, GameUser> users = new THashMap<>();
    private Game game;

    public DefaultUserSelector(Game game) {
        this.game = game;
    }

    public void select(GameUser user, int tout, Consumer<GameUser> ask, Consumer<GameUser> response){
        this.select(user, tout, (choiceUser)-> {
            if (user == choiceUser) {
                return false;
            }
            if (!choiceUser.isAlive()) {
                return false;
            }
            return true;
        }, ask, response);
    }

    public void select(GameUser user, int tout, Predicate<GameUser> condition,
                       Consumer<GameUser> ask, Consumer<GameUser> response) {
        ask.accept(user);
        game.getRequestManager().dialog(user, new IRequest() {
            @Override
            public long timeout() {
                return tout;
            }

            @Override
            public void ask(GameUser user) {
                buildVoteMap();
                View.defaultSelector(user, users, condition);
            }

            @Override
            public void expired(GameUser user) {
                View.selectionExpired(game, user);
            }

            @Override
            public boolean response(MessageReceivedEvent e) {
                try {
                    Integer choice = Integer.parseInt(e.getMessage().getContentRaw());
                    GameUser choiceUser = users.get(choice);
                    if (choiceUser == null) {
                        View.userNotFound(game, user);
                        return false;
                    }
                    if (!condition.test(choiceUser)){
                        View.invalidOption(user);
                        return false;
                    }
                    View.selected(game, user);
                    response.accept(choiceUser);
                    return true;
                } catch (NumberFormatException ex) {
                    View.invalidOption(user);
                    return false;
                }
            }
        });
    }

    private void buildVoteMap() {
        THashMap<String, GameUser> gameUsers = game.getGameUsers();
        int index = 1;
        for (String key : gameUsers.keySet()) {
            users.put(index, gameUsers.get(key));
            index++;
        }
    }
}
