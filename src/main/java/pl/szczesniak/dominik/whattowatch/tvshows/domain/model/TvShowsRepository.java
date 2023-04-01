package pl.szczesniak.dominik.whattowatch.tvshows.domain.model;

import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;

public interface TvShowsRepository {

    int nextShowId();
    void save(final TvShow tvShow);
    List<TvShow> findAll(final UserId userId);
    void removeTvShow(final TvShowId tvShowId);

}
