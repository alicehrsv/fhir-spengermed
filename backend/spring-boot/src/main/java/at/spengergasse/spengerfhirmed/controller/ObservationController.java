package at.spengergasse.spengerfhirmed.controller;

import at.spengergasse.spengerfhirmed.entity.Bundle;
import at.spengergasse.spengerfhirmed.entity.Observation;
import at.spengergasse.spengerfhirmed.model.ObservationSpecification;
import at.spengergasse.spengerfhirmed.repository.ObservationRepository;
import at.spengergasse.spengerfhirmed.service.ObservationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/observation")
@CrossOrigin(origins = "http://localhost:4200")
public class ObservationController extends GenericController<Observation> {

    private final ObservationRepository observationRepository;
    private final ObservationService observationService;

    public ObservationController(ObservationService observationService,
                                 ObservationRepository observationRepository) {
        super(observationService);
        this.observationRepository = observationRepository;
        this.observationService = observationService;
    }

    /**
     * Search observations with FHIR-style parameters
     */
    @GetMapping
    public Bundle search(
            @RequestParam Map<String, String> params,
            HttpServletRequest request) {

        // Parse pagination parameters
        int count = safeParse(params.remove("_count"), 10);
        int page = safeParse(params.remove("_page"), 1);

        // Build specification
        Specification<Observation> spec = ObservationSpecification.buildSpecification(params);

        // Execute query
        PageRequest pageable = PageRequest.of(page - 1, count);
        Page<Observation> resultPage = observationRepository.findAll(spec, pageable);

        // Build FHIR Bundle
        String baseUrl = request.getRequestURL().toString();

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.searchset);
        bundle.setTotal((int) resultPage.getTotalElements());

        // self-Link
        bundle.getLink().add(new Bundle.Link("self", buildUrl(baseUrl, params, page, count)));

        // next-Link
        if (resultPage.hasNext()) {
            bundle.getLink().add(new Bundle.Link("next", buildUrl(baseUrl, params, page + 1, count)));
        }

        // previous-Link
        if (resultPage.hasPrevious() && page > 1) {
            bundle.getLink().add(new Bundle.Link("previous", buildUrl(baseUrl, params, page - 1, count)));
        }

        // entries
        for (Observation obs : resultPage.getContent()) {
            bundle.getEntry().add(new Bundle.Entry(baseUrl + "/" + obs.getId(), obs));
        }

        return bundle;
    }


    /**
     * Create new observation
     */
    @PostMapping
    public ResponseEntity<Observation> create(@RequestBody Observation observation) {
        try {
            // Ensure ID is null for new entities
            observation.setId(null);

            // Use createResource from GenericService
            Observation saved = observationService.createResource(observation);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update existing observation
     */
    @PutMapping("/{id}")
    public ResponseEntity<Observation> update(
            @PathVariable String id,
            @RequestBody Observation observation) {
        try {
            // Use updateResource from GenericService
            Optional<Observation> updated = observationService.updateResource(id, observation);
            return updated.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete single observation by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        try {
            // Use deleteResource from GenericService
            boolean deleted = observationService.deleteResource(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete all observations (use with caution!)
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        try {
            observationRepository.deleteAll();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Helper method to parse integer parameters safely
     */
    private int safeParse(String raw, int fallback) {
        try {
            return raw == null ? fallback : Integer.parseInt(raw);
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Helper method to build pagination URLs
     */
    private String buildUrl(String baseUrl, Map<String, String> params, int page, int count) {
        StringBuilder sb = new StringBuilder(baseUrl).append("?");
        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        sb.append("_page=").append(page).append("&_count=").append(count);
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '&') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}