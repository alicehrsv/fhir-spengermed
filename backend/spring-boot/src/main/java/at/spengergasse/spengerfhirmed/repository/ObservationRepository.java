package at.spengergasse.spengerfhirmed.repository;

import at.spengergasse.spengerfhirmed.entity.Observation;
import at.spengergasse.spengerfhirmed.entity.Substance;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationRepository extends GenericRepository<Observation>, JpaSpecificationExecutor<Observation> {
}