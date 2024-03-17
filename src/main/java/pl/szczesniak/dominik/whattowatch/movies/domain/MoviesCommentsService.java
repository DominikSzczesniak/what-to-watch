package pl.szczesniak.dominik.whattowatch.movies.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddCommentToMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteCommentFromMovie;

import java.util.UUID;

@RequiredArgsConstructor
@Service
class MoviesCommentsService {

	private final MoviesToWatchRepository repository;

	UUID addCommentToMovie(final AddCommentToMovie command) {
		final Movie movie = repository.getMovie(command.getMovieId(), command.getUserId());
		final UUID commentId = movie.addComment(command.getComment());
		repository.update(movie);
		return commentId;
	}

	void deleteCommentFromMovie(final DeleteCommentFromMovie command) {
		final Movie movie = repository.getMovie(command.getMovieId(), command.getUserId());
		movie.deleteComment(command.getCommentId());
		repository.update(movie);
	}

}
