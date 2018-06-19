package app;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Date;
import org.springframework.data.jpa.repository.Query;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {
    Episode findById(Integer id);

    @Query("select e from Episode e where e.airDate = current_date()")
    List<Episode> findByCurdate();
}