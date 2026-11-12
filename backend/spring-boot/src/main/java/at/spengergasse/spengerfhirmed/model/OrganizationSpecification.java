package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.entity.Organization;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrganizationSpecification {

    public static Specification<Organization> buildSpecification(Map<String, String> params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Leere Specification - gibt alle Organizations zurück
            // Kann später erweitert werden

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

