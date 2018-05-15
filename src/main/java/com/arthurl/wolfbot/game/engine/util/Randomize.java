package com.arthurl.wolfbot.game.engine.util;

import java.util.Random;

public class Randomize {

    private static Random random = new Random();

    public static void propabilityCall(final int percent, final Runnable run){
        final int randVal = random.nextInt(100);
        if (randVal >= 0 && randVal <= percent){
            run.run();
        }
    }
}
