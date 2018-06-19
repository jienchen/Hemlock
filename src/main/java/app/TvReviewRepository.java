package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface TvReviewRepository extends CrudRepository<TvReview, Long> {
    TvReview findById(Integer id);
    TvReview findByUsernameAndTvId(String username, Integer tvId);
    List<TvReview> findFirst5ByTvIdOrderByDateDesc(Integer tvId);
    List<TvReview> findFirst5ByUsernameOrderByDateDesc(String username);
    List<TvReview> findAllByUsername(String username);
    List<TvReview> findAllByRoleAndTvId(String role, Integer tvId);
    List<TvReview> findAllByTvId(Integer tvId);

    @Transactional
    Long deleteByUsername(String username);
}