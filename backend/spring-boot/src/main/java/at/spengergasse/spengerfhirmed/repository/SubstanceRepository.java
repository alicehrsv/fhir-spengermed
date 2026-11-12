package at.spengergasse.spengerfhirmed.repository;

import at.spengergasse.spengerfhirmed.entity.Substance;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubstanceRepository extends GenericRepository<Substance>, JpaSpecificationExecutor<Substance> {
}