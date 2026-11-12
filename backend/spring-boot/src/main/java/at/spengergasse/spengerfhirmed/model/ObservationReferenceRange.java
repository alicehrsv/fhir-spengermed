package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orr_observation_reference_range")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ObservationReferenceRange extends BakcboneElement {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orr_low_id")
    private SimpleQuantity low;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orr_high_id")
    private SimpleQuantity high;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orr_normalvalue_id")
    private CodeableConcept normalValue;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orr_type_id")
    private CodeableConcept type;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_orr_appliesto_id", referencedColumnName = "id")
    private List<CodeableConcept> appliesTo = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orr_age_id")
    private Range age;

    @Column(name = "orr_text", columnDefinition = "TEXT")
    private String text;
}