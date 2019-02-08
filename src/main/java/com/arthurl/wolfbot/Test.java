package com.arthurl.wolfbot;


import com.arthurl.wolfbot.game.engine.util.ListManipulator;
import gnu.trove.map.hash.THashMap;

import java.util.*;

public class Test {
    public static void main(String[] args) throws Exception {
        THashMap<String, Integer> votes = new THashMap<>();
        votes.put("id1", 1);
        votes.put("id2", 2);
        votes.put("id3", 1);

        System.out.println(ListManipulator.getUniqueBigKey(votes));


        //System.out.println("KILL FORCA ");
        //return game.getUserById(""+votes.get(keys.first()));
    }


    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(unsortMap.entrySet());

        list.sort(Comparator.comparing(o -> (o.getValue())));

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
