package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface WatchlistTvRepository extends CrudRepository<WatchlistTv, ListTvKey> {
    WatchlistTv findByUsernameAndTvId(String username, Integer tvId);
    List<WatchlistTv> findAllByUsername(String username);

    @Transactional
    Long deleteByUsername(String username);
}