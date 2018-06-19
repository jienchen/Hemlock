package app;

import org.springframework.data.repository.CrudRepository;

public interface ReportedReviewTvRepository extends CrudRepository<ReportedReviewTv, ListTvKey> {
    ReportedReviewTv findByUsernameAndTvId(String username, Integer tvId);
}