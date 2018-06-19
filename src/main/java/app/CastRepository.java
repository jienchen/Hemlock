package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CastRepository extends CrudRepository<Cast, MovieKey> {
    Cast findAllByPersonIdAndMovieId(Integer personId, Integer movieId);
    List<Cast> findByMovieId(Integer movieId);
    List<Cast> findAllByPersonId(Integer personId);
}