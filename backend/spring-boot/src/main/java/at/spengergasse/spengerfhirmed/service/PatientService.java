package at.spengergasse.spengerfhirmed.service;

import at.spengergasse.spengerfhirmed.entity.Patient;
import at.spengergasse.spengerfhirmed.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService extends GenericService<Patient> {

    @Autowired
    public PatientService(PatientRepository repository) {
        super(repository);
    }
}