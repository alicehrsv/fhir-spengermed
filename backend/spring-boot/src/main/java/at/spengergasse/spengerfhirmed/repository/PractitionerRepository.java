package at.spengergasse.spengerfhirmed.repository;

import org.springframework.stereotype.Repository;
import at.spengergasse.spengerfhirmed.entity.Practioner;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface PractitionerRepository extends GenericRepository<Practioner>, JpaSpecificationExecutor<Practioner> {
}