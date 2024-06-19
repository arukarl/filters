package ee.karlaru.filters.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String title;

    @NotNull
    private Integer views;

    @NotNull
    private Date releaseDate;

    public static Map<String, String> mapToTypes() {
        return Map.of(
                "title", "STRING",
                "views", "NUMBER",
                "releaseDate", "DATE"
        );
    }

}
