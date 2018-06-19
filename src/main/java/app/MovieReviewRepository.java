package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface MovieReviewRepository extends CrudRepository<MovieReview, Long> {
    MovieReview findById(Integer id);
    MovieReview findByUsernameAndMovieId(String username, Integer movieId);
    List<MovieReview> findFirst5ByMovieIdOrderByDateDesc(Integer movieId);
    List<MovieReview> findFirst5ByUsernameOrderByDateDesc(String username);
    List<MovieReview> findAllByUsername(String username);
    List<MovieReview> findAllByRoleAndMovieId(String role, Integer movieId);
    List<MovieReview> findAllByMovieId(Integer movieId);

    @Transactional
    Long deleteByUsername(String username);
}