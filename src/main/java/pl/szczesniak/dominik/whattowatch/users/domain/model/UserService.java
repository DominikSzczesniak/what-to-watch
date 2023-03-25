package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService{

    private final UserRepository repository;

    public int getUserId(final User user) {
        return repository.getUserId(user);
    }

    public void saveUser(final User user) {
        repository.saveUserId(user);
    }
}
