package at.spengergasse.spengerfhirmed.service;

import at.spengergasse.spengerfhirmed.entity.Encounter;
import at.spengergasse.spengerfhirmed.repository.EncounterRepository;
import org.springframework.stereotype.Service;

@Service
public class EncounterService extends GenericService<Encounter> {

    public EncounterService(EncounterRepository encounterRepository) {
        super(encounterRepository);
    }


}