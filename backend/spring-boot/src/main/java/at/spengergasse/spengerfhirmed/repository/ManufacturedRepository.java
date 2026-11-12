package at.spengergasse.spengerfhirmed.repository;

import at.spengergasse.spengerfhirmed.entity.ManufacturedItemDefinition;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturedRepository extends GenericRepository<ManufacturedItemDefinition>, JpaSpecificationExecutor<ManufacturedItemDefinition> {
}
