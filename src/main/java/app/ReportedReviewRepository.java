package app;

import org.springframework.data.repository.CrudRepository;

public interface ReportedReviewRepository extends CrudRepository<ReportedReview, ListKey> {
    ReportedReview findByUsernameAndMovieId(String username, Integer movieId);
}