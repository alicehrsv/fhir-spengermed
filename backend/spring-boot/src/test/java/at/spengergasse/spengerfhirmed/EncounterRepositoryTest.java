package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.entity.Encounter;
import at.spengergasse.spengerfhirmed.entity.Patient;
import at.spengergasse.spengerfhirmed.entity.Practioner;
import at.spengergasse.spengerfhirmed.model.*;
import at.spengergasse.spengerfhirmed.repository.EncounterRepository;
import at.spengergasse.spengerfhirmed.repository.PatientRepository;
import at.spengergasse.spengerfhirmed.repository.PractitionerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EncounterRepositoryTest {

    @Autowired
    private EncounterRepository encounterRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PractitionerRepository practitionerRepository;

    private Patient testPatient;
    private Practioner testPractitioner;

    @BeforeEach
    public void setup() {
        // Test-Patient anlegen
        testPatient = Patient.builder()
                .id("example")
                .name(List.of(HumanName.builder().text("Example Patient").build()))
                .build();
        patientRepository.save(testPatient);

        // Test-Practitioner anlegen
        testPractitioner = Practioner.builder()
                .id("example")
                .name(List.of(HumanName.builder().text("Dr Adam Careful").build()))
                .build();
        practitionerRepository.save(testPractitioner);
    }

    @Test
    public void testSaveAndLoadEncounter() {
        Encounter encounter = createTestEncounter();

        // Speichern
        Encounter saved = encounterRepository.save(encounter);
        assertNotNull(saved.getId());

        // Laden
        Optional<Encounter> loadedOpt = encounterRepository.findById(saved.getId());
        assertTrue(loadedOpt.isPresent());
        Encounter loaded = loadedOpt.get();

        // Vergleich
        assertEquals(Encounter.Status.completed, loaded.getStatus());
        assertEquals(testPatient.getId(), loaded.getSubject().getReference().replace("Patient/", ""));
        assertEquals(2, loaded.getParticipant().size());

        List<String> actorRefs = loaded.getParticipant().stream()
                .map(p -> p.getActor().getReference())
                .collect(Collectors.toList());
        assertTrue(actorRefs.contains("Practitioner/example"));
        assertTrue(actorRefs.contains("Patient/example"));
    }

    @Test
    public void testUpdateEncounter() {
        Encounter encounter = createTestEncounter();
        Encounter saved = encounterRepository.save(encounter);

        // Status ändern (Enum-Konstante verwenden!)
        saved.setStatus(Encounter.Status.in_progress);
        Encounter updated = encounterRepository.save(saved);

        assertEquals(Encounter.Status.in_progress, updated.getStatus());
    }

    @Test
    public void testDeleteEncounter() {
        Encounter encounter = createTestEncounter();
        Encounter saved = encounterRepository.save(encounter);

        encounterRepository.deleteById(saved.getId());
        assertFalse(encounterRepository.findById(saved.getId()).isPresent());
    }

    // Hilfsmethode für Testdaten (entspricht deinem JSON)
    private Encounter createTestEncounter() {
        // Teilnehmer 1: Practitioner
        Participant participant1 = Participant.builder()
                .type(List.of(CodeableConcept.builder()
                        .coding(List.of(Coding.builder()
                                .system("http://terminology.hl7.org/CodeSystem/v3-ParticipationType")
                                .code("PPRF")
                                .build()))
                        .build()))
                .period(Period.builder()
                        .start(LocalDateTime.of(2015, 1, 17, 16, 0))
                        .end(LocalDateTime.of(2015, 1, 17, 16, 30))
                        .build())
                .actor(Reference.builder()
                        .reference("Practitioner/example")
                        .display("Dr Adam Careful")
                        .build())
                .build();

        // Teilnehmer 2: Patient
        Participant participant2 = Participant.builder()
                .actor(Reference.builder()
                        .reference("Patient/example")
                        .build())
                .build();

        return Encounter.builder()
                .status(Encounter.Status.completed)
                .subject(Reference.builder()
                        .reference("Patient/example")
                        .build())
                .participant(List.of(participant1, participant2))
                .build();
    }
}