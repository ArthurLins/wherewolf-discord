package com.arthurl.wolfbot.game;

import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class GameManager {

    private final THashMap<String, Game> games = new THashMap<>();

    public void gameChatListener(MessageReceivedEvent event) {

        games.forEach((k, v) -> v.onMessageDiscordReceived(event));
    }

    public synchronized void create(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            return;
        }
        if (games.containsKey(event.getChannel().getId())) {
            return;
        }
        final Game game = new Game(event.getChannel(),
                event.getAuthor(),
                41,
                2
        );
        games.put(event.getChannel().getId(), game);
    }

    public synchronized void startGame(String textChId) {
        if (!games.containsKey(textChId))
            return;
        games.get(textChId).start();
    }

    public synchronized void stopGame(String textChId) {
        if (!games.containsKey(textChId))
            return;
        games.remove(textChId);
    }

    public synchronized void stopGame(Game game) {
        String id = game.getMessageChannel().getId();
        if (!games.containsKey(id))
            return;
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
