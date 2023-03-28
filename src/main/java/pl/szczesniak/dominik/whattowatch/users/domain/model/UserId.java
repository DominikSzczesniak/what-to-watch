package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class UserId {

    @Getter
    private final int id;

    public UserId(final int id) {
        this.id = id;
    }
}
