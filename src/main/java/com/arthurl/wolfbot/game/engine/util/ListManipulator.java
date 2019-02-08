package com.arthurl.wolfbot.game.engine.util;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
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
        if (map.isEmpty()){
            return null;
        }
        final Map.Entry<String, Integer> val = Collections.max(map.entrySet(), Map.Entry.comparingByValue());

        if (val == null){
            return null;
        }

        final int highValue = val.getValue();

        if (highValue == 0){
            return null;
        }
        if(map.values().stream().filter((v) -> v == highValue).count() > 1){
            return null;
        }

        return val.getKey();
    }

}
