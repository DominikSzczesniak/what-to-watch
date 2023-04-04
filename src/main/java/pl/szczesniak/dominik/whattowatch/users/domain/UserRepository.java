package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

public interface UserRepository {

    List<User> findAll();
    UserId createUser(final User user);
    UserId nextUserId();
    boolean exists(final UserId userId);
}
