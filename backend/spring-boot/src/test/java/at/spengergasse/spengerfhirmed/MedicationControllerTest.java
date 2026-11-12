package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.config.TestSecurityConfig;
import at.spengergasse.spengerfhirmed.entity.Medication;
import at.spengergasse.spengerfhirmed.model.*;
import at.spengergasse.spengerfhirmed.repository.MedicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Import(TestSecurityConfig.class)
@TestPropertySource(locations = "classpath:application-h2.properties")
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllMedications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetMedicationById() throws Exception {
        // Arrange - Medication in DB speichern
        MedicationBatch batch = MedicationBatch.builder()
                .lotNumber("TEST123")
                .expirationDate(LocalDate.of(2025, 12, 31))
                .build();

        Medication medication = Medication.builder()
                .status(Medication.StatusCode.active)
                .batch(batch)
                .ingredient(new ArrayList<>())
                .build();

        Medication saved = medicationRepository.save(medication);
        assertNotNull(saved.getId());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication/byId/" + saved.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(saved.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.batch.lotNumber").value("TEST123"));
    }

    @Test
    public void testCreateMedication() throws Exception {
        // Arrange
        MedicationBatch batch = MedicationBatch.builder()
                .lotNumber("NEW123")
                .expirationDate(LocalDate.of(2026, 6, 30))
                .build();

        Medication medication = Medication.builder()
                .status(Medication.StatusCode.active)
                .batch(batch)
                .ingredient(new ArrayList<>())
                .build();

        String json = objectMapper.writeValueAsString(medication);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/medication/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.batch.lotNumber").value("NEW123"));
    }

    @Test
    public void testUpdateMedication() throws Exception {
        // Arrange - Medication erstellen
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

        String json = objectMapper.writeValueAsString(saved);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/medication/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("inactive"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.batch.lotNumber").value("UPDATED456"));
    }

    @Test
    public void testDeleteMedication() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/medication/" + saved.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify deletion
        assertFalse(medicationRepository.findById(saved.getId()).isPresent());
    }

    @Test
    public void testCreateMedicationWithIngredients() throws Exception {
        // Arrange - Complete medication mit Ingredients
        Coding ingredientCoding = Coding.builder()
                .system("http://www.nlm.nih.gov/research/umls/rxnorm")
                .code("1114879")
                .display("Dopamine")
                .build();

        List<Coding> codings = new ArrayList<>();
        codings.add(ingredientCoding);

        CodeableConcept ingredientConcept = CodeableConcept.builder()
                .coding(codings)
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

        MedicationBatch batch = MedicationBatch.builder()
                .lotNumber("AT 9035 ZU69")
                .expirationDate(LocalDate.of(2030, 11, 30))
                .build();

        Medication medication = Medication.builder()
                .status(Medication.StatusCode.active)
                .batch(batch)
                .ingredient(ingredients)
                .build();

        String json = objectMapper.writeValueAsString(medication);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/medication/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredient[0].itemCodeableConcept.coding[0].display").value("Dopamine"));
    }
}