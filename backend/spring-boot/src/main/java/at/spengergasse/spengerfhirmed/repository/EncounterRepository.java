package at.spengergasse.spengerfhirmed.repository;

import at.spengergasse.spengerfhirmed.entity.Encounter;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EncounterRepository extends GenericRepository<Encounter>, JpaSpecificationExecutor<Encounter> {

}