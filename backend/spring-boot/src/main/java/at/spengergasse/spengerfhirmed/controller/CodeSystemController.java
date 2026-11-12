package at.spengergasse.spengerfhirmed.controller;

import at.spengergasse.spengerfhirmed.entity.Bundle;
import at.spengergasse.spengerfhirmed.entity.CodeSystem;
import at.spengergasse.spengerfhirmed.model.CodeSystemSpecification;
import at.spengergasse.spengerfhirmed.repository.CodeSystemRepository;
import at.spengergasse.spengerfhirmed.service.CodeSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/codesystem")
@CrossOrigin(origins = "http://localhost:4200")
public class CodeSystemController extends GenericController<CodeSystem> {

    private static final Logger log = LoggerFactory.getLogger(CodeSystemController.class);

    @Autowired
    private CodeSystemRepository codeSystemRepository;

    public CodeSystemController(CodeSystemService codeSystemService) {
        super(codeSystemService);
    }

    /**
     * FHIR-konforme Search-Operation mit Pagination.
     * Unterstützte Parameter: name, title, url, system, version, status, content, code
     * Pagination: _count, _page
     */
    @Operation(summary = "Search CodeSystems with FHIR parameters",
            description = "Search for CodeSystems using FHIR search parameters. " +
                    "Supports pagination with _count and _page parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bundle.class)))
    })
    @GetMapping
    public Bundle search(
            @RequestParam Map<String, String> params,
            HttpServletRequest request
    ) {
        log.info("CodeSystem search called with params: {}", params);

        try {
            // -------- 1. FHIR Paging Parameter verarbeiten --------
            int count = safeParse(params.remove("_count"), 10);
            int page  = safeParse(params.remove("_page"),  1);

            // Spring Data Pages sind 0-basiert, FHIR Pages sind 1-basiert
            int pageIndex = Math.max(0, page - 1);

            // -------- 1.5. Leere Parameter entfernen --------
            params.entrySet().removeIf(entry ->
                    entry.getValue() == null || entry.getValue().trim().isEmpty()
            );

            log.debug("After filtering empty params: {}, page: {}, count: {}", params, page, count);

            // -------- 2. Dynamische Specification bauen --------
            Specification<CodeSystem> spec = CodeSystemSpecification.buildSpecification(params);

            // -------- 3. Query ausführen --------
            PageRequest pageable = PageRequest.of(pageIndex, count, Sort.by("id").ascending());
            Page<CodeSystem> resultPage = codeSystemRepository.findAll(spec, pageable);

            log.debug("Found {} CodeSystems, total: {}",
                    resultPage.getNumberOfElements(), resultPage.getTotalElements());

            // -------- 4. FHIR Bundle bauen --------
            String baseUrl = request.getRequestURL().toString();

            Bundle bundle = new Bundle();
            bundle.setType(Bundle.BundleType.searchset);
            bundle.setTotal((int) resultPage.getTotalElements());

            // self-Link
            bundle.getLink().add(new Bundle.Link("self", buildUrl(baseUrl, params, page, count)));

            // next-Link
            if (resultPage.hasNext()) {
                bundle.getLink().add(new Bundle.Link("next",
                        buildUrl(baseUrl, params, page + 1, count)));
            }

            // previous-Link
            if (resultPage.hasPrevious() && page > 1) {
                bundle.getLink().add(new Bundle.Link("previous",
                        buildUrl(baseUrl, params, page - 1, count)));
            }

            // Entries
            for (CodeSystem cs : resultPage.getContent()) {
                bundle.getEntry().add(new Bundle.Entry(
                        baseUrl + "/" + cs.getId(),
                        cs
                ));
            }

            return bundle;

        } catch (Exception e) {
            log.error("Error during CodeSystem search with params: {}", params, e);
            Bundle errorBundle = new Bundle();
            errorBundle.setType(Bundle.BundleType.searchset);
            errorBundle.setTotal(0);
            return errorBundle;
        }
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