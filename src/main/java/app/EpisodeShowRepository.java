package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface EpisodeShowRepository extends CrudRepository<EpisodeShow, EpisodeKey> {
    EpisodeShow findByEpIdAndTvId(Integer EpId, Integer TvId);
    List<EpisodeShow> findAllByTvId(Integer TvId);
}