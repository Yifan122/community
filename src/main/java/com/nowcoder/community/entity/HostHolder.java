package com.nowcoder.community.entity;

import org.springframework.stereotype.Component;

/**
 * Hold the User information
 * Use HostHolder to replace the session
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }


}
