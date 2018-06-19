package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MoviesDirectedRepository extends CrudRepository<MoviesDirected, MovieKey> {
    MoviesDirected findAllByPersonIdAndMovieId(Integer personId, Integer movieId);
    List<MoviesDirected> findAllByMovieId(Integer movieId);
    List<MoviesDirected> findAllByPersonId(Integer personId);
}