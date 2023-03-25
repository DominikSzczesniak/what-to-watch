package pl.szczesniak.dominik.whattowatch.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@EqualsAndHashCode
@ToString
public class UserId {

    @Getter
    private final int id;
    public static final AtomicInteger nextId = new AtomicInteger();

    UserId() {
        id = nextId.incrementAndGet();
    }
}
