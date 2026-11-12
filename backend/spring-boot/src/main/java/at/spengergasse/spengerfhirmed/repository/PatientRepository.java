package at.spengergasse.spengerfhirmed.repository;

import at.spengergasse.spengerfhirmed.entity.Patient;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

    @Repository
    public interface PatientRepository extends GenericRepository<Patient>, JpaSpecificationExecutor<Patient> {
    }