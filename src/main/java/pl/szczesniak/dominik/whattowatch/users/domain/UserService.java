package pl.szczesniak.dominik.whattowatch.users.domain;


import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserId createUser(final User user) {
        return repository.createUser(user);
    }

    public UserId nextUserId() {
        return repository.nextUserId();
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public boolean exists(final UserId userId) {
        return repository.findAll().stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }

}
