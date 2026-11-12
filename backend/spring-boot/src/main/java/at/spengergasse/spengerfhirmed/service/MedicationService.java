package at.spengergasse.spengerfhirmed.service;

import at.spengergasse.spengerfhirmed.entity.Medication;
import at.spengergasse.spengerfhirmed.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicationService extends GenericService<Medication> {

    @Autowired
    public MedicationService(MedicationRepository repository) {
        super(repository);
    }
}