package com.arthurl.wolfbot.views;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.function.Predicate;

public class View {

    // Game
    public static void initGame(Game game) {
        //PV
        game.getGameUsers().forEach((k,user)->{
            user.sendMessage("Aviso pv (Novo jogo)");
        });
        //All
        game.getBroadcaster().send("Inicio do jogo");
        game.getBroadcaster().send("~~~EXPLICAÇÃO TOP~~");
    }

    public static void insufficientUsersToStart(Game game) {
        game.getBroadcaster().send("Não há jogadores suficientes...");
    }

    public static void gameFull(Game game, User user) {
        game.getBroadcaster().send(game.getLang().get("game.full", user.getAsMention()));
    }

    public static void userInGame(Game game, User user) {
        game.getBroadcaster().sendLang("game.already-in-game", user.getAsMention());
    }

    public static void userJoin(Game game, User user) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setImage(game.getLang().get("game.join-image"));
        eb.setTitle(game.getLang().get("game.join", user.getName()));
        eb.setDescription(game.getLang().get("game.invite-desc"));
        eb.addField(game.getLang().get("game.players"),
                game.getGameUsers().size() + "/" + game.getMaxUsers(),
                true);
        eb.setFooter(game.getLang().get("game.created-by",
                game.getCreator().getName()),
                game.getCreator().getAvatarUrl());
        eb.setColor(Color.red);
        game.getBroadcaster().send(eb.build());
    }

    public static void gameInit(Game game) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setImage(game.getLang().get("game.invite-image"));
        eb.setTitle(game.getLang().get("game.invite-title"));
        eb.setDescription(game.getLang().get("game.invite-desc"));
        eb.addField(game.getLang().get("game.players"),
                game.getGameUsers().size() + "/" + game.getMaxUsers(),
                true);
        eb.setFooter(game.getLang().get("game.created-by",
                game.getCreator().getName()),
                game.getCreator().getAvatarUrl());
        eb.setColor(Color.red);
        game.getBroadcaster().send(eb.build());
    }

    public static void gallowKill(Game game, GameUser killed) {
        game.getBroadcaster().send(killed.getUser().getAsMention() + " foi enforcado!");
    }

    public static void nightResume(Game game) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Resumo da noite");
        game.getGameUsers().forEach((k,v)->{
            String name = (v.isAlive()) ? ":grimacing: ":":skull: ";
            String job = (v.isHidden() || !v.isAlive())? "Desconhecido" : v.getRole().name;
            eb.addField(name + v.getUser().getName(), job, true);
        });
        eb.setColor(Color.red);
        game.getBroadcaster().send(eb.build());
    }

    public static void inactivityForceEnd(Game game) {
        game.getBroadcaster().send("Afk.. jogo finalizado!");
    }

    public static void defaultSelector(GameUser user, THashMap<Integer, GameUser> users, Predicate<GameUser> condition) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Selecione um usuário:");
        eb.setDescription("Digite o numero correspondente");
        users.forEach((k, gameUser) -> {
            if (condition.test(gameUser)) {
                if (gameUser.isAlive()) {
                    eb.addField(k + "",
                            gameUser.getUser().getName(), false);
                } else {
                    eb.addField(":skull:", gameUser.getUser().getName(), false);
                }
            }
        });
        user.sendMessage(eb.build());
    }

    public static void userIsDead(Game game, GameUser user) {
        user.sendMessage(game.getLang().get("default-selector.is-dead"));
    }

    public static void sameUser(Game game, GameUser user) {
        user.sendMessage("Você não pode escolher você mesmo.");
    }

    public static void selected(Game game, GameUser user) {
        user.sendMessage(game.getLang().get("default-selector.selected"));
    }

    public static void userNotFound(Game game, GameUser user) {
        user.sendMessage("Esta pessoa não exite.");
    }

    public static void selectionExpired(Game game, GameUser user) {
        user.sendMessage("O tempo pra escolher acabou...");
    }

    public static void invalidOption(GameUser user) {
        user.sendMessageLang("default-selector.invalid-option");
    }

    public static void genericOptionAsk(Game game, GameUser user, String[] options) {
        int index = 1;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Selecione uma Opção:");
        eb.setDescription("Digite o numero correspondente");
        for (String option : options){
            eb.addField(""+index, option, false);
            index++;
        }
        user.sendMessage(eb.build());
    }

    public static void gameDay(Game game) {
        game.getBroadcaster().send(game.getLang().get("game.day"));
    }

    public static void gameDayEnd(Game game) {
        game.getBroadcaster().send("Fim do dia...");
    }

    public static void wolfsWins(Game game) {
        game.getBroadcaster().send("Vitoria dos lobos!");
    }

    public static void civiliansWins(Game game) {
        game.getBroadcaster().send("Vitoria dos cidadoes!");
    }

    public static void askPrefectToShowAll(GameUser selfuser) {
        selfuser.sendMessage("Você deseja mostrarar que é prefeito?");
    }

    public static void gameEnd(Game game) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Resumo do jogo");
        game.getGameUsers().forEach((k,v)->{
            String name = (v.isAlive()) ? ":grimacing: ":":skull: ";
            String job = v.getRole().name;
            eb.addField(name + v.getUser().getName(), job, true);
        });
        eb.setColor(Color.red);
        game.getBroadcaster().send(eb.build());
    }

}
