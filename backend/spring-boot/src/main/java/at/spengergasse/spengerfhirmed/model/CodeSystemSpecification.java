package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.entity.CodeSystem;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeSystemSpecification {

    private static final Logger log = LoggerFactory.getLogger(CodeSystemSpecification.class);

    public static Specification<CodeSystem> buildSpecification(Map<String, String> params) {
        return (root, query, cb) -> {

            // Distinct aktivieren um Duplikate durch Joins zu vermeiden
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

                        // ────────────── STRING-Felder (LIKE-Suche) ──────────────

                        // name – maschinenlesbarer Name des CodeSystem
                        case "name" ->
                                predicates.add(cb.like(cb.lower(root.get("name")),
                                        "%" + value.toLowerCase() + "%"));

                        // title – menschenlesbarer Titel
                        case "title" ->
                                predicates.add(cb.like(cb.lower(root.get("title")),
                                        "%" + value.toLowerCase() + "%"));

                        // url – kanonische URI (auch als "system" suchbar – FHIR-Standard)
                        case "url", "system" ->
                                predicates.add(cb.like(cb.lower(root.get("url")),
                                        "%" + value.toLowerCase() + "%"));

                        // version – Business-Versionsnummer
                        case "version" ->
                                predicates.add(cb.equal(root.get("version"), value));

                        // ────────────── TOKEN-Felder (exakte Suche) ──────────────

                        // status – draft | active | retired | unknown
                        case "status" ->
                                predicates.add(cb.equal(root.get("status"),
                                        CodeSystem.PublicationStatus.valueOf(value)));

                        // content – not_present | example | fragment | complete | supplement
                        case "content" ->
                                predicates.add(cb.equal(root.get("content"),
                                        CodeSystem.CodeSystemContentMode.valueOf(value)));

                        // ────────────── JOIN auf concept.code ──────────────

                        // code – sucht nach einem Concept mit diesem Code (SNOMED CT ID)
                        case "code" -> {
                            Join<CodeSystem, CodeSystemConcept> conceptJoin =
                                    root.join("concept", JoinType.LEFT);
                            predicates.add(cb.equal(conceptJoin.get("code"), value));
                        }

                        default -> { /* unbekannte Parameter ignorieren */ }
                    }
                } catch (Exception e) {
                    log.warn("Error processing search parameter '{}' with value '{}': {}",
                            key, value, e.getMessage());
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}