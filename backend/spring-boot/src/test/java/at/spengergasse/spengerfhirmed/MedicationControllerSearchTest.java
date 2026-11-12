package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.config.TestSecurityConfig;
import at.spengergasse.spengerfhirmed.entity.Medication;
import at.spengergasse.spengerfhirmed.repository.MedicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
@Import(TestSecurityConfig.class)
@TestPropertySource(locations = "classpath:application-h2.properties")
public class MedicationControllerSearchTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @Autowired
    MedicationRepository medicationRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void getAllMedications() {
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.get("/api/medication"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchMedications() throws Exception {
        // Arrange - save test medication
        Medication testMedication = MedicationRepositoryTest.returnOneMedicationJSON(resourceLoader, om);
        if (testMedication != null) {
            medicationRepository.save(testMedication);
        }

        // Act & Assert - search with pagination
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists());

        // Search without parameters - should return all medications
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication")
                        .param("_count", "10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").exists());
    }

    @Test
    public void testSearchWithPagination() throws Exception {
        // Search with pagination
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication")
                        .param("_count", "5")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.link[?(@.relation=='self')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists());
    }

    @Test
    public void testSearchWithDifferentPageSizes() throws Exception {
        // Test with page size 2
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication")
                        .param("_count", "2")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));

        // Test with page size 20
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication")
                        .param("_count", "20")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"));
    }

    @Test
    public void testPaginationLinks() throws Exception {
        // Test pagination with sufficient data
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication")
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
    public void testSearchWithSecondPage() throws Exception {
        // Test second page
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication")
                        .param("_count", "5")
                        .param("_page", "2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link[?(@.relation=='self')]").exists());
    }

    @Test
    public void testSearchEmptyResult() throws Exception {
        // This should still return a valid Bundle structure, just with 0 total
        mockMvc.perform(MockMvcRequestBuilders.get("/api/medication")
                        .param("_count", "10")
                        .param("_page", "999")) // Very high page number
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").exists());
    }
}

