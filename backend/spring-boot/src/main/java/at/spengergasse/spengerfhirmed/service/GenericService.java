package at.spengergasse.spengerfhirmed.service;


import at.spengergasse.spengerfhirmed.model.Resource;
import at.spengergasse.spengerfhirmed.repository.GenericRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;


@NoRepositoryBean
public abstract class GenericService<T extends Resource> {
    protected final GenericRepository<T> genericRepository;

    public GenericService(GenericRepository<T> genericRepository) {
        this.genericRepository = genericRepository;
    }

    public Iterable<T> getAllResources() {
        return genericRepository.findAll();
    }

    public Optional<T> getResource(String id) {
        return genericRepository.findById(id);
    }

    public T createResource(T resource) {
        return genericRepository.save(resource);
    }

    public Optional<T> updateResource(String id, T resourceDetails) {
        return genericRepository.findById(id).map(resource -> {
            try {
                BeanUtils.copyProperties(resource, resourceDetails);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Fehler beim Kopieren der Eigenschaften", e);
            }
            return genericRepository.save(resource);
        });
    }

    public boolean deleteResource(String id) {
        return genericRepository.findById(id).map(resource -> {
            genericRepository.delete(resource);
            return true;
        }).orElse(false);
    }
    public Page<T> findAll(Pageable pageable) {
        return genericRepository.findAll(pageable);
    }


}