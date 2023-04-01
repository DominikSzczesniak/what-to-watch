package pl.szczesniak.dominik.whattowatch.users.domain.model;


import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserId createUser(String username) {
        return repository.createUser(username);
    }

    public List<UserId> findAllId() {
        return repository.findAll();
    }

    public boolean exists(final UserId userId) {
        return repository.findAll().contains(userId);
    }

}
