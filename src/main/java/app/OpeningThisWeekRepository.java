package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface OpeningThisWeekRepository extends CrudRepository<OpeningThisWeek, Long> {
    OpeningThisWeek findByMovieId(Integer movieId);
    List<OpeningThisWeek> findAll();
}