package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.entity.Observation;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObservationSpecification {

    public static Specification<Observation> buildSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Status search
            if (params.containsKey("status")) {
                try {
                    Observation.StatusCode status = Observation.StatusCode.valueOf(params.get("status"));
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                } catch (IllegalArgumentException e) {
                    // Invalid status, ignore
                }
            }

            // Code search (code.text or code.coding.display)
            if (params.containsKey("code")) {
                String codeValue = params.get("code");
                Join<Observation, CodeableConcept> codeJoin = root.join("code", JoinType.LEFT);

                Predicate textMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(codeJoin.get("text")),
                        "%" + codeValue.toLowerCase() + "%"
                );

                // Also search in coding display
                Join<CodeableConcept, Coding> codingJoin = codeJoin.join("coding", JoinType.LEFT);
                Predicate codingMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(codingJoin.get("display")),
                        "%" + codeValue.toLowerCase() + "%"
                );

                predicates.add(criteriaBuilder.or(textMatch, codingMatch));
            }

            // Subject search (patient reference)
            if (params.containsKey("subject") || params.containsKey("patient")) {
                String subjectId = params.getOrDefault("subject", params.get("patient"));
                Join<Observation, Reference> subjectJoin = root.join("subject", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        subjectJoin.get("reference"),
                        "%" + subjectId + "%"
                ));
            }

            // Category search
            if (params.containsKey("category")) {
                String categoryValue = params.get("category");
                Join<Observation, CodeableConcept> categoryJoin = root.join("category", JoinType.LEFT);

                Predicate categoryTextMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(categoryJoin.get("text")),
                        "%" + categoryValue.toLowerCase() + "%"
                );

                predicates.add(categoryTextMatch);
            }

            // Date search (effectiveDateTime)
            if (params.containsKey("date")) {
                String dateStr = params.get("date");
                try {
                    LocalDateTime date = LocalDateTime.parse(dateStr + "T00:00:00");
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            root.get("effectiveDateTime"), date
                    ));
                } catch (Exception e) {
                    // Invalid date format, ignore
                }
            }

            // Value quantity search
            if (params.containsKey("value-quantity")) {
                String valueStr = params.get("value-quantity");
                try {
                    double value = Double.parseDouble(valueStr);
                    Join<Observation, Quantity> valueJoin = root.join("valueQuantity", JoinType.LEFT);
                    predicates.add(criteriaBuilder.equal(valueJoin.get("value"), value));
                } catch (NumberFormatException e) {
                    // Invalid number, ignore
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}