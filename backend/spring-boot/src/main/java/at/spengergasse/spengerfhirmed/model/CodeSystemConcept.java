package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.model.CodeSystemConceptProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * FHIR CodeSystem.concept (BackboneElement)
 * Repräsentiert einen einzelnen Begriff/Eintrag des CodeSystem.
 *
 * Mapping: Eine Zeile der Excel-Datei = ein Concept
 *   - code        → id-Spalte (= SNOMED CT ID als Primary Key)
 *   - display     → Begriffe-Spalte (Eigenentwicklung/Anzeigename)
 *   - property    → SNOMEDCTID, SNOMEDCTBEGRIFFE, ICPC2ID, KRITERIEN,
 *                   HINWEISE, FARBE, KOMPONENTE, ICD10BEGRIFFE
 *   - designation → die 8 Synonym-Spalten (concept.designation)
 */
@Entity
@Table(name = "csc_codesystemconcept")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CodeSystemConcept extends Element {

    /**
     * Eindeutiger Code des Concepts (= SNOMED CT ID, Primary Key der Zeile).
     * Entspricht CodeSystem.concept.code
     */
    @Column(name = "csc_code", nullable = false, unique = true)
    private String code;

    /**
     * Anzeigetext für den Benutzer (die "Begriffe"-Spalte, Eigenentwicklung).
     * Entspricht CodeSystem.concept.display
     */
    @Column(name = "csc_display", length = 500)
    private String display;

    /**
     * Weitere Eigenschaften des Concepts:
     * SNOMEDCTID, SNOMEDCTBEGRIFFE, ICPC2ID, KRITERIEN,
     * HINWEISE, FARBE, KOMPONENTE, ICD10BEGRIFFE
     * Entspricht CodeSystem.concept.property (0..*)
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cscp_csc_id", referencedColumnName = "id")
    private List<CodeSystemConceptProperty> property = new ArrayList<>();

    /**
     * Alternative Bezeichnungen / Synonyme des Concepts.
     * Mapping: die 8 Synonym-Spalten aus der Excel-Tabelle.
     * Entspricht CodeSystem.concept.designation (0..*)
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cscd_csc_id", referencedColumnName = "id")
    private List<CodeSystemConceptDesignation> designation = new ArrayList<>();
}