package at.spengergasse.spengerfhirmed.repository;

import at.spengergasse.spengerfhirmed.model.Greeting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GreetingRepository extends CrudRepository<Greeting, Long> {
    //Alle Greetign finden, die einen gewissen Counterwert haben
    //Sortiert, Absteigend nach Id
    //was ist das interface?

    List<Greeting> findGreetingByCounterOrderByIdDesc(long counter);


}

