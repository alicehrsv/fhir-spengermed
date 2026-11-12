package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "in_ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SubstanceIngredient extends BakcboneElement{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ra_in_id", referencedColumnName = "id", nullable = true)
    private Ratio quantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sc_in_id", referencedColumnName = "id", nullable = false)
    private CodeableConcept substanceCodeableConcept;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sr_in_id", referencedColumnName = "id", nullable = false)
    private Reference substanceReference;
}
