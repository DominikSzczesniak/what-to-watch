package pl.szczesniak.dominik.videos.domain.model;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

public class UserID {

    @Getter
    private final int id;
    public static final AtomicInteger nextId = new AtomicInteger();

    UserID() {
        id = nextId.incrementAndGet();
    }
}
