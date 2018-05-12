package com.arthurl.wolfbot.game.engine.selections;

import com.arthurl.wolfbot.Views.View;
import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.requests.IRequest;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DefaultOptionSelector {

    private Game game;

    public DefaultOptionSelector(Game game){
        this.game = game;
    }

    public void select(GameUser user, int tout, Consumer<GameUser> ask ,String[] options, BiConsumer<Integer,String> selected){
        game.getRequestManager().dialog(user, new IRequest() {
            @Override
            public long timeout() {
                return tout;
            }

            @Override
            public void ask(GameUser user) {
                ask.accept(user);
                View.genericOptionAsk(game, user, options);
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
                    return true;
                } catch (Exception ex) {
                    View.invalidOption(user);
                    return false;
                }
            }
        });
    }
}
