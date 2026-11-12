package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.entity.Patient;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

public class PatientSpecification {

    private static final Logger log = LoggerFactory.getLogger(PatientSpecification.class);

    public static Specification<Patient> buildSpecification(Map<String, String> params) {
        return (root, query, cb) -> {

            // Wichtig: Distinct aktivieren um Duplikate durch Joins zu vermeiden
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            for (var entry : params.entrySet()) {
                String key = entry.getKey();
                String rawValue = entry.getValue();

                // Ignoriere null oder leere Werte
                if (rawValue == null || rawValue.trim().isEmpty()) {
                    continue;
                }

                String value = rawValue.trim();

                try {

                switch (key) {

                    // ────────────── TOKEN ──────────────
                    case "gender" ->
                            predicates.add(cb.equal(root.get("gender"), Patient.GenderCode.valueOf(value)));


                    // ────────────── DATE ──────────────
                    case "birthdate" ->
                            predicates.add(buildDatePredicate(cb, root.get("birthDate"), value));


                    // ────────────── NAME SEARCH ──────────────
                    case "family" ->
                            predicates.add(buildNamePredicate(root, cb, "family", value));

                    case "given" ->
                            predicates.add(buildNameGivenPredicate(root, cb, value));


                    // ────────────── ADDRESS SEARCH ──────────────
                    case "address" ->
                            predicates.add(buildAddressFullText(root, cb, value));


                    // ────────────── TELECOM ──────────────

                    case "telecom" ->
                            predicates.add(buildTelecomAnyPredicate(root, cb, value));


                    default -> { /* ignore unknown parameters */ }
                }
                } catch (Exception e) {
                    log.warn("Error processing search parameter '{}' with value '{}': {}", key, value, e.getMessage());
                    // Continue with next parameter
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // ─────────────────────────────────────────────────────────────
    // NAME
    // ─────────────────────────────────────────────────────────────
    private static Predicate buildNamePredicate(Root<Patient> root, CriteriaBuilder cb,
                                                String field, String value) {
        Join<Patient, HumanName> join = root.join("name", JoinType.LEFT);
        String[] parts = value.split(",");

        List<Predicate> orPred = new ArrayList<>();
        for (String p : parts) {
            String cleaned = p.trim();
            if (!cleaned.isEmpty()) {
                orPred.add(cb.like(cb.lower(join.get(field)), "%" + cleaned.toLowerCase() + "%"));
            }
        }
        return orPred.isEmpty() ? cb.conjunction() : cb.or(orPred.toArray(new Predicate[0]));
    }

    /**
     * KORRIGIERT: given ist eine @ElementCollection List<String>
     */
    private static Predicate buildNameGivenPredicate(Root<Patient> root, CriteriaBuilder cb, String value) {
        try {
            Join<Patient, HumanName> nameJoin = root.join("name", JoinType.LEFT);
            Join<HumanName, String> givenJoin = nameJoin.join("given", JoinType.LEFT);

            String[] parts = value.split(",");
            List<Predicate> orPred = new ArrayList<>();
            for (String p : parts) {
                String cleaned = p.trim();
                if (!cleaned.isEmpty()) {
                    orPred.add(cb.like(cb.lower(givenJoin), "%" + cleaned.toLowerCase() + "%"));
                }
            }
            return orPred.isEmpty() ? cb.conjunction() : cb.or(orPred.toArray(new Predicate[0]));
        } catch (Exception e) {
            // Fallback wenn JOIN fehlschlägt
            return cb.conjunction();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // ADDRESS
    // ─────────────────────────────────────────────────────────────
    /**
     * KORRIGIERT: line ist eine @ElementCollection List<String>
     */
    private static Predicate buildAddressFullText(Root<Patient> root, CriteriaBuilder cb,
                                                  String value) {
        try {
            Join<Patient, Adress> join = root.join("adresses", JoinType.LEFT);
            Join<Adress, String> lineJoin = join.join("line", JoinType.LEFT);
            String v = "%" + value.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(join.get("city")), v),
                    cb.like(cb.lower(join.get("state")), v),
                    cb.like(cb.lower(join.get("country")), v),
                    cb.like(cb.lower(join.get("district")), v),
                    cb.like(cb.lower(lineJoin), v),
                    cb.like(cb.lower(join.get("postalcode")), v)
            );
        } catch (Exception e) {
            // Fallback wenn JOIN fehlschlägt
            return cb.conjunction();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // TELECOM
    // ─────────────────────────────────────────────────────────────
    private static Predicate buildTelecomAnyPredicate(Root<Patient> root, CriteriaBuilder cb,
                                                      String value) {
        Join<Patient, ContactPoint> join = root.join("telecom", JoinType.LEFT);
        return cb.like(cb.lower(join.get("value")), "%" + value.toLowerCase() + "%");
    }

    // ─────────────────────────────────────────────────────────────
    // DATE / DATETIME
    // ─────────────────────────────────────────────────────────────
    private static Predicate buildDatePredicate(
            CriteriaBuilder cb, Path<LocalDate> field, String value) {

        if (value.startsWith("gt"))
            return cb.greaterThan(field, LocalDate.parse(value.substring(2)));

        if (value.startsWith("lt"))
            return cb.lessThan(field, LocalDate.parse(value.substring(2)));

        return cb.equal(field, LocalDate.parse(value));
    }
}