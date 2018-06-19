package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MovieBoxOfficeRepository extends CrudRepository<MovieBoxOffice, Long> {
    MovieBoxOffice findByMovieId(Integer movieId);
    List<MovieBoxOffice> findAll();
}