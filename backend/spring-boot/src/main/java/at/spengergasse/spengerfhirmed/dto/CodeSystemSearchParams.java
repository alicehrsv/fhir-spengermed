package at.spengergasse.spengerfhirmed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO für CodeSystem Such-Parameter nach FHIR-Standard.
 *
 * Unterstützte Suchparameter:
 * 1. url     – kanonische URI des CodeSystem
 * 2. name    – maschinenlesbarer Name
 * 3. title   – menschenlesbarer Titel
 * 4. status  – Veröffentlichungsstatus (draft|active|retired|unknown)
 * 5. version – Business-Version
 * 6. code    – Suche nach einem bestimmten Concept-Code
 * 7. system  – Alias für url (FHIR-konform)
 * 8. content – Vollständigkeit (complete|fragment|example|...)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSystemSearchParams {

    // 1. url – kanonische URI
    private String url;

    // 2. name – maschinenlesbarer Name
    private String name;

    // 3. title – menschenlesbarer Titel
    private String title;

    // 4. status – draft | active | retired | unknown
    private String status;

    // 5. version – Business-Versionsnummer
    private String version;

    // 6. code – sucht nach einem Concept mit diesem Code
    private String code;

    // 7. system – Alias für url (FHIR-Standard-Suchparameter)
    private String system;

    // 8. content – Vollständigkeit des CodeSystem
    private String content;

    // Pagination
    private Integer page;
    private Integer count;

    /**
     * Prüft ob ein String-Parameter gültig (nicht null/leer) ist.
     */
    public static boolean isValid(String param) {
        return param != null && !param.trim().isEmpty();
    }
}