package ee.karlaru.filters.repository;

import ee.karlaru.filters.domain.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {

    Optional<Filter> findByUuid(UUID uuid);
}
