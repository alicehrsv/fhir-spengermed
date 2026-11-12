package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "mi_medication_ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MedicationIngredient extends BakcboneElement {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mi_item_cc_id")
    private CodeableConcept itemCodeableConcept;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mi_item_ref_id")
    private Reference itemReference;

    @Column(name = "mi_is_active")
    private Boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mi_strength_id")
    private Ratio strength;
}