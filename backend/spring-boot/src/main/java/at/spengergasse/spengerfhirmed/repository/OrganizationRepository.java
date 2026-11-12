package at.spengergasse.spengerfhirmed.repository;

import at.spengergasse.spengerfhirmed.entity.Organization;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends GenericRepository<Organization>, JpaSpecificationExecutor<Organization> {
}
