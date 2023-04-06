package pl.szczesniak.dominik.whattowatch.users.domain;


import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void saveUser(final User user) {
        repository.saveUser(user);
    }

    public User createUser(final String username) {
        if (usernameIsTaken(username)) {
            throw new UsernameIsTakenException("Please choose different name, " + username + " is already taken");
        }
        return new User(username, nextUserId());
    }

    private UserId nextUserId() {
        return repository.nextUserId();
    }


    public boolean exists(final UserId userId) {
        return repository.exists(userId);
    }

    private boolean usernameIsTaken(final String username) {
        return repository.findAll().stream().anyMatch(user -> username.equalsIgnoreCase(user.getUserName()));
    }

}
