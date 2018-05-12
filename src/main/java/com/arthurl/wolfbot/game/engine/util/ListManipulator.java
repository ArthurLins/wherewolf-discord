package com.arthurl.wolfbot.game.engine.util;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class ListManipulator {

    /**
     * This functions return the KEY of biggest and unique value.
     *
     * @param map Any type of map.
     * @return Null | String key Return null if have duplicated big values or if bigger = 0
     */
    @Nullable
    public static String getUniqueBigKey(Map<String, Integer> map) {
        int bigger = 0;
        String biggerKey = null;
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            int voteQtd = map.get(key);
            if (voteQtd > bigger) {
                bigger = voteQtd;
                biggerKey = key;
            }
        }
        if (bigger == 0) {
            return null;
        }
        for (String key : keySet) {
            if (!biggerKey.equals(key) && map.get(key) == bigger) {
                //System.out.println("Valores repetidos...");
                return null;
            }
        }
        return biggerKey;
    }

}
