package at.spengergasse.spengerfhirmed.service;

import at.spengergasse.spengerfhirmed.entity.CodeSystem;
import at.spengergasse.spengerfhirmed.repository.CodeSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeSystemService extends GenericService<CodeSystem> {

    @Autowired
    public CodeSystemService(CodeSystemRepository repository) {
        super(repository);
    }
}