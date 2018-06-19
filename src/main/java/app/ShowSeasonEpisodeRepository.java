package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ShowSeasonEpisodeRepository extends CrudRepository<ShowSeasonEpisode, ShowSeasonEpisodeKey> {
    ShowSeasonEpisode findByEpIdAndTvIdAndSeasonNum(Integer epId, Integer tvId, Integer seasonNum);
    List<ShowSeasonEpisode> findAllByTvIdAndSeasonNum(Integer tvId, Integer seasonNum);
}