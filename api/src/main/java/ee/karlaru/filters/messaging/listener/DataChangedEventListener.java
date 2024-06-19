package ee.karlaru.filters.messaging.listener;

import ee.karlaru.filters.messaging.messages.FilterChangedEvent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static ee.karlaru.filters.config.RedisConfig.FILTERED_MOVIES_CACHE;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataChangedEventListener {

    private final CacheManager cacheManager;

    @EventListener
    public void handleFilterChangedEvent(@NotNull FilterChangedEvent event) {
        log.debug("Evicting cache for movies filtered with UUID: {}", event.filterUuid());

        Objects.requireNonNull(
                cacheManager.getCache(FILTERED_MOVIES_CACHE))
                .evictIfPresent(event.filterUuid()
        );
    }

}
