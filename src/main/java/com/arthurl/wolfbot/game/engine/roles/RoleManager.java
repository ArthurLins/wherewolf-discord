package com.arthurl.wolfbot.game.engine.roles;

import com.arthurl.wolfbot.game.Game;
import com.arthurl.wolfbot.game.engine.roles.role.NormalWolf;
import com.arthurl.wolfbot.game.engine.roles.role.Prefect;
import com.arthurl.wolfbot.game.engine.roles.role.Seer;
import com.arthurl.wolfbot.game.engine.roles.role.Villager;
import com.arthurl.wolfbot.game.engine.roles.role.types.Wolf;
import com.arthurl.wolfbot.game.engine.users.GameUser;
import com.arthurl.wolfbot.views.View;
import gnu.trove.map.hash.THashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RoleManager {

    private final Game game;
    private final Random random = new Random();
    private final List<Class<? extends ARole>> roles = new ArrayList<>();

    public RoleManager(Game game) {
        this.game = game;
        registerRole(Villager.class);
        registerRole(NormalWolf.class);
        registerRole(Seer.class);
        registerRole(Prefect.class);
    }

    public void assignRoles() {
        final THashMap<String, GameUser> users = game.getGameUsers();
        final List<String> keySet = new ArrayList<>(users.keySet());
        Collections.shuffle(keySet);
        int index = 0;
        for (String userKey : keySet) {
            if (index >= roles.size()) {
                assignToUser(users.get(userKey), roles.get(0));
                return;
            }
            assignToUser(users.get(userKey), roles.get(index));
            index++;
        }
        //Todo implementation to best wolf dist...
    }


    public List<GameUser> aliveList(Class<? extends ARole> role) {
        final List<GameUser> aliveUsers = new ArrayList<>();
        game.getGameUsers().forEach((k,v)->{
            if (v.hasRole(role) && v.isAlive()) {
                aliveUsers.add(v);
            }
        });
        return aliveUsers;
    }

    public boolean roleWin(Class<? extends ARole> roleWin, Class<? extends ARole> compare) {
        final int win = aliveList(roleWin).size();
        final int com = aliveList(compare).size();
        return win > 0 && com == 0;
    }

    private void assignToUser(final GameUser user, final Class<? extends ARole> role) {
        try {
            final ARole roleInstance = role.newInstance();
            roleInstance.selfuser = user;
            roleInstance.game = game;
            roleInstance.init();
            user.setRole(roleInstance);
            View.sayRoleToUser(user);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    private void assing(Class<? extends ARole> role, THashMap<String, GameUser> users, int count) {
        if (count == 0) {
            return;
        }
        final Object[] keys = users.keySet().toArray();
        final String randKey = (String) keys[random.nextInt(users.size())];
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
