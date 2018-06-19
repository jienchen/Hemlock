package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface FollowingRepository extends CrudRepository<Following, Long> {
    Following findByUsernameAndFollowing(String username, String following);
    List<Following> findAllByUsername(String username);
    List<Following> findAllByFollowing(String following);
    Long countByFollowing(String username);
}