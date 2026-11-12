package at.spengergasse.spengerfhirmed.controller;

import at.spengergasse.spengerfhirmed.entity.Bundle;
import at.spengergasse.spengerfhirmed.entity.Practioner;
import at.spengergasse.spengerfhirmed.repository.PractitionerRepository;
import at.spengergasse.spengerfhirmed.service.PractitionerService;
import at.spengergasse.spengerfhirmed.model.PractitionerSpecification;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/practitioner")
@CrossOrigin
public class PractitionerController extends GenericController<Practioner> {

    private static final Logger log = LoggerFactory.getLogger(PractitionerController.class);

    private final PractitionerRepository repository;

    public PractitionerController(
            PractitionerService service,
            PractitionerRepository repository) {
        super(service);
        this.repository = repository;
    }

    @GetMapping
    public Bundle search(
            @RequestParam Map<String, String> params,
            HttpServletRequest request) {

        log.info("Practitioner search called with params: {}", params);

        try {
            int count = parse(params.remove("_count"), 10);
            int page = parse(params.remove("_page"), 1);

            // Entferne leere oder null Parameter
            params.entrySet().removeIf(entry ->
                entry.getValue() == null || entry.getValue().trim().isEmpty()
            );

            log.debug("After filtering empty params: {}, page: {}, count: {}", params, page, count);

            Pageable pageable = PageRequest.of(page - 1, count);
            Specification<Practioner> spec =
                    PractitionerSpecification.build(params);

            Page<Practioner> result = repository.findAll(spec, pageable);

            log.debug("Found {} practitioners, total: {}", result.getNumberOfElements(), result.getTotalElements());

            Bundle bundle = new Bundle();
            bundle.setType(Bundle.BundleType.searchset);
            bundle.setTotal((int) result.getTotalElements());

            String base = request.getRequestURL().toString();

            bundle.getLink().add(new Bundle.Link(
                    "self", base + "?_page=" + page + "&_count=" + count));

            // next-Link
            if (result.hasNext()) {
                bundle.getLink().add(new Bundle.Link("next",
                        base + "?_page=" + (page + 1) + "&_count=" + count));
            }

            // previous-Link
            if (result.hasPrevious() && page > 1) {
                bundle.getLink().add(new Bundle.Link("previous",
                        base + "?_page=" + (page - 1) + "&_count=" + count));
            }

            for (Practioner p : result.getContent()) {
                bundle.getEntry().add(
                        new Bundle.Entry(base + "/" + p.getId(), p));
            }

            return bundle;
        } catch (Exception e) {
            log.error("Error during practitioner search with params: {}", params, e);
            // Rückgabe eines leeren Bundles im Fehlerfall
            Bundle errorBundle = new Bundle();
            errorBundle.setType(Bundle.BundleType.searchset);
            errorBundle.setTotal(0);
            return errorBundle;
        }
    }

    private int parse(String v, int def) {
        try { return v == null ? def : Integer.parseInt(v); }
        catch (Exception e) { return def; }
    }
}
