package ee.karlaru.filters.service;


import ee.karlaru.filters.domain.Classifications;
import ee.karlaru.filters.domain.Criterion;
import ee.karlaru.filters.domain.Filter;
import ee.karlaru.filters.domain.Movie;
import ee.karlaru.filters.exception.FilterNotFoundException;
import ee.karlaru.filters.messaging.messages.FilterChangedEvent;
import ee.karlaru.filters.repository.FilterRepository;
import ee.karlaru.filters.util.CriteriaValidator;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterService {

    private final FilterRepository filterRepository;
    private final List<? extends Criterion> criteria;
    private final ApplicationEventPublisher eventPublisher;

    @Getter
    private Classifications classifications;

    @PostConstruct
    public void initClassifications() {
        Map<String, String> movieFields = Movie.mapToTypes();

        Map<String, List<String>> filterOptions = this.criteria.stream()
                .flatMap(e -> e.toClassification().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing
                ));

        classifications = new Classifications(
                movieFields.entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    String type = entry.getValue();
                    List<String> operators = filterOptions.get(type);
                    return new Classifications.Criterion(name, type, operators);
                })
                .toList());
    }

    @Transactional
    public UUID updateFilter(Filter filter) {
        log.info("Updating filter");
        CriteriaValidator.validate(classifications, filter);

        Optional<Filter> existingFilter = filterRepository.findByUuid(filter.getUuid());
        existingFilter.ifPresent(f -> {
            log.debug("Deleting existing filter with UUID: {}", f.getUuid());
            filterRepository.delete(f);
            eventPublisher.publishEvent(new FilterChangedEvent(f.getUuid().toString()));
            filter.setUuid(UUID.randomUUID());
        });

        filterRepository.save(filter);
        return filter.getUuid();
    }

    public Filter getFilter(UUID id) {
        log.info("Getting filter with UUID: {}", id);
        return filterRepository.findByUuid(id).
                orElseThrow(() -> new FilterNotFoundException("Filter %s not found".formatted(id)));
    }

    public List<Filter> getFilters() {
        log.info("Getting all filters");
        return filterRepository.findAll();
    }
}
