package pl.szczesniak.dominik.whattowatch.movies.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.szczesniak.dominik.whattowatch.commons.domain.model.exceptions.ObjectDoesNotExistException;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagId;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.MovieTagLabel;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddMovieToListSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.AddTagToMovieSample;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteTagFromMovie;
import pl.szczesniak.dominik.whattowatch.movies.domain.model.commands.DeleteTagFromMovieSample;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieInListQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieQueryResult;
import pl.szczesniak.dominik.whattowatch.movies.query.model.MovieTagQueryResult;
import pl.szczesniak.dominik.whattowatch.users.domain.model.UserId;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.szczesniak.dominik.whattowatch.movies.domain.TestMoviesFacadeConfiguration.moviesFacade;
import static pl.szczesniak.dominik.whattowatch.movies.domain.model.TagLabelSample.createAnyTagLabel;
import static pl.szczesniak.dominik.whattowatch.users.domain.model.UserIdSample.createAnyUserId;

public class MoviesFacadeTagsTest {

	private InMemoryUserProvider userProvider;

	private MoviesFacade tut;

	@BeforeEach
	void setUp() {
		userProvider = new InMemoryUserProvider();
		tut = moviesFacade(userProvider);
	}

	@Test
	void should_add_tag_to_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		// when
		final MovieTagLabel tagLabel = createAnyTagLabel();
		tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movieId).userId(user).build());

		// then
		final Set<MovieTagQueryResult> tags = tut.getMovie(movieId, user).getTags();
		assertThat(tags).extracting(MovieTagQueryResult::getLabel).containsExactly(tagLabel.getValue());
	}

	@Test
	void should_find_all_tags() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieTagLabel firstTagLabel = createAnyTagLabel();
		final MovieTagLabel secondTagLabel = createAnyTagLabel();
		final MovieTagLabel thirdTagLabel = createAnyTagLabel();
		final MovieTagLabel fourthTagLabel = createAnyTagLabel();

		// when
		final MovieTagId firstTagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(firstTagLabel).movieId(movieId).userId(user).build());
		final MovieTagId secondTagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(secondTagLabel).movieId(movieId).userId(user).build());
		final MovieTagId thirdTagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(thirdTagLabel).movieId(movieId).userId(user).build());
		final MovieTagId fourthTagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(fourthTagLabel).movieId(movieId).userId(user).build());

		final List<MovieTagQueryResult> tags = tut.getMovieTagsByUserId(user.getValue());

		// then
		assertThat(tags.size()).isEqualTo(4);
		assertThat(tags).extracting(MovieTagQueryResult::getTagId)
				.containsExactlyInAnyOrder(firstTagId.getValue(), secondTagId.getValue(), thirdTagId.getValue(), fourthTagId.getValue());
		assertThat(tags).extracting(MovieTagQueryResult::getLabel)
				.containsExactlyInAnyOrder(firstTagLabel.getValue(), secondTagLabel.getValue(), thirdTagLabel.getValue(), fourthTagLabel.getValue());
	}

	@Test
	void should_delete_tag_from_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		final MovieTagId tagId = tut.addTagToMovie(AddTagToMovieSample.builder().movieId(movieId).userId(user).build());
		assertThat(tut.getMovie(movieId, user).getTags()).hasSize(1);

		// when
		tut.deleteTagFromMovie(DeleteTagFromMovieSample.builder().tagId(tagId).movieId(movieId).userId(user).build());

		// then
		assertThat(tut.getMovie(movieId, user).getTags()).hasSize(0);
	}

	@Test
	void should_not_delete_tag_from_movie_if_not_users_tag() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final UserId differentUser = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		final MovieTagId tagId = tut.addTagToMovie(AddTagToMovieSample.builder().movieId(movieId).userId(user).build());

		// when
		final Throwable thrown = catchThrowable(() -> tut.deleteTagFromMovie(new DeleteTagFromMovie(differentUser, movieId, tagId)));

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

	@Test
	void should_not_create_additional_movie_tag_when_adding_same_tag() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieTagLabel tagLabel = createAnyTagLabel();

		// when
		final MovieTagId tagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movieId).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().tagId(tagId).movieId(movieId).userId(user).build());

		final MovieQueryResult movie = tut.getMovie(movieId, user);

		// then
		assertThat(movie.getTags()).hasSize(1);
	}

	@Test
	void should_find_movies_by_tag_id() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movie1 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movie2 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movie3 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movie4 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movieWithDifferentTag = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieTagLabel tagLabel = createAnyTagLabel();

		// when
		final MovieTagId tagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movie1).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().tagId(tagId).movieId(movie2).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().tagId(tagId).movieId(movie3).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().tagId(tagId).movieId(movie4).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().movieId(movieWithDifferentTag).userId(user).build());

		// then
		assertThat(tut.getMoviesToWatch(user)).hasSize(5);
		assertThat(tut.getMoviesByTags(List.of(tagId), user)).hasSize(4);
	}

	@Test
	void should_find_movie_with_two_matching_tags() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final MovieId movie1 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId movie2 = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());

		// when
		final MovieTagId movieTag1 = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(createAnyTagLabel()).movieId(movie1).userId(user).build());
		tut.addTagToMovie(AddTagToMovieSample.builder().tagId(movieTag1).movieId(movie2).userId(user).build());
		final MovieTagId movieTag2 = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(createAnyTagLabel()).movieId(movie2).userId(user).build());

		final List<MovieInListQueryResult> foundMovies = tut.getMoviesByTags(List.of(movieTag1, movieTag2), user);

		// then
		assertThat(foundMovies.size()).isEqualTo(1);
	}

	@Test
	void user_should_not_be_able_to_add_not_his_movie_tag_to_his_movie() {
		// given
		final UserId user = userProvider.addUser(createAnyUserId());
		final UserId differentUser = userProvider.addUser(createAnyUserId());
		final MovieId movieId = tut.addMovieToList(AddMovieToListSample.builder().userId(user).build());
		final MovieId differentUsersMovie = tut.addMovieToList(AddMovieToListSample.builder().userId(differentUser).build());

		// when
		final MovieTagLabel tagLabel = createAnyTagLabel();
		final MovieTagId tagId = tut.addTagToMovie(AddTagToMovieSample.builder().tagLabel(tagLabel).movieId(movieId).userId(user).build());

		final Throwable thrown = catchThrowable(() -> tut.addTagToMovie
				(AddTagToMovieSample.builder().tagId(tagId).movieId(differentUsersMovie).tagLabel(tagLabel).userId(differentUser).build())
		);

		// then
		assertThat(thrown).isInstanceOf(ObjectDoesNotExistException.class);
	}

}
