package com.arthurl.wolfbot.game.engine.users;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.roles.ARole;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameUser {
    private final User user;
    private final Game game;
    private final List<GameUser> related = new ArrayList<>();
    private final List<GameUser> roleKnows = new ArrayList<>();
    private final Map<Attributes, Object> attr = new THashMap<>();
    private volatile boolean inHouse = true;
    private volatile boolean alive = true;
    private volatile boolean hidden = true;
    private volatile ARole role = null;

    public GameUser(User user, Game game) {
        this.user = user;
        this.game = game;
    }

    public void sendMessage(String msg) {
        user.openPrivateChannel().queue(s -> s.sendMessage(msg).queue());
    }

    public void sendMessage(MessageEmbed msg) {
        user.openPrivateChannel().queue(s -> s.sendMessage(msg).queue());
    }

    public void kill() {
        if (!isAlive()) {
            return;
        }
        if (role == null) {
            return;
        }
        this.getRole().kill(null);
    }

    public void kill(final GameUser killer) {
        if (!isAlive()) {
            return;
        }
        if (role == null) {
            return;
        }
        this.getRole().kill(killer);
    }

    public Object getAttr(Attributes name) {
        return attr.get(name);
    }

    public void setAttr(Attributes name, Object value) {
        if (attr.containsKey(name)) {
            attr.replace(name, value);
            return;
        }
        attr.put(name, value);
    }

    public void setAttr(Attributes name) {
        setAttr(name, null);
    }

    public boolean hasAttr(Attributes attributes) {
        return attr.containsKey(attributes);
    }

    public void removeAttr(Attributes attributes) {
        attr.remove(attributes);
    }

    public boolean hasRole(Class<? extends ARole> role) {
        if (this.getRole().getClass() == role) {
            return true;
        }
        for (Class cls : role.getClasses()) {
            if (cls == role) {
                return true;
            }
        }
        return false;
    }

    public boolean hasVisitors() {
        return related.isEmpty();
    }

    public void sendMessageLang(String msg){
        sendMessage(game.getLang().get(msg));
    }

    public void sendMessageLang(String msg, String... str){
        sendMessage(game.getLang().get(msg, str));
    }

    public Game getGame() {
        return game;
    }

    public List<GameUser> getRelated() {
        return related;
    }

    public boolean inHouse() {
        return inHouse;
    }

    public void setInHouse(boolean inHouse) {
        this.inHouse = inHouse;
    }

    public User getUser() {
        return user;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public ARole getRole() {
        return role;
    }

    public void setRole(ARole role) {
        this.role = role;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Map<Attributes, Object> getAttributes() {
        return attr;
    }

    public boolean isHidden() {
        return hidden;
    }

    public List<GameUser> getRoleKnows() {
        return roleKnows;
    }
}
