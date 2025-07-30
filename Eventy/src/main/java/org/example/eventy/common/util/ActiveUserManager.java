package org.example.eventy.common.util;

import org.example.eventy.users.models.User;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ActiveUserManager {
    private final ConcurrentMap<String, Boolean> activeUsers = new ConcurrentHashMap<>();

    public void addUser(User user) {
        activeUsers.put(user.getEmail(), true);
    }

    public void removeUser(String email) {
        activeUsers.remove(email);
    }

    public boolean isUserActive(String email) {
        return activeUsers.containsKey(email);
    }
}