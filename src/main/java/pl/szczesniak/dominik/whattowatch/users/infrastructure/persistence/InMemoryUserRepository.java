package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import pl.szczesniak.dominik.whattowatch.users.domain.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.UserRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UsernameIsTakenException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UserId, User> users = new HashMap<>();
    public final AtomicInteger nextId = new AtomicInteger();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserId createUser(final User user) {
        if (usernameIsTaken(user.getUserName())) {
            throw new UsernameIsTakenException("Please choose different name, " + user.getUserName() + " is already taken");
        }
        users.put(user.getUserId(), user);
        return user.getUserId();
    }

    @Override
    public UserId nextUserId() {
        return new UserId(nextId.incrementAndGet());
    }

    private boolean usernameIsTaken(final String username) {
        return findAll().stream().anyMatch(user -> username.equalsIgnoreCase(user.getUserName()));
    }

}
