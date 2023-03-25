package pl.szczesniak.dominik.whattowatch.users.domain.model;

public interface UserRepository {

    int saveUserId(final User user);
    int getUserId(final User user);
}
