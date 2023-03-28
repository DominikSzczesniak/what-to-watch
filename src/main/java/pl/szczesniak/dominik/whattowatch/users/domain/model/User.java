package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class User {

    @Getter
    private final UserId id;
    @Getter
    private final String userName;

    public User(final String userName, final int id) {
        this.userName = userName;
        this.id = new UserId(id);
    }
}
