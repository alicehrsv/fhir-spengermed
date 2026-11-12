package at.spengergasse.spengerfhirmed.entity;

import at.spengergasse.spengerfhirmed.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "o_observation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Observation extends DomainResource {

    public enum StatusCode {
        registered, preliminary, _final, amended, corrected, cancelled,
        entered_in_error, unknown
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_o_id", referencedColumnName = "id")
    private List<Identifier> identifier = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_basedon_id", referencedColumnName = "id")
    private List<Reference> basedOn = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_partof_id", referencedColumnName = "id")
    private List<Reference> partOf = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "o_status", nullable = false)
    private StatusCode status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_o_category_id", referencedColumnName = "id")
    private List<CodeableConcept> category = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_code_id", nullable = false)
    private CodeableConcept code;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_subject_id")
    private Reference subject;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_focus_id", referencedColumnName = "id")
    private List<Reference> focus = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_encounter_id")
    private Reference encounter;

    @Column(name = "o_effective_datetime")
    private LocalDateTime effectiveDateTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_effective_period_id")
    private Period effectivePeriod;

    @Column(name = "o_issued")
    private LocalDateTime issued;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_performer_id", referencedColumnName = "id")
    private List<Reference> performer = new ArrayList<>();

    // Value[x] - we'll support the most common types
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_value_quantity_id")
    private Quantity valueQuantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_value_cc_id")
    private CodeableConcept valueCodeableConcept;

    @Column(name = "o_value_string")
    private String valueString;

    @Column(name = "o_value_boolean")
    private Boolean valueBoolean;

    @Column(name = "o_value_integer")
    private Integer valueInteger;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_value_range_id")
    private Range valueRange;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_data_absent_reason_id")
    private CodeableConcept dataAbsentReason;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_interpretation_id", referencedColumnName = "id")
    private List<CodeableConcept> interpretation = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "a_o_id", referencedColumnName = "id")
    private List<Annotation> note = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_bodysite_id")
    private CodeableConcept bodySite;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_bodystructure_id")
    private Reference bodyStructure;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_method_id")
    private CodeableConcept method;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_specimen_id")
    private Reference specimen;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_device_id")
    private Reference device;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "orr_o_id", referencedColumnName = "id")
    private List<ObservationReferenceRange> referenceRange = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_hasmember_id", referencedColumnName = "id")
    private List<Reference> hasMember = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_derivedfrom_id", referencedColumnName = "id")
    private List<Reference> derivedFrom = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "oc_o_id", referencedColumnName = "id")
    private List<ObservationComponent> component = new ArrayList<>();
}