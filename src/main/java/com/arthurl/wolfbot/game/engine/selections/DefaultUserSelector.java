package com.arthurl.wolfbot.game.engine.selections;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.requests.IRequest;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class DefaultUserSelector {

    private final THashMap<Integer, GameUser> users = new THashMap<>();
    private final Game game;

    public DefaultUserSelector(Game game) {
        this.game = game;
    }

    public void select(final GameUser user,
                       final int tout,
                       final Consumer<GameUser> ask,
                       final Consumer<GameUser> response) {
        this.select(user, tout,
                (choiceUser) -> user != choiceUser && choiceUser.isAlive(), ask, response);
    }

    public void select(final GameUser user,
                       final int tout,
                       final Predicate<GameUser> condition,
                       final Consumer<GameUser> ask,
                       final Consumer<GameUser> response) {
        ask.accept(user);
        game.getRequestManager().dialog(user, new IRequest() {
            @Override
            public long timeout() {
                return tout;
            }

            @Override
            public void ask(GameUser user) {
                buildVoteMap();
                View.defaultUserSelectorAsk(game, user, users, condition);
            }

            @Override
            public void expired(GameUser user) {
                View.selectionExpired(game, user);
            }

            @Override
            public boolean response(MessageReceivedEvent e) {
                try {
                    final int choice = Integer.parseInt(e.getMessage().getContentRaw());
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
        final THashMap<String, GameUser> gameUsers = game.getGameUsers();
        int index = 1;
        for (String key : gameUsers.keySet()) {
            users.put(index, gameUsers.get(key));
            index++;
        }
    }
}
