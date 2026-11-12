package at.spengergasse.spengerfhirmed.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * FHIR CodeSystem.concept.property (BackboneElement)
 * Speichert den konkreten Wert einer Property für ein bestimmtes Concept.
 *
 * Mapping: Eine Zeile hier = eine Zelle in der Excel-Tabelle
 * (z.B. Concept "Diffuser Schmerz" → Property "ICD10BEGRIFFE" → Value "R52.9")
 *
 * value[x] – je nach Typ wird genau ein value-Feld befüllt:
 *   - valueString  → SNOMEDCTBEGRIFFE, ICD10BEGRIFFE, KRITERIEN, HINWEISE, SYNONYM, ICPC2ID
 *   - valueInteger → FARBE, KOMPONENTE
 *   - valueCode    → SNOMEDCTID (als Code-Wert)
 */
@Entity
@Table(name = "cscp_codesystemconceptproperty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CodeSystemConceptProperty extends Element {

    /**
     * Referenz auf die Property-Definition (welche Spalte).
     * Entspricht CodeSystem.concept.property.code
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cscp_code", nullable = false)
    private CodeCode code;

    /**
     * Wert als String (für SNOMEDCTBEGRIFFE, ICD10BEGRIFFE, KRITERIEN, HINWEISE, SYNONYM, ICPC2ID)
     * Entspricht CodeSystem.concept.property.valueString
     */
    @Column(name = "cscp_value_string", length = 1000)
    private String valueString;

    /**
     * Wert als Integer (für FARBE, KOMPONENTE)
     * Entspricht CodeSystem.concept.property.valueInteger
     */
    @Column(name = "cscp_value_integer")
    private Integer valueInteger;

    /**
     * Wert als Code (für SNOMEDCTID)
     * Entspricht CodeSystem.concept.property.valueCode
     */
    @Column(name = "cscp_value_code")
    private String valueCode;
}