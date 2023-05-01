package pl.szczesniak.dominik.whattowatch.movies.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
public class AddMovieToList {

	@NonNull
	MovieTitle movieTitle;
	@NonNull
	UserId userId;

	public static AddMovieToListBuilder builder(final MovieTitle movieTitle, final UserId userId) {
		return new AddMovieToListBuilder(movieTitle, userId);
	}

	@ToString
	public static class AddMovieToListBuilder {
		private final MovieTitle movieTitle;
		private final UserId userId;

		private AddMovieToListBuilder(final MovieTitle movieTitle, final UserId userId) {
			this.movieTitle = movieTitle;
			this.userId = userId;
		}

		public AddMovieToList build() {
			return new AddMovieToList(movieTitle, userId);
		}
	}

}