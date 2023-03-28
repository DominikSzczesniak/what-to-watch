package pl.szczesniak.dominik.whattowatch.users.domain.model;

import java.util.List;

public interface UserRepository {

    UserId getUserId(final String username);
    void createUser(String username);
    int nextUserId();
    List<UserId> findAll();
}
