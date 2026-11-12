package at.spengergasse.spengerfhirmed.service;

import at.spengergasse.spengerfhirmed.entity.ManufacturedItemDefinition;
import at.spengergasse.spengerfhirmed.repository.ManufacturedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManufacturedService extends GenericService<ManufacturedItemDefinition>{
    @Autowired
    public ManufacturedService(ManufacturedRepository manufacturedrepository) {
        super(manufacturedrepository);
    }
}
