package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.entity.Observation;
import at.spengergasse.spengerfhirmed.model.*;
import at.spengergasse.spengerfhirmed.repository.ObservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ObservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObservationRepository observationRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        observationRepository.deleteAll();
    }

    @Test
    void testGetAllObservations() throws Exception {
        // Given: Create test observation
        Observation obs = createTestObservation();
        observationRepository.save(obs);

        // When & Then
        mockMvc.perform(get("/api/observation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("searchset"))
                .andExpect(jsonPath("$.total").value(greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.entry", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testGetObservationById() throws Exception {
        // Given
        Observation obs = createTestObservation();
        obs = observationRepository.save(obs);

        // When & Then
        mockMvc.perform(get("/api/observation/" + obs.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(obs.getId()))
                .andExpect(jsonPath("$.status").value("_final"));
    }

    @Test
    void testCreateObservation() throws Exception {
        // Given
        Observation newObs = createTestObservation();
        String json = objectMapper.writeValueAsString(newObs);

        // When & Then
        mockMvc.perform(post("/api/observation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("_final"))
                .andExpect(jsonPath("$.code").exists());

        // Verify in database
        assertThat(observationRepository.count()).isGreaterThan(0);
    }

    @Test
    void testUpdateObservation() throws Exception {
        // Given
        Observation obs = createTestObservation();
        obs = observationRepository.save(obs);

        // Modify
        obs.setStatus(Observation.StatusCode.amended);
        String json = objectMapper.writeValueAsString(obs);

        // When & Then
        mockMvc.perform(put("/api/observation/" + obs.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("amended"));

        // Verify in database
        Observation updated = observationRepository.findById(obs.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(Observation.StatusCode.amended);
    }

    @Test
    void testDeleteObservation() throws Exception {
        // Given
        Observation obs = createTestObservation();
        obs = observationRepository.save(obs);
        String id = obs.getId();

        // When & Then
        mockMvc.perform(delete("/api/observation/" + id))
                .andExpect(status().isOk());

        // Verify deletion
        assertThat(observationRepository.findById(id)).isEmpty();
    }

    @Test
    void testSearchByStatus() throws Exception {
        // Given
        Observation obs1 = createTestObservation();
        obs1.setStatus(Observation.StatusCode._final);
        observationRepository.save(obs1);

        Observation obs2 = createTestObservation();
        obs2.setStatus(Observation.StatusCode.preliminary);
        observationRepository.save(obs2);

        // When & Then
        mockMvc.perform(get("/api/observation?status=_final"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entry[*].resource.status", everyItem(is("_final"))));
    }

    @Test
    void testSearchByCode() throws Exception {
        // Given
        Observation obs = createTestObservation();
        observationRepository.save(obs);

        // When & Then
        mockMvc.perform(get("/api/observation?code=creatinine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(greaterThanOrEqualTo(1)));
    }

    @Test
    void testSearchBySubject() throws Exception {
        // Given
        Observation obs = createTestObservation();
        observationRepository.save(obs);

        // When & Then
        mockMvc.perform(get("/api/observation?subject=Patient/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(greaterThanOrEqualTo(1)));
    }

    @Test
    void testPagination() throws Exception {
        // Given: Create multiple observations
        for (int i = 0; i < 15; i++) {
            Observation obs = createTestObservation();
            observationRepository.save(obs);
        }

        // When & Then: First page
        mockMvc.perform(get("/api/observation?_count=5&_page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entry", hasSize(5)))
                .andExpect(jsonPath("$.link[?(@.relation=='next')]").exists());

        // Second page
        mockMvc.perform(get("/api/observation?_count=5&_page=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entry", hasSize(5)))
                .andExpect(jsonPath("$.link[?(@.relation=='previous')]").exists());
    }

    @Test
    void testObservationWithComponents() throws Exception {
        // Given: Observation with components (e.g., Blood Pressure)
        Observation obs = createBloodPressureObservation();
        obs = observationRepository.save(obs);

        // When & Then
        mockMvc.perform(get("/api/observation/" + obs.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.component", hasSize(2)))
                .andExpect(jsonPath("$.component[0].code").exists())
                .andExpect(jsonPath("$.component[0].valueQuantity").exists());
    }

    @Test
    void testObservationWithReferenceRange() throws Exception {
        // Given
        Observation obs = createTestObservation();
        obs = observationRepository.save(obs);

        // When & Then
        mockMvc.perform(get("/api/observation/" + obs.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referenceRange", hasSize(1)))
                .andExpect(jsonPath("$.referenceRange[0].low").exists())
                .andExpect(jsonPath("$.referenceRange[0].high").exists());
    }

    // Helper methods

    private Observation createTestObservation() {
        // Create code
        CodeableConcept code = CodeableConcept.builder()
                .text("Serum Creatinine")
                .coding(new ArrayList<>())
                .build();

        Coding coding = Coding.builder()
                .system("http://loinc.org")
                .code("2160-0")
                .display("Creatinine [Mass/volume] in Serum or Plasma")
                .build();
        code.getCoding().add(coding);

        // Create subject reference
        Reference subject = Reference.builder()
                .reference("Patient/123")
                .display("Test Patient")
                .build();

        // Create value quantity
        Quantity valueQuantity = Quantity.builder()
                .value(122.0)
                .unit("umol/L")
                .system("http://unitsofmeasure.org")
                .code("umol/L")
                .build();

        // Create reference range
        SimpleQuantity low = SimpleQuantity.builder()
                .value(64.0)
                .unit("umol/L")
                .system("http://unitsofmeasure.org")
                .code("umol/L")
                .build();

        SimpleQuantity high = SimpleQuantity.builder()
                .value(104.0)
                .unit("umol/L")
                .system("http://unitsofmeasure.org")
                .code("umol/L")
                .build();

        ObservationReferenceRange refRange = ObservationReferenceRange.builder()
                .low(low)
                .high(high)
                .build();

        // Create observation
        Observation obs = Observation.builder()
                .status(Observation.StatusCode._final)
                .code(code)
                .subject(subject)
                .effectiveDateTime(LocalDateTime.now())
                .valueQuantity(valueQuantity)
                .referenceRange(new ArrayList<>())
                .build();

        obs.getReferenceRange().add(refRange);

        return obs;
    }

    private Observation createBloodPressureObservation() {
        // Create code for BP panel
        CodeableConcept code = CodeableConcept.builder()
                .text("Blood Pressure")
                .coding(new ArrayList<>())
                .build();

        Coding coding = Coding.builder()
                .system("http://loinc.org")
                .code("85354-9")
                .display("Blood pressure panel")
                .build();
        code.getCoding().add(coding);

        // Create subject
        Reference subject = Reference.builder()
                .reference("Patient/bp-test")
                .display("BP Test Patient")
                .build();

        // Component 1: Systolic
        CodeableConcept systolicCode = CodeableConcept.builder()
                .text("Systolic Blood Pressure")
                .build();

        Quantity systolicValue = Quantity.builder()
                .value(120.0)
                .unit("mm[Hg]")
                .system("http://unitsofmeasure.org")
                .code("mm[Hg]")
                .build();

        ObservationComponent systolicComp = ObservationComponent.builder()
                .code(systolicCode)
                .valueQuantity(systolicValue)
                .build();

        // Component 2: Diastolic
        CodeableConcept diastolicCode = CodeableConcept.builder()
                .text("Diastolic Blood Pressure")
                .build();

        Quantity diastolicValue = Quantity.builder()
                .value(80.0)
                .unit("mm[Hg]")
                .system("http://unitsofmeasure.org")
                .code("mm[Hg]")
                .build();

        ObservationComponent diastolicComp = ObservationComponent.builder()
                .code(diastolicCode)
                .valueQuantity(diastolicValue)
                .build();

        // Create observation
        Observation obs = Observation.builder()
                .status(Observation.StatusCode._final)
                .code(code)
                .subject(subject)
                .effectiveDateTime(LocalDateTime.now())
                .component(new ArrayList<>())
                .build();

        obs.getComponent().add(systolicComp);
        obs.getComponent().add(diastolicComp);

        return obs;
    }
}