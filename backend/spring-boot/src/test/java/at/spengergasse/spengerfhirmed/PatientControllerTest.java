package at.spengergasse.spengerfhirmed;
import at.spengergasse.spengerfhirmed.config.TestSecurityConfig;
import at.spengergasse.spengerfhirmed.model.Narrative;
import at.spengergasse.spengerfhirmed.entity.Patient;
import at.spengergasse.spengerfhirmed.repository.PatientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Macht aus der Klasse eine Testklasse
@SpringBootTest
//Darüber werden Anfragen an den Controller geschickt und die Antworten
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Import(TestSecurityConfig.class)
@TestPropertySource(locations = "classpath:application-h2.properties")
public class PatientControllerTest {





    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper om;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ResourceLoader resourceLoader;
//Erster Test, um alle Patienten unter der URL /api/patient mit GET

    //andExpect überprüft, ob der zurückgegebene Status 200 (OK) ist.
    @Test
    public void
    getAllPatients() {
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.get("/api/patient/"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getAPatient() {
        // Vorbereiten eines Patienten mit der ID 1
        Patient patient = new Patient();
        patient.setActive(true);
        patient.setGender(Patient.GenderCode.male);
        patient = patientRepository.save(patient); // Speichern in der Testdatenbank

        assertNotNull(patient.getId()); // Sicherstellen, dass die ID generiert wurde

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/patient/" + patient.getId()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void
    postAPatient() {
        Patient patient = PatientRepositoryTest.returnOnePatient();
        String json = null;
        try {
            json = om.writeValueAsString(patient);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/api/patient/")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void
    putAPatient() {

        Patient patientSave =
                PatientRepositoryTest.returnOnePatientJSON(resourceLoader, om);
        patientSave.setId(null);
        Patient patient = patientRepository.save(patientSave);
        assertNotNull(patient);

        patient.setActive(!patient.getActive());
        patient.setGender(Patient.GenderCode.unknown);
        String json = null;
        try {
            json = om.writeValueAsString(patient);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.put("/api/patient/" + patient.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    //Der Patient wird geöscht.
//Die id muss es bereits geben.
//Erwartete Antwort ist 200 (OK)
    @Test
    public void
    deleteAPatient() {
        Patient p = PatientRepositoryTest.returnOnePatient();
        Patient pWithId = patientRepository.save(p);
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.delete("/api/patient/" + pWithId.getId()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void
    postInvalidDeceasedPatient() {
        Patient patient = PatientRepositoryTest.returnOnePatient();
//patient.setDeceasedDateTime(LocalDateTime.now());
        patient.setDeceasedDateTime(LocalDateTime.now());
        patient.setDeceasedBoolean(true);
        String json = null;
        try {
            json = om.writeValueAsString(patient);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/api/patient/")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void
    postInvalidNarrativePatient() {
        Patient patient = PatientRepositoryTest.returnOnePatient();
        Narrative narrative = new Narrative();
        narrative.setStatus(null);
        narrative.setDiv(null);
        patient.setText(narrative);
        String json = null;
        try {
            json = om.writeValueAsString(patient);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/api/patient/")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchPatients() throws Exception {
        // Arrange - save test patient
        Patient testPatient = PatientRepositoryTest.returnOnePatientJSON(resourceLoader, om);
        testPatient.setId(null); // ID sollte null sein, damit eine neue generiert wird
        patientRepository.save(testPatient);

        // Act & Assert - search by family name
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("family", "Chalmers")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").isArray());

        // Search by gender
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("gender", "male"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));

        // Search by birthdate
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("birthdate", "1974-12-25"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"));

        // Search with pagination
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("_count", "5")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.link[?(@.relation=='self')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").isArray());

        // Search without parameters - should return all patients
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("_count", "10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").exists());
    }



}

