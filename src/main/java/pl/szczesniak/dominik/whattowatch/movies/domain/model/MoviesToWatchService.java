package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.szczesniak.dominik.whattowatch.users.domain.model.User;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserService;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MoviesToWatchService {

    private final MoviesRepository repository;
//    private final UserService userService;

    public void addMovieToList(final User user, final String movieTitle, final UserId userId) {
//        if (userService.getUserId(user.getUserName()) != userId.getId()) {
//            System.out.println("exception no user");
//            return;
//        }
        Movie movie = new Movie(new MovieId(repository.nextMovieId()), movieTitle, userId);
        if (isMovieTitleDuplicate(user, movieTitle)) {
            System.out.println("Ignoring movie to add, because movie title already exists in user's watchlist. "
                    + "UserId = " + userId.getId() + " MovieTitle = " + movieTitle);
            return;
        }
        repository.save(movie);
    }

    private boolean isMovieTitleDuplicate(final User user, final String movieTitle) {
        return getList(user.getId())
                .stream()
                .anyMatch(title -> movieTitle.equals(title.getTitle()));
    }

    public void removeMovieFromList(final UserId id, final String title) {
        getList(id).forEach(movie -> {
                    if (movie.getTitle().equals(title)) {
                        repository.removeMovie(movie.getMovieId());
                    }
                });
    }


    public List<Movie> getList(final UserId id) {
        return repository.findAll(id);
    }
}


