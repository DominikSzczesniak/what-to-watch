package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import pl.szczesniak.dominik.whattowatch.users.domain.model.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UserId, User> users = new HashMap<>(); // TU ZMIENILEM
    public final AtomicInteger nextId = new AtomicInteger();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserId createUser(final String username) {
        final UserId userId = new UserId(nextUserId());
        final User user = new User(username, userId);
        users.put(userId, user);
        return userId;
    }

    @Override
    public int nextUserId() {
        return nextId.incrementAndGet();
    }

}
