package pl.szczesniak.dominik.whattowatch.movies.domain.model.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTitle;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
public class AddMovieToList {

	@NonNull
	private final MovieTitle movieTitle;
	@NonNull
	private final Long userId;

	public static AddMovieToListBuilder builder(final MovieTitle movieTitle, final Long userId) {
		return new AddMovieToListBuilder(movieTitle, userId);
	}

	@ToString
	public static class AddMovieToListBuilder {
		private final MovieTitle movieTitle;
		private final Long userId;

		private AddMovieToListBuilder(final MovieTitle movieTitle, final Long userId) {
			this.movieTitle = movieTitle;
			this.userId = userId;
		}

		public AddMovieToList build() {
			return new AddMovieToList(movieTitle, userId);
		}
	}

}