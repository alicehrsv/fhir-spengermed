package at.spengergasse.spengerfhirmed.repository;

import at.spengergasse.spengerfhirmed.entity.Medication;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends GenericRepository<Medication>, JpaSpecificationExecutor<Medication> {
}