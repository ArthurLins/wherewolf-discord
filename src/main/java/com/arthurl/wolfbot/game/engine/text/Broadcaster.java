package com.arthurl.wolfbot.game.engine.text;

import com.arthurl.wolfbot.game.Game;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class Broadcaster {


    private MessageChannel broadcasterChannel;
    private Game game;


    public Broadcaster(MessageChannel tc, Game game) {
        this.broadcasterChannel = tc;
        this.game = game;
    }

    public void send(String msg) {
        if (broadcasterChannel == null) {
            return;
        }
        broadcasterChannel.sendMessage(msg).queue();
    }

    public void send(MessageEmbed embed) {
        if (broadcasterChannel == null) {
            return;
        }
        broadcasterChannel.sendMessage(embed).queue();
    }

    public void sendLang(String msg) {
        send(game.getLang().get(msg));
    }
    public void sendLang(String msg, String... strings) {
        send(game.getLang().get(msg, strings));
    }

    public MessageAction sendF(MessageEmbed me) {
        return broadcasterChannel.sendMessage(me);
    }

    public MessageAction sendF(String me) {
        return broadcasterChannel.sendMessage(me);
    }
}
