package at.spengergasse.spengerfhirmed.Validator;

import at.spengergasse.spengerfhirmed.entity.Patient;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class SVNRValidator implements ConstraintValidator<SVNRValid, Patient> {

    @Override
    public void initialize(SVNRValid constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(Patient patient, ConstraintValidatorContext context) {
        if (patient == null) {
            return true;
        }

        boolean isValid = true;

        // 1. Check deceased fields
        if (patient.getDeceasedBoolean() != null &&
                patient.getDeceasedDateTime() != null) {

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Patient cannot have both deceasedBoolean and deceasedDateTime set")
                    .addConstraintViolation();
            isValid = false;
        }

        // 2. Check SVNR if present
        String svnr = getSvnrFromPatient(patient);
        if (svnr != null && !isValidAustrianSvnr(svnr)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "SVNR format is invalid")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }

    private String getSvnrFromPatient(Patient patient) {
        if (patient.getIdentifier() == null || patient.getIdentifier().isEmpty()) {
            return null;
        }

        return patient.getIdentifier().stream()
                .filter(identifier -> identifier.getSystem() != null &&
                        (identifier.getSystem().contains("svnr") ||
                                identifier.getSystem().contains("sozialversicherung")))
                .map(identifier -> identifier.getValue())
                .findFirst()
                .orElse(null);
    }

    /**
     * Validate Austrian SVNR according to official format
     * Structure: 10 digits total
     * - Digits 1-3: Sequential number
     * - Digit 4: Check digit
     * - Digits 5-10: Birth date (TTMMJJ)
     */
    private boolean isValidAustrianSvnr(String svnr) {
        if (svnr == null || svnr.trim().isEmpty()) {
            return true; // Empty is valid, @NotNull should handle required validation
        }

        // Clean input
        String cleanSvnr = svnr.replaceAll("[\\s-]", "");

        // Must be exactly 10 digits
        if (!cleanSvnr.matches("\\d{10}")) {
            return false;
        }

        try {
            // Extract parts
            String sequentialNumber = cleanSvnr.substring(0, 3);
            int checkDigit = Integer.parseInt(cleanSvnr.substring(3, 4));
            String birthDatePart = cleanSvnr.substring(4);

            // Validate birth date part (TTMMJJ)
            if (!isValidBirthDatePart(birthDatePart)) {
                return false;
            }

            // Calculate and verify check digit
            int calculatedCheckDigit = calculateCheckDigit(sequentialNumber + birthDatePart);
            return checkDigit == calculatedCheckDigit;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate birth date part (TTMMJJ format)
     */
    private boolean isValidBirthDatePart(String birthDatePart) {
        if (birthDatePart.length() != 6) {
            return false;
        }

        try {
            int day = Integer.parseInt(birthDatePart.substring(0, 2));
            int month = Integer.parseInt(birthDatePart.substring(2, 4));
            int year = Integer.parseInt(birthDatePart.substring(4, 6));

            // Basic range checks
            if (day < 1 || day > 31 || month < 1 || month > 12) {
                return false;
            }

            // Convert 2-digit year to 4-digit (adjust logic as needed)
            int fullYear = year < 50 ? 2000 + year : 1900 + year;

            // Try to create a valid date
            LocalDate.of(fullYear, month, day);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calculate Austrian SVNR check digit
     * Uses modulo 11 algorithm with specific weights
     */
    private int calculateCheckDigit(String nineDitits) {
        int[] weights = {3, 7, 9, 5, 8, 4, 2, 1, 6}; // Austrian SVNR weights
        int sum = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(nineDitits.charAt(i));
            sum += digit * weights[i];
        }

        int remainder = sum % 11;
        return remainder < 2 ? remainder : 11 - remainder;
    }
}