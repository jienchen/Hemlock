package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CriticApplicationsRepository extends CrudRepository<CriticApplications, Long> {
    CriticApplications findById(Integer id);
    CriticApplications findByUsername(String username);

    @Transactional
    Long deleteByUsername(String username);
}