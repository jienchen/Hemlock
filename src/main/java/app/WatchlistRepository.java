package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface WatchlistRepository extends CrudRepository<Watchlist, ListKey> {
    Watchlist findByUsernameAndMovieId(String username, Integer movieId);
    List<Watchlist> findAllByUsername(String username);

    @Transactional
    Long deleteByUsername(String username);
}