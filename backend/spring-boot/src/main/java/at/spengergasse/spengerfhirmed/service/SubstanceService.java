package at.spengergasse.spengerfhirmed.service;

import at.spengergasse.spengerfhirmed.entity.Substance;
import at.spengergasse.spengerfhirmed.repository.SubstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubstanceService extends GenericService<Substance> {

    @Autowired
    public SubstanceService(SubstanceRepository repository) {
        super(repository);
    }
}