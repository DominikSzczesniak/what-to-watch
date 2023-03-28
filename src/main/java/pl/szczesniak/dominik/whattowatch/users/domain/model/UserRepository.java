package pl.szczesniak.dominik.whattowatch.users.domain.model;

public interface UserRepository {

    int getUserId(final String username);
}
