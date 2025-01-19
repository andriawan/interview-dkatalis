package andriawan.takehome.test.utilities;

import java.util.HashMap;
import java.util.Optional;

import andriawan.takehome.test.entities.User;

public class AuthManager {
    
    private HashMap<String, User> users = new HashMap<>();
    private User authenticatedUser;

    public User login(String name) {
        User user = new User(name);
        authenticatedUser = Optional.ofNullable(
                users.get(name))
            .orElse(user);
        users.put(name, user);
        return authenticatedUser;
    }

    public void logout() {
        authenticatedUser = null;
    }

    public User getAuthenticatedUser() {
        return this.authenticatedUser;
    }

    public User getUser(String name) {
        return users.get(name);
    }
}
