package com.arthurl.wolfbot.game.engine.selections;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.requests.IRequest;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DefaultOptionSelector {

    private Game game;

    public DefaultOptionSelector(Game game){
        this.game = game;
    }

    public void select(GameUser user, final int tout,
                       final Consumer<GameUser> ask,
                       final String[] options,
                       final BiConsumer<Integer, String> selected) {

        game.getRequestManager().dialog(user, new IRequest() {
            @Override
            public long timeout() {
                return tout;
            }

            @Override
            public void ask(GameUser user) {
                ask.accept(user);
                View.defaultOptionSelectorAsk(game, user, options);
            }

            @Override
            public void expired(GameUser user) {
                View.selectionExpired(game, user);
            }

            @Override
            public boolean response(MessageReceivedEvent e) {
                try{
                    final int response = Integer.parseInt(e.getMessage().getContentRaw());
                    selected.accept(response, options[(response - 1)]);
                    View.selected(game, user);
                    return true;
                } catch (Exception ex) {
                    View.invalidOption(user);
                    return false;
                }
            }
        });
    }
}
