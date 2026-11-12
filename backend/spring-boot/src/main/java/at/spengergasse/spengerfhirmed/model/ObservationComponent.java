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
@Table(name = "oc_observation_component")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ObservationComponent extends BakcboneElement {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oc_code_id", nullable = false)
    private CodeableConcept code;

    // Value[x] - supporting common types
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oc_value_quantity_id")
    private Quantity valueQuantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oc_value_cc_id")
    private CodeableConcept valueCodeableConcept;

    @Column(name = "oc_value_string")
    private String valueString;

    @Column(name = "oc_value_boolean")
    private Boolean valueBoolean;

    @Column(name = "oc_value_integer")
    private Integer valueInteger;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oc_value_range_id")
    private Range valueRange;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oc_data_absent_reason_id")
    private CodeableConcept dataAbsentReason;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_oc_interpretation_id", referencedColumnName = "id")
    private List<CodeableConcept> interpretation = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orr_oc_id", referencedColumnName = "id")
    private List<ObservationReferenceRange> referenceRange = new ArrayList<>();
}