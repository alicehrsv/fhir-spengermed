package at.spengergasse.spengerfhirmed.repository;

import at.spengergasse.spengerfhirmed.entity.CodeSystem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeSystemRepository extends GenericRepository<CodeSystem>, JpaSpecificationExecutor<CodeSystem> {
}