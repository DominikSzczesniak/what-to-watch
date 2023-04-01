package pl.szczesniak.dominik.whattowatch.tvshows.infrastructure.persistence;

import pl.szczesniak.dominik.whattowatch.tvshows.domain.model.TvShow;
import pl.szczesniak.dominik.whattowatch.tvshows.domain.model.TvShowId;
import pl.szczesniak.dominik.whattowatch.tvshows.domain.model.TvShowsRepository;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryTvShowsRepository implements TvShowsRepository {

    public static final AtomicInteger nextId = new AtomicInteger();
    private final Map<TvShowId, TvShow> tvShows = new HashMap<>();

    @Override
    public int nextShowId() {
        return nextId.incrementAndGet();
    }

    @Override
    public void save(final TvShow tvShow) {
        tvShows.put(tvShow.getTvShowId(), tvShow);
    }

    @Override
    public List<TvShow> findAll(final UserId userId) {
        return tvShows.values().stream()
                .filter(tvShow -> tvShow.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void removeTvShow(final TvShowId tvShowId) {
        tvShows.remove(tvShowId);
    }
}
