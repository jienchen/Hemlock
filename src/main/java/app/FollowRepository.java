package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface FollowRepository extends CrudRepository<Follow, FollowKey> {
    Follow findByUsernameAndFollow(String username, String follow);
    List<Follow> findAllByUsername(String username);
    List<Follow> findAllByFollow(String follow);
    Long countByFollow(String username);

    @Transactional
    Long deleteByUsername(String username);
    @Transactional
    Long deleteByFollow(String username);
}