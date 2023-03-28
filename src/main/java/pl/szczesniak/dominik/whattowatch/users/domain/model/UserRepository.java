package pl.szczesniak.dominik.whattowatch.users.domain.model;

public interface UserRepository {

    UserId getUserId(final String username);
}
