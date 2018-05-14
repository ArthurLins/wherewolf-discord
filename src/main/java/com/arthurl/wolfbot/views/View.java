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
    public static void initGame(final Game game) {
        //All
        game.getBroadcaster().send("Inicio do jogo");
    }

    public static void insufficientUsersToStart(final Game game) {
        game.getBroadcaster().sendLang("game.insufficient-users");
    }

    public static void gameFull(final Game game, final User user) {
        game.getBroadcaster().sendLang("game.full", user.getAsMention());
    }

    public static void userInGame(final Game game, final User user) {
        game.getBroadcaster().sendLang("game.already-in-game", user.getAsMention());
    }

    public static void userJoin(final Game game, final User user) {
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

    public static void gameInit(final Game game) {
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

    public static void lynchKill(final Game game, final GameUser killed) {
        game.getBroadcaster().sendLang("gallow.kill", killed.getUser().getAsMention(), killed.getRole().getName());
    }

    public static void nightResume(final Game game) {
        final EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(game.getLang().get("night-resume.title"));
        game.getGameUsers().forEach((k,v)->{
            final String state = (v.isAlive()) ? " :grimacing: Vivo(a) " : " :skull: Morto(a) ";
            final String job = (v.isHidden() && v.isAlive()) ? game.getLang().get("unknown") : v.getRole().getName();
            eb.addField(v.getUser().getName() + state, job, true);
        });
        eb.setColor(Color.red);
        game.getBroadcaster().send(eb.build());
    }

    public static void inactivityForceEnd(final Game game) {
        game.getBroadcaster().send("Afk.. jogo finalizado!");
    }

    public static void defaultUserSelectorAsk(final Game game, final GameUser user,
                                              final THashMap<Integer, GameUser> users, final Predicate<GameUser> condition) {
        final EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(game.getLang().get("default-user-selector.title"));
        eb.setDescription(game.getLang().get("default-user-selector.description"));
        users.forEach((k, gameUser) -> {
            if (condition.test(gameUser)) {
                if (gameUser.isAlive()) {
                    eb.addField(k + "",
                            gameUser.getUser().getName(), false);
                } else {
                    eb.addField(":skull: Morto(a)", gameUser.getUser().getName(), false);
                }
            }
        });
        user.sendMessage(eb.build());
    }

    public static void defaultOptionSelectorAsk(final Game game, final GameUser user, final String[] options) {
        int index = 1;
        final EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(game.getLang().get("default-option-selector.title"));
        eb.setDescription(game.getLang().get("default-option-selector.description"));
        for (String option : options){
            eb.addField(""+index, option, false);
            index++;
        }
        user.sendMessage(eb.build());
    }


    public static void userIsDead(final Game game, final GameUser user) {
        user.sendMessageLang("default-selector.is-dead");
    }

    public static void sameUser(final Game game, final GameUser user) {
        user.sendMessage("Você não pode escolher você mesmo.");
    }

    public static void selected(final Game game, final GameUser user) {
        user.sendMessage(game.getLang().get("option-selected"));
    }

    public static void userNotFound(final Game game, final GameUser user) {
        user.sendMessage("Esta pessoa não exite.");
    }

    public static void selectionExpired(Game game, GameUser user) {
        user.sendMessage("O tempo pra escolher acabou...");
    }

    public static void invalidOption(final GameUser user) {
        user.sendMessageLang("default-selector.invalid-option");
    }

    public static void gameDay(final Game game) {
        game.getBroadcaster().send(game.getLang().get("game.day"));
    }

    public static void gameDayEnd(final Game game) {
        game.getBroadcaster().send("Fim do dia...");
    }

    public static void wolfsWins(final Game game) {
        game.getBroadcaster().send("Vitoria dos lobos!");
    }

    public static void civiliansWins(final Game game) {
        game.getBroadcaster().send("Vitoria dos cidadoes!");
    }

    public static void askPrefectToShowAll(final GameUser selfuser) {
        selfuser.sendMessage("Você deseja mostrarar que é prefeito?");
    }

    public static void gameEnd(final Game game) {
        final EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Resumo do jogo");
        game.getGameUsers().forEach((k,v)->{
            final String state = (v.isAlive()) ? " :grimacing: Vivo(a) " : " :skull: Morto(a) ";
            final String job = v.getRole().getName();
            eb.addField(v.getUser().getName() + state, job, true);
        });
        eb.setColor(Color.red);
        game.getBroadcaster().send(eb.build());
    }

    public static void seerViewAsk(GameUser selfuser) {
        selfuser.sendMessage("Escolha o");
    }

    public static void sayRoleToUser(GameUser user) {
        user.sendMessage("Você é `" + user.getRole().getName() + "` \n e sua obrigação na Vila é "
                + user.getRole().getDescription());
    }

    public static void notHaveMonition(Game game, GameUser selfuser) {
        selfuser.sendMessage("Você não tem munição!");
    }

    public static void hunterKillAsk(Game game, GameUser selfuser, int munition) {
        selfuser.sendMessage("Em quem você deseja atirar? (Você tem " + munition + " muniçoes restantes)");
    }

    public static void wolfKillAsk(GameUser self) {
        self.sendMessage("Escolha quem você quer matar: ");
    }

    public static void harlotVisitAsk(GameUser user) {
        user.sendMessage("Quem você deseja visitar?");
    }

    public static void wolfKillDrunk(GameUser wolf) {
        wolf.sendMessage("Você matou um bebado, agora você esta bebado e não poderá jogar por 1 rodada");
    }

    public static void wolfIsDrunk(GameUser selfuser) {
        selfuser.sendMessage("Você esta bebado... esta noite você não pode atacar ng!");
    }

    public static void drunkNightMessage(GameUser selfuser) {
        selfuser.sendMessage("Você é um simples bebado, apenas se recolhe para sua casa, e vai dormir.");
    }

    public static void gunnerNightMessage(GameUser selfuser) {
        selfuser.sendMessage("Espere o dia para realizar suas açoes...");
    }

    public static void gameNight(Game game) {
        game.getBroadcaster().sendLang("game.night");
    }

    public static void gameVote(Game game) {
        game.getBroadcaster().send(game.getLang().get("game.vote"));
    }
}
