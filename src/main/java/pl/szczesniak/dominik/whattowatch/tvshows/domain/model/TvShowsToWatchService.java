package pl.szczesniak.dominik.whattowatch.tvshows.domain.model;

import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;

import java.util.List;

@RequiredArgsConstructor
public class TvShowsToWatchService {

    private final TvShowsRepository repository;
    private final UserService userService;

    public void addTvShowToList(final String tvShowTitle, final UserId userId) {
        if (!userService.exists(userId)) {
            System.out.println("user does not exist");
            return;
        }
        TvShow tvShow = new TvShow(new TvShowId(repository.nextShowId()), tvShowTitle, userId);
        repository.save(tvShow);
    }

    public void removeTvShowFromList(final String title, final UserId id) {
        getList(id).forEach(tvShow -> {
            if (tvShow.getTitle().equals(title)) {
                repository.removeTvShow(tvShow.getTvShowId());
            }
        });
    }

    public List<TvShow> getList(final UserId userId) {
        return repository.findAll(userId);
    }

}
