package com.arthurl.wolfbot.discord;

import com.arthurl.wolfbot.Bootstrap;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordHandler extends ListenerAdapter{

    @Override
    public void onReady(ReadyEvent event) {
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Bootstrap.getGameManager().gameChatListener(event);
        if (event.getMessage().getContentRaw().equals("!create")){
            Bootstrap.getGameManager().create(event);
            return;
        }
        if (event.getMessage().getContentRaw().equals("!start")){
            Bootstrap.getGameManager().startGame(event.getChannel().getId());
        }
        if (event.getMessage().getContentRaw().equals("!join")){
            Bootstrap.getGameManager().putUserInGame(event);
        }
    }
}
