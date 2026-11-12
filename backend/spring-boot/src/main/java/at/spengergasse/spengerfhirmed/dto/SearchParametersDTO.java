package at.spengergasse.spengerfhirmed.dto;

import lombok.*;

/**
 * DTO für FHIR Search-Parameter
 * Validiert und normalisiert Suchparameter
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchParametersDTO {

    // Pagination
    private Integer count;
    private Integer page;

    // Common search parameters
    private String family;
    private String given;
    private String name;
    private String gender;
    private Boolean active;

    // Address parameters
    private String address;
    private String addressCity;
    private String addressState;
    private String addressPostalCode;
    private String addressCountry;

    // Telecom parameters
    private String telecom;
    private String phone;
    private String email;

    private String u;
    // Date parameters
    private String birthdate;

    /**
     * Hilfsmethode um leere oder whitespace-only Strings als null zu behandeln
     */
    public static String normalizeString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    /**
     * Setzt normalisierte Werte (leere Strings werden zu null)
     */
    public void normalize() {
        this.family = normalizeString(this.family);
        this.given = normalizeString(this.given);
        this.name = normalizeString(this.name);
        this.gender = normalizeString(this.gender);
        this.address = normalizeString(this.address);
        this.addressCity = normalizeString(this.addressCity);
        this.addressState = normalizeString(this.addressState);
        this.addressPostalCode = normalizeString(this.addressPostalCode);
        this.addressCountry = normalizeString(this.addressCountry);
        this.telecom = normalizeString(this.telecom);
        this.phone = normalizeString(this.phone);
        this.email = normalizeString(this.email);
        this.birthdate = normalizeString(this.birthdate);
    }
}

