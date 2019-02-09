package com.arthurl.wolfbot.game;

import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.ConcurrentHashMap;


public class GameManager {

    private final ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();

    public void gameChatListener(MessageReceivedEvent event) {

        games.forEach((k, v) -> v.onMessageDiscordReceived(event));
    }

    public void create(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            return;
        }
        if (games.containsKey(event.getChannel().getId())) {
            return;
        }
        final Game game = new Game(event.getChannel(), event.getAuthor(), new GameSettings());
        games.put(event.getChannel().getId(), game);
    }

    public void startGame(String textChId) {
        if (!games.containsKey(textChId))
            return;
        games.get(textChId).start();
    }

    public void stopGame(Game game) {
        final String id = game.getMessageChannel().getId();
        if (!games.containsKey(id))
            return;

        game.stop();
        games.remove(id);
    }

    public void putUserInGame(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            return;
        }
        if (!games.containsKey(event.getTextChannel().getId()))
            return;
        games.get(event.getTextChannel().getId()).join(event.getAuthor());
    }

}
