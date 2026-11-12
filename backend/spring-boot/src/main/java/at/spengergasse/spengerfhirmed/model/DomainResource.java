package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class DomainResource extends Resource {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dr_na_id")
    private Narrative text;
}
