package at.spengergasse.spengerfhirmed.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * DTO für Patient Search Parameter nach FHIR-Standard
 * 8 Suchparameter:
 * 1. family - Nachname
 * 2. given - Vorname
 * 3. gender - Geschlecht
 * 4. birthdate - Geburtsdatum
 * 5. active - Aktiv-Status
 * 6. address-city - Stadt
 * 7. telecom - Telefon/Email
 * 8. name - Volltext-Namenssuche
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientSearchParams {

    // 1. family - Nachname (String)
    private String family;

    // 2. given - Vorname (String)
    private String given;

    // 3. gender - Geschlecht (Token)
    private String gender;

    // 4. birthdate - Geburtsdatum (Date)
    private String birthdate;

    // 5. active - Aktiv-Status (Boolean)
    private Boolean active;

    // 6. address-city - Stadt (String)
    private String addressCity;

    // 7. telecom - Telefon/Email (String)
    private String telecom;

    // 8. name - Volltext-Namenssuche (String)
    private String name;

    // Pagination
    private Integer page;
    private Integer count;

    /**
     * Prüft ob der String-Parameter gültig ist (nicht null und nicht leer)
     */
    public static boolean isValid(String param) {
        return param != null && !param.trim().isEmpty();
    }
}

