package at.spengergasse.spengerfhirmed.service;

import at.spengergasse.spengerfhirmed.entity.Organization;
import at.spengergasse.spengerfhirmed.entity.Patient;
import at.spengergasse.spengerfhirmed.repository.OrganizationRepository;
import at.spengergasse.spengerfhirmed.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService extends GenericService<Organization>{
    @Autowired
    public OrganizationService(OrganizationRepository organizationrepository) {
        super(organizationrepository);
    }
}
