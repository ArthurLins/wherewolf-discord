package com.arthurl.wolfbot.game.engine.roles;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.roles.role.NormalWolf;
import com.arthurl.wolfbot.game.engine.roles.role.Prefect;
import com.arthurl.wolfbot.game.engine.roles.role.Villager;
import com.arthurl.wolfbot.game.engine.roles.role.types.Civilian;
import com.arthurl.wolfbot.game.engine.roles.role.Seer;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import gnu.trove.map.hash.THashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RoleManager {

    private Game game;
    private Random random = new Random();
    private List<Class<? extends ARole>> roles = new ArrayList<>();

    public RoleManager(Game game) {
        this.game = game;
        registerRole(Villager.class);
        registerRole(NormalWolf.class);
        registerRole(Seer.class);
        registerRole(Prefect.class);
    }

    public void assingRoles() {
        THashMap<String, GameUser> users = game.getGameUsers();
        List<String> keyset = new ArrayList<>(users.keySet());
        Collections.shuffle(keyset);
        int index = 0;
        for (String userKey : keyset) {
            if (index >= roles.size()) {
                assingToUser(users.get(userKey), roles.get(0));
                return;
            }
            assingToUser(users.get(userKey), roles.get(index));
            index++;
        }
        //Todo implementation to best wolf dist...
    }


    public List<GameUser> aliveList(Class<? extends ARole> role, boolean superclass){
        List<GameUser> aliveUsers = new ArrayList<>();
        game.getGameUsers().forEach((k,v)->{
            //System.out.println("WOLF COUNT ->" + v.getUser().getName() + "|"+ v.getRole().getClass().getSuperclass());
            if (v.getRole().getClass() == role && v.isAlive() && !superclass){
                aliveUsers.add(v);
            }
            if (v.getRole().getClass().getSuperclass() == role && v.isAlive() && superclass){
                aliveUsers.add(v);
            }
        });
        //System.out.println("Wolf count -> "+wolfUsers.size());
        return aliveUsers;
    }

    private void assingToUser(GameUser user, Class<? extends ARole> role) {
        try {
            user.setRole(role.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    private void assing(Class<? extends ARole> role, THashMap<String, GameUser> users, int count) {
        if (count == 0) {
            return;
        }
        Object[] keys = users.keySet().toArray();
        String randKey = (String) keys[random.nextInt(users.size())];
        if (users.get(randKey).getRole().getClass() == role) {
            assing(role, users, count);
        }
        try {
            users.get(randKey).setRole(role.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assing(role, users, (count - 1));
    }


    private void randomRole(GameUser user) {
        int role = random.nextInt(roles.size());
        Class<? extends ARole> rolel = roles.get(role);
        if (rolel == Wolf.class) {
            randomRole(user);
        }
        try {
            user.setRole(roles.get(role).newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void registerRole(Class<? extends ARole> role) {
        if (roles.contains(role)) {
            return;
        }
        roles.add(role);
    }

}
