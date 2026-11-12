package at.spengergasse.spengerfhirmed.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * FHIR CodeSystem.property (BackboneElement)
 * Beschreibt die "Spalten" des CodeSystem – also welche Properties
 * an jedem Concept hängen können und von welchem Typ sie sind.
 *
 * Mapping: Eine Zeile hier = eine Spaltendefinition aus der Excel-Tabelle
 */
@Entity
@Table(name = "csp_codesystemproperty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CodeSystemProperty extends Element {

    public enum PropertyType {
        code, Coding, string, integer, bool, dateTime, decimal
    }

    /**
     * Identifiziert die Property (z.B. "SNOMEDCTID", "ICD10BEGRIFFE").
     * Entspricht CodeSystem.property.code
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "csp_code", nullable = false)
    private CodeCode code;

    /**
     * Menschenlesbare Beschreibung der Property.
     * Entspricht CodeSystem.property.description
     */
    @Column(name = "csp_description")
    private String description;

    /**
     * Datentyp der Property (string, integer, code, ...).
     * Entspricht CodeSystem.property.type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "csp_type", nullable = false)
    private PropertyType type;
}