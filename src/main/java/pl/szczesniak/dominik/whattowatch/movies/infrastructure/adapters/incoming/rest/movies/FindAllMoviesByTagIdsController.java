package pl.szczesniak.dominik.whattowatch.movies.infrastructure.adapters.incoming.rest.movies;

//@RequiredArgsConstructor
//@RestController
//public class FindAllMoviesByTagIdsController {

//	private final MoviesService moviesService;
//
//	@GetMapping("/api/movies")
//	public ResponseEntity<List<MovieDetailsByTagDto>> findAllMoviesByTagId(@RequestHeader final Integer userId,
//																		   @RequestBody final TagIdsDto tags) {
//		final List<MovieTagId> tagIds = tags.getTags().stream()
//				.map(tag -> new MovieTagId(UUID.fromString(tag)))
//				.collect(Collectors.toList());
//		final List<Movie> foundMovies = moviesService.getMoviesByTags(tagIds, new UserId(userId));
//
//		final List<MovieDetailsByTagDto> movies = foundMovies.stream().map(movie ->
//				new MovieDetailsByTagDto(
//						movie.getTitle().getValue(),
//						movie.getMovieId().getValue(),
//						userId,
//						mapMovieComments(movie),
//						mapMovieTags(movie)
//				)).collect(Collectors.toList());
//		return ResponseEntity.status(200).body(movies);
//	}
//
//	private static List<MovieTagDto> mapMovieTags(final Movie movie) {
//		return movie.getTags().stream()
//				.map(movieTag -> new MovieTagDto(movieTag.getTagId().getValue(), movieTag.getLabel().getValue()))
//				.toList();
//	}
//
//	private static List<MovieCommentDto> mapMovieComments(final Movie movie) {
//		return movie.getComments().stream()
//				.map(movieComment -> new MovieCommentDto(
//						movieComment.getCommentId().getValue().toString(),
//						movieComment.getText()))
//				.collect(Collectors.toList());
//	}
//
//	@Data
//	private static class TagIdsDto {
//
//		private List<String> tags;
//
//	}
//
//	@Value
//	private static class MovieDetailsByTagDto {
//		String title;
//		Integer movieId;
//		Integer userId;
//		List<MovieCommentDto> comments;
//		List<MovieTagDto> tags;
//	}
//
//	@Value
//	private static class MovieCommentDto {
//		String commentId;
//		String value;
//	}
//
//	@Value
//	private static class MovieTagDto {
//		String tagId;
//		String tagLabel;
//	}

//}
