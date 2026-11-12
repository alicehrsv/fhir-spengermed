package at.spengergasse.spengerfhirmed.dto;

import at.spengergasse.spengerfhirmed.model.CodeCode;
import at.spengergasse.spengerfhirmed.entity.CodeSystem.CodeSystemContentMode;
import at.spengergasse.spengerfhirmed.entity.CodeSystem.PublicationStatus;
import at.spengergasse.spengerfhirmed.model.CodeSystemProperty.PropertyType;
import at.spengergasse.spengerfhirmed.model.Resource.LANGUAGE_CODE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO für die FHIR CodeSystem Ressource.
 * Wird für API-Requests (POST/PUT) und API-Responses (GET) verwendet.
 *
 * Bildet die FHIR JSON-Struktur ab:
 * {
 *   "resourceType": "CodeSystem",
 *   "url": "https://termgit.elga.gv.at/CodeSystem/kl-oegam-primarycarecode",
 *   "version": "1.0.0+20230915",
 *   "name": "kl-oegam-primarycarecode",
 *   "title": "KL ÖGAM PrimaryCareCodes",
 *   "status": "active",
 *   "content": "supplement",
 *   "supplements": "http://snomed.info/sct",
 *   "property": [ { "code": "SNOMEDCTID", "type": "code" }, ... ],
 *   "concept": [{
 *       "code": "2134003",
 *       "display": "Diffuser Schmerz",
 *       "designation": [ { "value": "Unspezifischer Schmerz" }, ... ],
 *       "property": [ { "code": "ICPC2ID", "valueString": "A01" }, ... ]
 *   }]
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSystemDTO {

    // Immer "CodeSystem" – FHIR resourceType
    @Builder.Default
    private String resourceType = "CodeSystem";

    // Vom Server generierte ID
    private String id;

    // Header-Felder (aus Excel "Hinweise"-Blatt)
    private String url;
    private String version;
    private String name;
    private String title;
    private PublicationStatus status;
    private String description;
    private String copyright;
    private CodeSystemContentMode content;
    private String supplements;

    // Spaltendefinitionen (CodeSystem.property)
    @Builder.Default
    private List<PropertyDefinitionDTO> property = new ArrayList<>();

    // Begriffe/Einträge (CodeSystem.concept)
    @Builder.Default
    private List<ConceptDTO> concept = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Nested DTOs
    // -------------------------------------------------------------------------

    /**
     * Entspricht CodeSystem.property (Spaltendefinition)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PropertyDefinitionDTO {
        private CodeCode code;
        private String description;
        private PropertyType type;
    }

    /**
     * Entspricht CodeSystem.concept (eine Zeile der Excel-Tabelle)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ConceptDTO {
        // code = SNOMED CT ID (Primary Key der Zeile)
        private String code;
        // display = "Begriffe"-Spalte (Eigenentwicklung/Anzeigename)
        private String display;
        // Synonyme aus den 8 Synonym-Spalten (concept.designation)
        @Builder.Default
        private List<DesignationDTO> designation = new ArrayList<>();
        // Weitere Properties: SNOMEDCTID, SNOMEDCTBEGRIFFE, ICPC2ID, ...
        @Builder.Default
        private List<ConceptPropertyDTO> property = new ArrayList<>();
    }

    /**
     * Entspricht CodeSystem.concept.designation (ein Synonym-Eintrag)
     * Mapping: je eine der 8 Synonym-Spalten aus der Excel-Tabelle
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DesignationDTO {
        // Sprache des Synonyms (optional)
        private LANGUAGE_CODE language;
        // Art der Designation z.B. "synonym"
        private String useCode;
        private String useDisplay;
        // Der Synonym-Text
        private String value;
    }

    /**
     * Entspricht CodeSystem.concept.property (ein Property-Wert)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ConceptPropertyDTO {
        // Welche Spalte (z.B. SNOMEDCTID, ICD10BEGRIFFE, ICPC2ID, ...)
        private CodeCode code;
        // value[x] – genau eines davon ist befüllt je nach PropertyType:
        private String valueString;   // SNOMEDCTBEGRIFFE, ICD10BEGRIFFE, KRITERIEN, HINWEISE, ICPC2ID
        private Integer valueInteger; // FARBE, KOMPONENTE
        private String valueCode;     // SNOMEDCTID
    }
}