package pl.szczesniak.dominik.whattowatch.users.domain.model;


import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserId createUser(String username) {
        return repository.createUser(username);
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public boolean exists(final UserId userId) {
        return repository.findAll().stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }

}
