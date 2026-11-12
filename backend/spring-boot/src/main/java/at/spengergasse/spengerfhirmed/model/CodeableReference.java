package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CodeableReference extends Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_concept_id", referencedColumnName = "id")
    private CodeableConcept concept;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_reference_id", referencedColumnName = "id")
    private Reference reference;
}