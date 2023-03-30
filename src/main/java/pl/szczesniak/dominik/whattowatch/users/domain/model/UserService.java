package pl.szczesniak.dominik.whattowatch.users.domain.model;


import pl.szczesniak.dominik.whattowatch.users.domain.model.exceptions.UserDoesNotExistException;

import java.util.List;

public class UserService {

    private final UserRepository repository;

    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    public UserId getUserId(final String username) {
        return repository.getUserId(username);
    }

    public void createUser(String username) {
        repository.createUser(username);
    }

    public List<UserId> findAllId() {
        return repository.findAll();
    }

    public boolean exists(final UserId userId) {
        if (!repository.findAll().contains(userId)) {
            throw new UserDoesNotExistException("User does not exist");
        }
        return true;
    }

}
