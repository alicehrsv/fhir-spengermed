package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.entity.Patient;
import at.spengergasse.spengerfhirmed.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestPropertySource(locations = "classpath:application-h2.properties")
public class PatientControllerSearchTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void getAllPatients() {
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.get("/api/patient"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.entry.[0].resource.id").exists())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchPatients() throws Exception {
        // Arrange - save test patient
        Patient testPatient = PatientRepositoryTest.returnOnePatientJSON(resourceLoader, om);
        patientRepository.save(testPatient);

        // Act & Assert - search by family name (Nachname)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("family", "Chalmers")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists());

        // Search by gender (Geschlecht)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("gender", "male"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));

        // Search by birthdate (Geburtsdatum)
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists());

        // Search without parameters - should return all patients
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("_count", "10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").exists());
    }

    @Test
    public void testSearchByGiven() throws Exception {
        // Search by given name (Vorname)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("given", "Peter")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    public void testSearchByAddress() throws Exception {
        // Search by address (Adresse - Volltext)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("address", "Musterstadt")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    public void testSearchByTelecom() throws Exception {
        // Search by telecom value (Kontakt)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("telecom", "555")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    public void testSearchMultipleParameters() throws Exception {
        // Search with multiple parameters
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("family", "Chalmers")
                        .param("gender", "male")
                        .param("_count", "5")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link").exists());
    }

    @Test
    public void testPaginationLinks() throws Exception {
        // Test pagination with sufficient data
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patient")
                        .param("_count", "2")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link[?(@.relation=='self')]").exists());
    }
}

