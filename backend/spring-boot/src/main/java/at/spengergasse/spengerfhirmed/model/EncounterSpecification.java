package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.entity.Encounter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Leere Specification für Encounter.
 * Gibt alle Einträge zurück, unabhängig von Suchparametern.
 */
public class EncounterSpecification {

    public static Specification<Encounter> buildSpecification(Map<String, String> params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Keine Filter implementiert - gibt alle zurück
            // Hier könnten zukünftig Suchparameter hinzugefügt werden

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

