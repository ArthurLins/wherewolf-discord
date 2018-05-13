package com.arthurl.wolfbot.game.engine.requests;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RequestManager {

    private final Game game;
    private final THashMap<String, ExpectedAnswer> waitingAnswers = new THashMap<>();

    public RequestManager(Game game) {
        this.game = game;
    }

    public void dialog(GameUser user, IRequest iRequest) {
        dialog(user, iRequest, true);
    }

    private void dialog(GameUser user, IRequest iRequest, boolean add) {
        if (add)
            addWaiting(user, iRequest);
        iRequest.ask(user);
    }

    public void chatRequestListener(MessageReceivedEvent e) {
        if (!waitingAnswers.containsKey(e.getAuthor().getId())) {
            return;
        }
        final ExpectedAnswer ea = waitingAnswers.get(e.getAuthor().getId());
        final GameUser gameUser = game.getUserById(e.getAuthor().getId());
        if (expired(e.getAuthor())) {
            waitingAnswers.remove(e.getAuthor().getId());
            ea.request.expired(gameUser);
            return;
        }
        if (!ea.request.response(e)) {
            dialog(gameUser, ea.request, false);
            return;
        }
        removeWaiting(e.getAuthor());

    }

    private void addWaiting(GameUser user, IRequest request) {
        if (waitingAnswers.containsKey(user.getUser().getId())) {
            waitingAnswers.replace(user.getUser().getId(),
                    new ExpectedAnswer(System.currentTimeMillis(), request));
        }
        waitingAnswers.put(user.getUser().getId(),
                new ExpectedAnswer(System.currentTimeMillis(), request));
    }

    private void removeWaiting(User user) {
        if (!waitingAnswers.containsKey(user.getId()))
            return;
        waitingAnswers.remove(user.getId());
    }

    private boolean expired(User user) {
        if (!waitingAnswers.containsKey(user.getId())) {
            return true;
        }
        final ExpectedAnswer ea = waitingAnswers.get(user.getId());
        final long millis = ea.request_time;
        return (millis + ea.request.timeout()) < System.currentTimeMillis();
    }
}
