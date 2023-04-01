package pl.szczesniak.dominik.whattowatch.tvshows.domain.model;

import lombok.Getter;
import lombok.ToString;

@ToString
public class TvShowId {

    @Getter
    private final int id;

    public TvShowId(final int id) {
        this.id = id;
    }
}
