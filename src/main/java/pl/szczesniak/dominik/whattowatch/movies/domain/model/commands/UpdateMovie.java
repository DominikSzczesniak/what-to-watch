package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public final class UpdateMovie {

	private final @NonNull MovieId movieId;
	private final @NonNull UserId userId;
	private final @NonNull MovieTitle title;

	public static UpdateMovieBuilder builder(final MovieId movieId, final UserId userId, final MovieTitle title) {
		return new UpdateMovieBuilder(movieId, userId, title);
	}

	public static class UpdateMovieBuilder {
		private final MovieId movieId;
		private final UserId userId;
		private final MovieTitle title;

		private UpdateMovieBuilder(final MovieId movieId, final UserId userId, final MovieTitle title) {
			this.movieId = movieId;
			this.userId = userId;
			this.title = title;
		}

		public UpdateMovie build() {
			return new UpdateMovie(movieId, userId, title);
		}

		@Override
		public String toString() {
			return "UpdateMovieBuilder{" +
					"movieId=" + movieId +
					", userId=" + userId +
					", title=" + title +
					'}';
		}
	}

}