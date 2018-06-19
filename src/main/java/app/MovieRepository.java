package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    Movie findById(Integer id);
    Movie findByTitle(String title);
    List<Movie> findAllByOrderByAvgRatingDesc();
    List<Movie> findAllByOrderByBoxOfficeDesc();
    List<Movie> findAllByOscarBoolOrderByReleaseDateDesc(Boolean oscarBool);

    @Query("select m from Movie m where year(m.releaseDate) = :year and m.oscarBool = :oscarBool")
    List<Movie> findAllByOscarBoolAndYearOrderByReleaseDateDesc(@Param("year") int year, @Param("oscarBool") Boolean oscarBool);

    List<Movie> findByTitleIgnoreCaseContaining(String title);
}