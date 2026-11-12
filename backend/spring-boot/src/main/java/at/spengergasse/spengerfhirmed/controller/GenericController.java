package at.spengergasse.spengerfhirmed.controller;

import at.spengergasse.spengerfhirmed.model.Resource;
import at.spengergasse.spengerfhirmed.service.GenericService;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Setter
public abstract class GenericController<T extends Resource> {

    protected GenericService<T> genericService;

    public GenericController(GenericService<T> service) {
        this.genericService = service;
    }

    // Commented out to avoid mapping conflicts with FHIR search operations in subclasses
    // Subclasses should implement their own search methods that return Bundle
    /*
    @GetMapping
    public @ResponseBody Iterable<T> getAllResources() {
        return genericService.getAllResources();
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<T> getResource(@PathVariable String id) {
        return genericService.getResource(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<T> createResource(@Valid @RequestBody T resource) {
        var saved = genericService.createResource(resource);
        return ResponseEntity.created(URI.create("api/resource/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> updateResource(@PathVariable String id, @Valid @RequestBody T resourceDetails) {
        return genericService.updateResource(id, resourceDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> createResource(resourceDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<T> deleteResource(@PathVariable String id) {
        return genericService.deleteResource(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}