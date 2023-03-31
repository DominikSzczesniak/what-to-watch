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
    public final AtomicInteger nextId = new AtomicInteger();

    @Override
    public List<UserId> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserId createUser(final String username) {
        final UserId userId = new UserId(nextUserId());
        users.put(username, userId);
        return userId;
    }

    @Override
    public int nextUserId() {
        return nextId.incrementAndGet();
    }

}
