package pl.szczesniak.dominik.whattowatch.users.domain.model;

public interface UserRepository {

    int saveUserId(User user);
    int getUserId(User user);
}
