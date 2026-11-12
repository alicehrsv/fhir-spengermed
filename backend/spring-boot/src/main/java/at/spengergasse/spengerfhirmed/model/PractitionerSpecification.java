package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.entity.Practioner;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PractitionerSpecification {

    private static final Logger log = LoggerFactory.getLogger(PractitionerSpecification.class);

    public static Specification<Practioner> build(Map<String, String> params) {
        return (root, query, cb) -> {

            // Distinct aktivieren um Duplikate durch Joins zu vermeiden
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            params.forEach((key, value) -> {
                // Ignoriere null oder leere Werte
                if (value == null || value.trim().isEmpty()) {
                    return;
                }

                String trimmedValue = value.trim();

                try {

                switch (key) {

                    // 2. family (string)
                    case "family" -> {
                        Join<Practioner, HumanName> nameJoin = root.join("name", JoinType.LEFT);
                        String[] parts = trimmedValue.split(",");
                        List<Predicate> orPreds = new ArrayList<>();
                        for (String part : parts) {
                            String cleaned = part.trim();
                            if (!cleaned.isEmpty()) {
                                orPreds.add(cb.like(
                                        cb.lower(nameJoin.get("family")),
                                        "%" + cleaned.toLowerCase() + "%"
                                ));
                            }
                        }
                        if (!orPreds.isEmpty()) {
                            predicates.add(cb.or(orPreds.toArray(new Predicate[0])));
                        }
                    }

                    // 3. given (string) - KORRIGIERT: given ist eine @ElementCollection List<String>
                    case "given" -> {
                        try {
                            Join<Practioner, HumanName> nameJoin = root.join("name", JoinType.LEFT);
                            Join<HumanName, String> givenJoin = nameJoin.join("given", JoinType.LEFT);

                            String[] parts = trimmedValue.split(",");
                            List<Predicate> orPreds = new ArrayList<>();
                            for (String part : parts) {
                                String cleaned = part.trim();
                                if (!cleaned.isEmpty()) {
                                    orPreds.add(cb.like(
                                            cb.lower(givenJoin),
                                            "%" + cleaned.toLowerCase() + "%"
                                    ));
                                }
                            }
                            if (!orPreds.isEmpty()) {
                                predicates.add(cb.or(orPreds.toArray(new Predicate[0])));
                            }
                        } catch (Exception e) {
                            // Fallback wenn JOIN fehlschlägt
                        }
                    }

                    // 5. gender (token)
                    case "gender" ->
                            predicates.add(
                                    cb.equal(root.get("gender"), trimmedValue)
                            );

                    // 5.5. birthdate (date)
                    case "birthdate" -> {
                        try {
                            java.util.Date searchDate = java.sql.Date.valueOf(trimmedValue);
                            predicates.add(cb.equal(root.get("birthDate"), searchDate));
                        } catch (Exception e) {
                            log.warn("Invalid date format for birthdate: {}", trimmedValue);
                        }
                    }

                    // 6. address (fulltext) - KORRIGIERT: line ist eine @ElementCollection List<String>
                    case "address" -> {
                        try {
                            Join<Practioner, Adress> addrJoin = root.join("address", JoinType.LEFT);
                            Join<Adress, String> lineJoin = addrJoin.join("line", JoinType.LEFT);
                            String v = "%" + trimmedValue.toLowerCase() + "%";

                            predicates.add(cb.or(
                                    cb.like(cb.lower(addrJoin.get("city")), v),
                                    cb.like(cb.lower(addrJoin.get("state")), v),
                                    cb.like(cb.lower(addrJoin.get("country")), v),
                                    cb.like(cb.lower(addrJoin.get("district")), v),
                                    cb.like(cb.lower(lineJoin), v),
                                    cb.like(cb.lower(addrJoin.get("postalcode")), v)
                            ));
                        } catch (Exception e) {
                            // Fallback wenn JOIN fehlschlägt
                        }
                    }


                    // 8. telecom (string - sucht in allen Telecom-Werten)
                    case "telecom" -> {
                        Join<Practioner, ContactPoint> telecomJoin = root.join("telecom", JoinType.LEFT);
                        predicates.add(cb.like(
                                cb.lower(telecomJoin.get("value")),
                                "%" + trimmedValue.toLowerCase() + "%"
                        ));
                    }

                    // 9. communication (string - sucht in CodeableConcept text)
                    case "communication" -> {
                        try {
                            Join<Practioner, CodeableConcept> commJoin = root.join("communication", JoinType.LEFT);
                            predicates.add(cb.like(
                                    cb.lower(commJoin.get("text")),
                                    "%" + trimmedValue.toLowerCase() + "%"
                            ));
                        } catch (Exception e) {
                            log.warn("Error processing communication parameter: {}", e.getMessage());
                        }
                    }

                    // 10. qualification (string - sucht in Qualification code oder display)
                    case "qualification" -> {
                        try {
                            Join<Practioner, Qualification> qualJoin = root.join("qualification", JoinType.LEFT);
                            Join<Qualification, CodeableConcept> codeJoin = qualJoin.join("code", JoinType.LEFT);
                            predicates.add(cb.like(
                                    cb.lower(codeJoin.get("text")),
                                    "%" + trimmedValue.toLowerCase() + "%"
                            ));
                        } catch (Exception e) {
                            log.warn("Error processing qualification parameter: {}", e.getMessage());
                        }
                    }

                    // 11. attachment (string - sucht in Attachment contentType oder title)
                    case "attachment" -> {
                        try {
                            Join<Practioner, Atachment> attachJoin = root.join("attachment", JoinType.LEFT);
                            predicates.add(cb.or(
                                    cb.like(cb.lower(attachJoin.get("contentType")), "%" + trimmedValue.toLowerCase() + "%"),
                                    cb.like(cb.lower(attachJoin.get("title")), "%" + trimmedValue.toLowerCase() + "%")
                            ));
                        } catch (Exception e) {
                            log.warn("Error processing attachment parameter: {}", e.getMessage());
                        }
                    }
                }
                } catch (Exception e) {
                    log.warn("Error processing search parameter '{}' with value '{}': {}", key, trimmedValue, e.getMessage());
                    // Continue with next parameter
                }
            });

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
