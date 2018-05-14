package com.arthurl.wolfbot.game.engine.roles;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.actions.AAction;
import com.arthurl.wolfbot.game.engine.users.Attributes;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.game.engine.votes.VoteTypes;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ARole {

    private String name;
    private String description;

    public GameUser selfuser;
    public Game game;

    //Role definition
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    //End role definition

    public Object getUserAttr(final Attributes name) {
        return selfuser.getAttr(name);
    }

    public boolean userHasAttr(final Attributes name) {
        return selfuser.hasAttr(name);
    }

    public void removeUserAttr(final Attributes name) {
        selfuser.removeAttr(name);
    }

    public void setUserAttr(final Attributes name, Object value) {
        selfuser.setAttr(name, value);
    }


    protected void optionSelector(final String[] options,
                                  final Consumer<GameUser> ask,
                                  final BiConsumer<Integer, String> res,
                                  final int tout) {
        game.getDefaultOptionSelector().select(selfuser, tout, ask, options, res);
    }

    protected void userSelector(final Consumer<GameUser> ask,
                                final Consumer<GameUser> res,
                                final int tout) {
        game.getDefaultUserSelector().select(selfuser, tout, ask, res);
    }

    protected void userSelector(final Consumer<GameUser> ask,
                                final Predicate<GameUser> cond,
                                final Consumer<GameUser> res,
                                final int tout) {
        game.getDefaultUserSelector().select(selfuser, tout, cond, ask, res);
    }


    public String text(final String prop) {
        return game.getLang().get(prop);
    }

    public String text(final String prop, final String... strs) {
        return game.getLang().get(prop, strs);
    }

    //Engine responses
    public void finishVote() {
        game.getEngine().fireVoteOK(selfuser);
    }

    public void finishNight() {
        game.getEngine().fireNightOK(selfuser);
    }

    protected void defaultVote(final int mult) {
        game.getVoteManager().requestVote(VoteTypes.DEFAULT, selfuser, mult);
    }

    //End engine responses

    protected void defaultDie() {
        selfuser.setAlive(false);
    }

    public void action(final Class<? extends AAction> action, final Object... objs) {
        game.getActionManager().call(action, objs);
    }

    public abstract void init();

    public abstract void night();

    public abstract void day();

    public abstract void vote();

    public abstract void kill(@Nullable GameUser killer);


}
