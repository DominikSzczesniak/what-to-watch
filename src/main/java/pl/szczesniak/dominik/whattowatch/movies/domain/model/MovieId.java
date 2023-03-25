package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@ToString
public class MovieId {

    @Getter
    private final int id;
    public static final AtomicInteger nextId = new AtomicInteger();

    public MovieId() {
        id = nextId.incrementAndGet();
    }
}
