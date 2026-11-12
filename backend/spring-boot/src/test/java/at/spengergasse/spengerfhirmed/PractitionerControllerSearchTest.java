package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.entity.Practioner;
import at.spengergasse.spengerfhirmed.repository.PractitionerRepository;
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
@AutoConfigureMockMvc(addFilters = false)   // <--- Security für Tests deaktivieren
@ActiveProfiles("h2")
@TestPropertySource(locations = "classpath:application-h2.properties")public class PractitionerControllerSearchTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @Autowired
    PractitionerRepository practitionerRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void getAllPractitioners() {
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.get("/api/practitioner"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchPractitioners() throws Exception {
        // Arrange - save test practitioner
        Practioner testPractitioner = PractitionerRepositoryTest.returnOnePractitionerJSON(resourceLoader, om);
        if (testPractitioner != null) {
            practitionerRepository.save(testPractitioner);
        }

        // Act & Assert - search by family name (Nachname)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("family", "Careful")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists());

        // Search by gender (Geschlecht)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("gender", "male"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));

        // Search with pagination
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("_count", "5")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.link[?(@.relation=='self')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists());

        // Search without parameters - should return all practitioners
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("_count", "10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").exists());
    }

    @Test
    public void testSearchByGiven() throws Exception {
        // Search by given name (Vorname)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("given", "Adam")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("address", "PleasantVille")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("telecom", "555")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    public void testSearchByBirthdate() throws Exception {
        // Search by birthdate (Geburtsdatum)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("birthdate", "1980-01-01")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    public void testSearchByCommunication() throws Exception {
        // Search by communication (Kommunikationen)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("communication", "English")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    public void testSearchByQualification() throws Exception {
        // Search by qualification (Qualifikationen)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("qualification", "Doctor")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    public void testSearchByAttachment() throws Exception {
        // Search by attachment (Anhänge)
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("attachment", "pdf")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("family", "Careful")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("_count", "2")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link[?(@.relation=='self')]").exists());
    }

    @Test
    public void testSearchReturnsBundle() throws Exception {
        // Verify Bundle structure
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resourceType").value("Bundle"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.link").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists());
    }

    @Test
    public void testSearchWithDifferentPageSizes() throws Exception {
        // Test with page size 2
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("_count", "2")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));

        // Test with page size 20
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("_count", "20")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"));
    }

    @Test
    public void testSearchWithSecondPage() throws Exception {
        // Test second page
        mockMvc.perform(MockMvcRequestBuilders.get("/api/practitioner")
                        .param("_count", "5")
                        .param("_page", "2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link[?(@.relation=='self')]").exists());
    }
}

