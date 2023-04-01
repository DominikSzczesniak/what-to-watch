package pl.szczesniak.dominik.whattowatch.tvshows.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@ToString
@EqualsAndHashCode
public class TvShow {

    @Getter
    private final TvShowId tvShowId;
    @Getter
    private final UserId userId;
    @Getter
    private final String title;

    public TvShow(final TvShowId tvShowId, final String title, final UserId userId) {
        this.tvShowId = tvShowId;
        this.userId = userId;
        this.title = title;
    }
}
