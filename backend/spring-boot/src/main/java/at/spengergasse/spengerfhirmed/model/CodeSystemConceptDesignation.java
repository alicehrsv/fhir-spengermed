package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * FHIR CodeSystem.concept.designation (BackboneElement)
 * Speichert alternative Bezeichnungen/Synonyme eines Concepts.
 *
 * Mapping: Die 8 Synonym-Spalten aus der Excel-Tabelle werden jeweils
 * als eigene Designation gespeichert (eine Zeile = ein Synonym).
 *
 * Laut Excel-Header: "concept.designation als Synonym des Begriffs concept.display"
 */
@Entity
@Table(name = "cscd_codesystemconceptdesignation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CodeSystemConceptDesignation extends Element {

    /**
     * Sprache der Designation (optional).
     * Entspricht CodeSystem.concept.designation.language
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cscd_language")
    private Resource.LANGUAGE_CODE language;

    /**
     * Art der Designation, z.B. "synonym".
     * Entspricht CodeSystem.concept.designation.use (Coding)
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cscd_use_id")
    private Coding use;

    /**
     * Der eigentliche Synonym-Text.
     * Entspricht CodeSystem.concept.designation.value
     * Mapping: Inhalt der Synonym-Spalten aus der Excel-Tabelle
     */
    @Column(name = "cscd_value", length = 500, nullable = false)
    private String value;
}