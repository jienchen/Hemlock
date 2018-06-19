package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MoviesWrittenRepository extends CrudRepository<MoviesWritten, MovieKey> {
    MoviesWritten findAllByPersonIdAndMovieId(Integer personId, Integer movieId);
    List<MoviesWritten> findAllByMovieId(Integer movieId);
    List<MoviesWritten> findAllByPersonId(Integer personId);
}