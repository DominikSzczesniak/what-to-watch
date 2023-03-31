package pl.szczesniak.dominik.whattowatch.users.domain.model;


import java.util.List;

public class UserService {

    private final UserRepository repository;

    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

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
