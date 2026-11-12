package at.spengergasse.spengerfhirmed.controller;

import at.spengergasse.spengerfhirmed.entity.Bundle;
import at.spengergasse.spengerfhirmed.entity.Medication;
import at.spengergasse.spengerfhirmed.model.MedicationSpecification;
import at.spengergasse.spengerfhirmed.repository.MedicationRepository;
import at.spengergasse.spengerfhirmed.service.MedicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/medication")
@CrossOrigin(origins = "http://localhost:4200")
public class MedicationController extends GenericController<Medication> {

    private final MedicationRepository medicationRepository;

    public MedicationController(MedicationService medicationService, MedicationRepository medicationRepository) {
        super(medicationService);
        this.medicationRepository = medicationRepository;
    }

    // Removed duplicate mappings - GenericController already handles /{id} and POST /

    @GetMapping
    public Bundle search(
            @RequestParam Map<String, String> params,
            HttpServletRequest request) {

        // Parse pagination parameters
        int count = safeParse(params.remove("_count"), 10);
        int page = safeParse(params.remove("_page"), 1);

        // Build specification (empty for now, returns all)
        Specification<Medication> spec = MedicationSpecification.buildSpecification(params);

        // Execute query
        PageRequest pageable = PageRequest.of(page - 1, count);
        Page<Medication> resultPage = medicationRepository.findAll(spec, pageable);

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
        for (Medication m : resultPage.getContent()) {
            bundle.getEntry().add(new Bundle.Entry(baseUrl + "/" + m.getId(), m));
        }

        return bundle;
    }

    private int safeParse(String raw, int fallback) {
        try {
            return raw == null ? fallback : Integer.parseInt(raw);
        } catch (Exception e) {
            return fallback;
        }
    }

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