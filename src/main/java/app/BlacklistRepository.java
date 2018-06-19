package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface BlacklistRepository extends CrudRepository<Blacklist, ListKey> {
    Blacklist findByUsernameAndMovieId(String username, Integer movieId);
    List<Blacklist> findAllByUsername(String username);

    @Transactional
    Long deleteByUsername(String username);
}