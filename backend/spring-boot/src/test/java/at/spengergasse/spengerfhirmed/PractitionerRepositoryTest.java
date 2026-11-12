package at.spengergasse.spengerfhirmed;


import at.spengergasse.spengerfhirmed.entity.Practioner;
import at.spengergasse.spengerfhirmed.repository.PractitionerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PractitionerRepositoryTest {

    @Autowired
    private PractitionerRepository practitionerRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResourceLoader resourceLoader;

    public static void returnOnePractitioner() {
    }

    public static Practioner returnOnePractitionerJSON(ResourceLoader resourceLoader, ObjectMapper om) {
        Practioner p = null;

        try {
            File dataFile = resourceLoader.getResource("classpath:Practitioner.json").getFile();
            p = om.readValue(dataFile, Practioner.class);
        } catch (Exception e) {
            System.out.println("Error reading JSON Object: " + e.getMessage());
        }

        return p;
    }


    @Test
    void testSaveAndFindPractitioner() {
        // Create a practitioner
        Practioner practitioner = new Practioner();
        practitioner.setActive(true);
        practitioner.setGender("male");
        practitioner.setBirthDate(LocalDate.of(1980, 5, 15));

        // Save the practitioner
        Practioner savedPractitioner = practitionerRepository.save(practitioner);

        // Retrieve the practitioner by ID
        // Wenn die ID ein Long ist:
        // Korrektur: Stelle sicher, dass die ID als String behandelt wird.
        Practioner foundPractitioner = practitionerRepository.findById(savedPractitioner.getId()).orElse(null);

        // Assert that the practitioner is the same
        assertEquals(savedPractitioner.getId(), foundPractitioner.getId());
        assertEquals(savedPractitioner.getGender(), foundPractitioner.getGender());
    }
}