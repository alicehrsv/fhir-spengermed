package at.spengergasse.spengerfhirmed.entity;

import at.spengergasse.spengerfhirmed.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "m_medication")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Medication extends DomainResource {

    public enum StatusCode {
        active, inactive, entered_in_error
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_m_id", referencedColumnName = "id")
    private List<Identifier> identifier = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "m_code_id")
    private CodeableConcept code;

    @Enumerated(EnumType.STRING)
    @Column(name = "m_status")
    private StatusCode status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "m_manufacturer_id")
    private Reference manufacturer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "m_form_id")
    private CodeableConcept form;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "m_amount_id")
    private Ratio amount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "mi_m_id", referencedColumnName = "id")
    private List<MedicationIngredient> ingredient = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "m_batch_id")
    private MedicationBatch batch;
}