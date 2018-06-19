package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TvCreatedRepository extends CrudRepository<TvCreated, TvKey> {
    TvCreated findAllByPersonIdAndTvId(Integer personId, Integer tvId);
    List<TvCreated> findAllByTvId(Integer tvId);
    List<TvCreated> findAllByPersonId(Integer personId);
}