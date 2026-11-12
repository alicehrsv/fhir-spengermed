package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.entity.Medication;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class MedicationSpecification {

    public static Specification<Medication> buildSpecification(Map<String, String> params) {
        return (root, query, cb) -> {
            query.distinct(true);

            // Leere Specification - gibt alle Medikamente zurück
            List<Predicate> predicates = new ArrayList<>();

            // Hier könnten in Zukunft Such-Parameter hinzugefügt werden
            // z.B. case "code" -> predicates.add(...)

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}