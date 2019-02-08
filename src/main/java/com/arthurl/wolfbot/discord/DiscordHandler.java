package com.arthurl.wolfbot.discord;

import com.arthurl.wolfbot.Bootstrap;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordHandler extends ListenerAdapter{

    @Override
    public void onReady(ReadyEvent event) {
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()){
            return;
        }
        Bootstrap.getGameManager().gameChatListener(event);
        final String message = event.getMessage().getContentRaw().toLowerCase();
        if (message.equals("!create") && event.getMember().hasPermission(Permission.MESSAGE_WRITE)) {
            Bootstrap.getGameManager().create(event);
            return;
        }
        if (event.getMessage().getContentRaw().equals("!start") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            Bootstrap.getGameManager().startGame(event.getChannel().getId());
            return;
        }
        if (event.getMessage().getContentRaw().equals("!join") && event.getMember().hasPermission(Permission.MESSAGE_WRITE)) {
            Bootstrap.getGameManager().putUserInGame(event);
        }
    }
}
