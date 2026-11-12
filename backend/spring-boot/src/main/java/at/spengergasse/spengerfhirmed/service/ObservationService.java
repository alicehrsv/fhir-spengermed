package at.spengergasse.spengerfhirmed.service;

import at.spengergasse.spengerfhirmed.entity.Observation;
import at.spengergasse.spengerfhirmed.repository.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObservationService extends GenericService<Observation> {

    @Autowired
    public ObservationService(ObservationRepository repository) {
        super(repository);
    }
}