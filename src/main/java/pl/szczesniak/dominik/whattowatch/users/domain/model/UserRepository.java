package pl.szczesniak.dominik.whattowatch.users.domain.model;

import java.util.List;

public interface UserRepository {

    List<UserId> findAll();
    UserId createUser(String username);
    int nextUserId();
}
