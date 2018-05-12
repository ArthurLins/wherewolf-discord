package com.arthurl.wolfbot.discord;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class DiscordClient {

    //private JDA discordClient;

    private static void create(String token, String playing){
        try {
            DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
            builder.setToken(token);
            builder.addEventListeners(new DiscordHandler());
            builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        //return null;
}

    public DiscordClient(String token, String playing){
        DiscordClient.create(token, playing);
    }

    //public JDA getDiscordClient() {
        //return discordClient;
    //}
}
