package at.spengergasse.spengerfhirmed.entity;

import at.spengergasse.spengerfhirmed.model.CodeSystemConcept;
import at.spengergasse.spengerfhirmed.model.CodeSystemProperty;
import at.spengergasse.spengerfhirmed.model.DomainResource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * FHIR CodeSystem Ressource (https://www.hl7.org/fhir/codesystem.html)
 *
 * Speichert eine komplette Terminologie (ICPC2, SNOMED CT, ICD-10).
 * Die Header-Felder (url, version, name, ...) kommen aus dem "Hinweise"-Blatt der Excel-Datei.
 * Die Concepts (Begriffe) bilden die Datenzeilen der Excel-Tabelle ab.
 *
 * 1 CodeSystem → n CodeSystemProperty  (Spaltendefinitionen)
 * 1 CodeSystem → n CodeSystemConcept   (Datenzeilen)
 * 1 CodeSystemConcept → n CodeSystemConceptProperty (Zellenwerte)
 */
@Entity
@Table(name = "cs_codesystem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CodeSystem extends DomainResource {

    /**
     * Mögliche Veröffentlichungsstatus-Werte.
     * Entspricht CodeSystem.status (1..1, required)
     */
    public enum PublicationStatus {
        draft, active, retired, unknown
    }

    /**
     * Gibt an, wie vollständig der Inhalt des CodeSystem ist.
     * Entspricht CodeSystem.content (1..1, required)
     */
    public enum CodeSystemContentMode {
        not_present, example, fragment, complete, supplement
    }

    /**
     * Kanonische URI, die dieses CodeSystem global eindeutig identifiziert.
     * Entspricht CodeSystem.url (0..1)
     * Aus Excel - Hinweise
     */
    @Column(name = "cs_url", length = 500)
    private String url;

    /**
     * Business-Versionsnummer des CodeSystem.
     * Entspricht CodeSystem.version (0..1)
     * Aus Excel - Hinweise
     */
    @Column(name = "cs_version")
    private String version;

    /**
     * Maschinenlesbarer Name (kein Leerzeichen, CamelCase empfohlen).
     * Entspricht CodeSystem.name (0..1)
     * Aus Excel - Hinweise
     */
    @Column(name = "cs_name")
    private String name;

    /**
     * Menschenlesbarer Titel des CodeSystem.
     * Entspricht CodeSystem.title (0..1)
     * Aus Excel - Hinweise
     */
    @Column(name = "cs_title")
    private String title;

    /**
     * Veröffentlichungsstatus: draft | active | retired | unknown.
     * Entspricht CodeSystem.status (1..1)
     * Aus Excel - Hinweise
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cs_status", nullable = false)
    private PublicationStatus status;

    /**
     * Natürlichsprachliche Beschreibung des CodeSystem.
     * Entspricht CodeSystem.description (0..1)
     * Aus Excel - Hinweise
     */
    @Column(name = "cs_description", length = 2000)
    private String description;

    /**
     * Copyright- und Nutzungshinweise.
     * Entspricht CodeSystem.copyright (0..1)
     * Aus Excel - Hinweise
     */
    @Column(name = "cs_copyright", length = 1000)
    private String copyright;

    /**
     * Vollständigkeit des Inhalts: not-present | example | fragment | complete | supplement
     * Entspricht CodeSystem.content (1..1)
     * Aus Excel - Hinweise
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cs_content", nullable = false)
    private CodeSystemContentMode content;

    /**
     * Nur bei content=supplement: URL des Basis-CodeSystem, das ergänzt wird.
     * Entspricht CodeSystem.supplements (0..1)
     * Aus Excel - Hinweise
     */
    @Column(name = "cs_supplements", length = 500)
    private String supplements;

    /**
     * Definitionen der Properties (Spaltenbeschreibungen).
     * Entspricht CodeSystem.property (0..*)
     * Beschreibt welche Zusatz-Properties an den Concepts hängen können.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "csp_cs_id", referencedColumnName = "id")
    private List<CodeSystemProperty> property = new ArrayList<>();

    /**
     * Die eigentlichen Begriffe/Einträge der Terminologie.
     * Entspricht CodeSystem.concept (0..*)
     * Eine Zeile der Excel-Datei = ein Concept.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "csc_cs_id", referencedColumnName = "id")
    private List<CodeSystemConcept> concept = new ArrayList<>();
}