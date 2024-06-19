package ee.karlaru.filters.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Builder.Default private UUID uuid = UUID.randomUUID();

    @Size(max = 50)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "filter")
    @Builder.Default
    private List<Criterion> criteria = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void updateCriteria() {
        for (Criterion criterion : criteria) {
            criterion.setFilter(this);
        }
    }
}
