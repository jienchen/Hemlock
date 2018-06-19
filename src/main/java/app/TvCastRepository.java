package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TvCastRepository extends CrudRepository<TvCast, TvKey> {
    TvCast findAllByPersonIdAndTvId(Integer personId, Integer tvId);
    List<TvCast> findAllByTvId(Integer tvId);
    List<TvCast> findAllByPersonId(Integer personId);
}