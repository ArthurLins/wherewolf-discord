package com.arthurl.wolfbot.game.engine.requests;

import com.arthurl.wolfbot.game.engine.users.GameUser;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface IRequest {

    long timeout();

    void ask(GameUser user);

    void expired(GameUser user);

    boolean response(MessageReceivedEvent e);
}
