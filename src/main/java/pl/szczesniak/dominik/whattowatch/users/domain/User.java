package pl.szczesniak.dominik.whattowatch.users.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class User {

    @Getter
    private final String userName;
    @Getter
    private final UserId userId;

}
