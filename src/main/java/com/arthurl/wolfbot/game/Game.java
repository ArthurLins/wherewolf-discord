package com.arthurl.wolfbot.game;

import com.arthurl.wolfbot.Bootstrap;
import com.arthurl.wolfbot.game.engine.Engine;
import com.arthurl.wolfbot.game.engine.actions.ActionManager;
import com.arthurl.wolfbot.game.engine.requests.RequestManager;
import com.arthurl.wolfbot.game.engine.roles.ARole;
import com.arthurl.wolfbot.game.engine.roles.RoleManager;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.selections.DefaultOptionSelector;
import com.arthurl.wolfbot.game.engine.selections.DefaultUserSelector;
import com.arthurl.wolfbot.game.engine.text.Broadcaster;
import com.arthurl.wolfbot.game.engine.text.Lang;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.game.engine.votes.VoteManager;
import com.arthurl.wolfbot.views.View;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.function.Consumer;

public class Game {

    private static final int MIN_USERS_START = 120000;

    private final MessageChannel messageChannel;
    private final Engine engine;
    private final Lang lang;
    private final User creator;
    private final Broadcaster broadcaster;
    private final RequestManager requestManager;
    private final ActionManager actionManager;
    private final RoleManager roleManager;
    private final DefaultUserSelector defaultUserSelector;
    private final DefaultOptionSelector defaultOptionSelector;
    private final VoteManager voteManager;
    private final GameSettings gameSettings;
    private THashMap<String, GameUser> gameUsers = new THashMap<>();
    private volatile boolean started = false;


    public Game(final MessageChannel mainChannel, final User creator, GameSettings settings) {
        this.messageChannel = mainChannel;
        this.creator = creator;
        this.gameSettings = settings;
        this.lang = new Lang(settings.getLang());
        this.engine = new Engine(this);
        this.broadcaster = new Broadcaster(mainChannel, this);
        this.actionManager = new ActionManager(this);
        this.voteManager = new VoteManager(this);
        this.requestManager = new RequestManager(this);
        this.roleManager = new RoleManager(this);
        this.defaultUserSelector = new DefaultUserSelector(this);
        this.defaultOptionSelector = new DefaultOptionSelector(this);

        //
        View.gameInit(this);
    }


    public void start() {
        if (started) {
            return;
        }
        if (gameUsers.size() < gameSettings.getMinUsers()) {
            View.insufficientUsersToStart(this);
            return;
        }
        started = true;
        View.initGame(this);
        this.getRoleManager().assignRoles();
        Bootstrap.getThreadPool().run(engine::startCycle, 5000);
    }

    public void stop() {
        if (!started)
            return;
        started = false;
        System.out.println("GAME STOPED");
    }

    public void join(User user) {
        if (started) {
            return;
        }
        if (gameSettings.getMaxUsers() <= gameUsers.size()) {
            View.gameFull(this, user);
            return;
        }
        if (gameUsers.containsKey(user.getId())) {
            View.userInGame(this, user);
            return;
        }

        //
        gameUsers.put(user.getId(), new GameUser(user, this));
        View.userJoin(this, user);
        //

    }

    public void hasWinner(Consumer<Class<? extends ARole>> winRole, Runnable continueGame) {
        if (!isStarted()) {
            return;
        }

        if (getRoleManager().roleWin(Civilian.class, Wolf.class)) {
            winRole.accept(Civilian.class);
            return;
        } else if (getRoleManager().roleWin(Wolf.class, Civilian.class)) {
            winRole.accept(Wolf.class);
            return;
        }
        continueGame.run();
    }

    public GameUser getUserById(String id) {
        if (!gameUsers.containsKey(id)) {
            return null;
        }
        return gameUsers.get(id);
    }

    public void onMessageDiscordReceived(MessageReceivedEvent event) {
        if (!isStarted()) {
            return;
        }
        if (gameUsers.containsKey(event.getAuthor().getId())) {
            if (!gameUsers.get(event.getAuthor().getId()).isAlive()){
                if (event.getChannelType() == ChannelType.PRIVATE){
                    return;
                }
                event.getMessage().delete().queue();
            }
        }
        requestManager.chatRequestListener(event);
    }

    public THashMap<String, GameUser> getGameUsers() {
        return gameUsers;
    }


    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public Engine getEngine() {
        return engine;
    }


    public RoleManager getRoleManager() {
        return roleManager;
    }

    public Lang getLang() {
        return lang;
    }

    public DefaultOptionSelector getDefaultOptionSelector() {
        return defaultOptionSelector;
    }

    public GameSettings getSettings() {
        return gameSettings;
    }

    public User getCreator() {
        return creator;
    }

    public DefaultUserSelector getDefaultUserSelector() {
        return defaultUserSelector;
    }

    public MessageChannel getMessageChannel() {
        return messageChannel;
    }

    public boolean isStarted() {
        return started;
    }

    public VoteManager getVoteManager() {
        return voteManager;
    }
}
