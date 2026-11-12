package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.config.TestSecurityConfig;
import at.spengergasse.spengerfhirmed.entity.Medication;
import at.spengergasse.spengerfhirmed.model.*;
import at.spengergasse.spengerfhirmed.repository.MedicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("h2")
@Import(TestSecurityConfig.class)
@TestPropertySource(locations = "classpath:application-h2.properties")
public class MedicationRepositoryTest {

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void testSaveAndFindMedication() {
        // Create batch
        MedicationBatch batch = MedicationBatch.builder()
                .lotNumber("TEST123")
                .expirationDate(LocalDate.of(2025, 12, 31))
                .build();

        // Create form
        Coding formCoding = Coding.builder()
                .system("http://snomed.info/sct")
                .code("385219001")
                .display("Injection Solution")
                .build();

        List<Coding> formCodings = new ArrayList<>();
        formCodings.add(formCoding);

        CodeableConcept form = CodeableConcept.builder()
                .coding(formCodings)
                .text("Injection Solution")
                .build();

        // Create ingredient
        Coding ingredientCoding = Coding.builder()
                .system("http://www.nlm.nih.gov/research/umls/rxnorm")
                .code("1114879")
                .display("Dopamine")
                .build();

        List<Coding> ingredientCodings = new ArrayList<>();
        ingredientCodings.add(ingredientCoding);

        CodeableConcept ingredientConcept = CodeableConcept.builder()
                .coding(ingredientCodings)
                .build();

        Quantity numerator = Quantity.builder()
                .value(400)
                .unit("mg")
                .system("http://unitsofmeasure.org")
                .code("mg")
                .build();

        Quantity denominator = Quantity.builder()
                .value(500)
                .unit("mL")
                .system("http://unitsofmeasure.org")
                .code("mL")
                .build();

        Ratio strength = Ratio.builder()
                .numerator(numerator)
                .denominator(denominator)
                .build();

        MedicationIngredient ingredient = MedicationIngredient.builder()
                .itemCodeableConcept(ingredientConcept)
                .strength(strength)
                .isActive(true)
                .build();

        List<MedicationIngredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);

        // Create medication
        Medication medication = Medication.builder()
                .status(Medication.StatusCode.active)
                .form(form)
                .batch(batch)
                .ingredient(ingredients)
                .build();

        // Save
        Medication savedMedication = medicationRepository.save(medication);

        // Assert
        assertThat(savedMedication.getId()).isNotNull();
        assertThat(savedMedication.getStatus()).isEqualTo(Medication.StatusCode.active);
        assertThat(savedMedication.getBatch().getLotNumber()).isEqualTo("TEST123");
        assertThat(savedMedication.getIngredient()).hasSize(1);
        assertThat(savedMedication.getIngredient().get(0).getStrength().getNumerator().getValue()).isEqualTo(400);
    }

    @Test
    public void testUpdateMedication() {
        // Create and save medication
        MedicationBatch batch = MedicationBatch.builder()
                .lotNumber("ORIGINAL123")
                .expirationDate(LocalDate.of(2025, 12, 31))
                .build();

        Medication medication = Medication.builder()
                .status(Medication.StatusCode.active)
                .batch(batch)
                .ingredient(new ArrayList<>())
                .build();

        Medication saved = medicationRepository.save(medication);

        // Update
        saved.setStatus(Medication.StatusCode.inactive);
        saved.getBatch().setLotNumber("UPDATED456");

        Medication updated = medicationRepository.save(saved);

        // Assert
        assertThat(updated.getStatus()).isEqualTo(Medication.StatusCode.inactive);
        assertThat(updated.getBatch().getLotNumber()).isEqualTo("UPDATED456");
    }

    @Test
    public void testDeleteMedication() {
        // Create and save
        MedicationBatch batch = MedicationBatch.builder()
                .lotNumber("DELETE123")
                .expirationDate(LocalDate.of(2025, 12, 31))
                .build();

        Medication medication = Medication.builder()
                .status(Medication.StatusCode.active)
                .batch(batch)
                .ingredient(new ArrayList<>())
                .build();

        Medication saved = medicationRepository.save(medication);
        String id = saved.getId();

        // Delete
        medicationRepository.delete(saved);

        // Assert
        assertThat(medicationRepository.findById(id)).isEmpty();
    }

    @Test
    public void testFindAll() {
        // Create multiple medications
        for (int i = 0; i < 3; i++) {
            MedicationBatch batch = MedicationBatch.builder()
                    .lotNumber("BATCH" + i)
                    .expirationDate(LocalDate.of(2025, 12, 31))
                    .build();

            Medication medication = Medication.builder()
                    .status(Medication.StatusCode.active)
                    .batch(batch)
                    .ingredient(new ArrayList<>())
                    .build();

            medicationRepository.save(medication);
        }

        // Assert
        List<Medication> all = medicationRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(3);
    }

    // Statische Hilfsmethode zum Laden eines Medications aus JSON
    public static Medication returnOneMedicationJSON(ResourceLoader resourceLoader,
                                                     ObjectMapper om) {
        Medication medication = null;
        try {
            File dataFile =
                    resourceLoader.getResource("classpath:Medication.json").getFile();
            System.out.println("File exists: " + dataFile.exists());
            medication = om.readValue(dataFile, Medication.class);
        } catch (Exception e) {
            System.out.println("Error reading Medication JSON Object: " + e.getMessage());
        }
        return medication;
    }
}