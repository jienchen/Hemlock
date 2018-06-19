package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface BlacklistTvRepository extends CrudRepository<BlacklistTv, ListTvKey> {
    BlacklistTv findByUsernameAndTvId(String username, Integer tvId);
    List<BlacklistTv> findAllByUsername(String username);

    @Transactional
    Long deleteByUsername(String username);
}