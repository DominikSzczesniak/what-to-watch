package pl.szczesniak.dominik.whattowatch.users.domain;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

public interface UserRepository {

    List<User> findAll();
    void saveUser(User user);
    UserId nextUserId();
    boolean exists(UserId userId);

}
