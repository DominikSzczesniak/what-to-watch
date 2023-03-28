package pl.szczesniak.dominik.whattowatch.users.infrastructure.persistence;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, UserId> users = new HashMap<>();
    public static final AtomicInteger nextId = new AtomicInteger();

    @Override
    public UserId getUserId(final String username) {
        if (users.get(username) != null) {
            return users.get(username);
        } else
            return new UserId(nextUserId());
    }

    @Override
    public void createUser(final String username) {
        users.put(username, new UserId(nextUserId()));
    }

    @Override
    public int nextUserId() {
        return nextId.incrementAndGet();
    }

    public List<UserId> findAll() {
        return new ArrayList<>(users.values());
    }


}
