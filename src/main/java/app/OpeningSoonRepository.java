package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface OpeningSoonRepository extends CrudRepository<OpeningSoon, Long> {
    OpeningSoon findByMovieId(Integer movieId);
    List<OpeningSoon> findAll();
}