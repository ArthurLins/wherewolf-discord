package com.arthurl.wolfbot;

import com.arthurl.wolfbot.discord.DiscordClient;
import com.arthurl.wolfbot.game.GameManager;
import com.arthurl.wolfbot.threading.ThreadPool;

public class Bootstrap {

    private static ThreadPool threadPool;
    private static GameManager gameManager;

    public static void main(String[] args){
        threadPool = new ThreadPool();
        gameManager = new GameManager();
        new DiscordClient(args[0],"");
    }

    public static ThreadPool getThreadPool() {
        return threadPool;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }
}
