package at.spengergasse.spengerfhirmed.controller;
import at.spengergasse.spengerfhirmed.entity.Bundle;
import at.spengergasse.spengerfhirmed.entity.ManufacturedItemDefinition;
import at.spengergasse.spengerfhirmed.model.ManufacturedItemDefinitionSpecification;
import at.spengergasse.spengerfhirmed.repository.ManufacturedRepository;
import at.spengergasse.spengerfhirmed.service.ManufacturedService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/manufactureditemdef")
@CrossOrigin(origins = "http://localhost:4200")
public class ManufacturedController extends GenericController<ManufacturedItemDefinition>{

    private final ManufacturedRepository repository;

    public ManufacturedController(ManufacturedService manufacturedService, ManufacturedRepository repository) {
        super(manufacturedService);
        this.repository = repository;
    }

    @GetMapping
    public Bundle search(
            @RequestParam Map<String, String> params,
            HttpServletRequest request) {

        int count = safeParse(params.remove("_count"), 10);
        int page = safeParse(params.remove("_page"), 1);

        Specification<ManufacturedItemDefinition> spec =
                ManufacturedItemDefinitionSpecification.buildSpecification(params);

        PageRequest pageable = PageRequest.of(page - 1, count);
        Page<ManufacturedItemDefinition> result = repository.findAll(spec, pageable);

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.searchset);
        bundle.setTotal((int) result.getTotalElements());

        String base = request.getRequestURL().toString();
        bundle.getLink().add(new Bundle.Link("self", buildUrl(base, params, page, count)));

        if (result.hasNext()) {
            bundle.getLink().add(new Bundle.Link("next", buildUrl(base, params, page + 1, count)));
        }

        if (result.hasPrevious() && page > 1) {
            bundle.getLink().add(new Bundle.Link("previous", buildUrl(base, params, page - 1, count)));
        }

        for (ManufacturedItemDefinition item : result.getContent()) {
            bundle.getEntry().add(new Bundle.Entry(base + "/" + item.getId(), item));
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
        if (sb.charAt(sb.length() - 1) == '&') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}

