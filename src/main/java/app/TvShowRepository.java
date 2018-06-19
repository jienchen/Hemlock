package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TvShowRepository extends CrudRepository<TvShow, Long> {
    TvShow findById(Integer id);
    TvShow findByTitle(String title);
    List<TvShow> findFirst12ByOrderByAvgCriticRatingDesc();
    List<TvShow> findAllByOrderByAvgRatingDesc();
    List<TvShow> findByTitleIgnoreCaseContaining(String title);
}