package at.spengergasse.spengerfhirmed.service;

import at.spengergasse.spengerfhirmed.entity.Patient;
import at.spengergasse.spengerfhirmed.entity.Practioner;
import at.spengergasse.spengerfhirmed.repository.PatientRepository;
import at.spengergasse.spengerfhirmed.repository.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import at.spengergasse.spengerfhirmed.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PractitionerService extends GenericService<Practioner> {
    @Autowired
    public PractitionerService(PractitionerRepository repository) {
        super(repository);
    }
}
