package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface OpeningWeekRepository extends CrudRepository<OpeningWeek, Long> {
    OpeningWeek findByMovieId(Integer movieId);
    List<OpeningWeek> findAll();
}