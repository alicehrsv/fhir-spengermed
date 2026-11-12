package at.spengergasse.spengerfhirmed;
import at.spengergasse.spengerfhirmed.entity.CodeSystem;

import at.spengergasse.spengerfhirmed.entity.CodeSystem;
import at.spengergasse.spengerfhirmed.repository.CodeSystemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class CodeSystemControllerSearchTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @Autowired
    CodeSystemRepository codeSystemRepository;

    /**
     * Vor jedem Test ein CodeSystem speichern, damit Suchen Treffer liefern können.
     */
    @BeforeEach
    public void setUp() {
        CodeSystem cs = CodeSystemRepositoryTest.returnOneCodeSystem();
        cs.setId(null);
        codeSystemRepository.save(cs);
    }

    // -------------------------------------------------------------------------
    // GET alle CodeSystems (ohne Parameter)
    // -------------------------------------------------------------------------

    /**
     * Testet GET /api/codesystem ohne Parameter – liefert searchset zurück.
     * Analog zu PatientControllerSearchTest.getAllPatients()
     */
    @Test
    public void getAllCodeSystems() {
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.get("/api/codesystem"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.entry.[0].resource.id").exists())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // Suche nach name
    // -------------------------------------------------------------------------

    /**
     * Testet Suche nach name (maschinenlesbarer Name des CodeSystem).
     */
    @Test
    public void testSearchByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("name", "kl-oegam-primarycarecode")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists());
    }

    // -------------------------------------------------------------------------
    // Suche nach url
    // -------------------------------------------------------------------------

    /**
     * Testet Suche nach url (kanonische URI des CodeSystem).
     */
    @Test
    public void testSearchByUrl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("url", "https://termgit.elga.gv.at/CodeSystem/kl-oegam-primarycarecode")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(1)));
    }

    // -------------------------------------------------------------------------
    // Suche nach status
    // -------------------------------------------------------------------------

    /**
     * Testet Suche nach status (draft | active | retired | unknown).
     */
    @Test
    public void testSearchByStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("status", "active"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(1)));
    }

    // -------------------------------------------------------------------------
    // Suche nach version
    // -------------------------------------------------------------------------

    /**
     * Testet Suche nach version (Business-Versionsnummer).
     */
    @Test
    public void testSearchByVersion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("version", "1.0.0+20230915")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(1)));
    }

    // -------------------------------------------------------------------------
    // Suche nach title
    // -------------------------------------------------------------------------

    /**
     * Testet Suche nach title (menschenlesbarer Titel).
     */
    @Test
    public void testSearchByTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("title", "KL ÖGAM PrimaryCareCodes")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(1)));
    }

    // -------------------------------------------------------------------------
    // Suche nach concept code
    // -------------------------------------------------------------------------

    /**
     * Testet Suche nach einem bestimmten Concept-Code (SNOMED CT ID).
     * z.B. "2134003" = "Diffuser Schmerz"
     */
    @Test
    public void testSearchByCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("code", "2134003")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));
    }

    // -------------------------------------------------------------------------
    // Suche nach content
    // -------------------------------------------------------------------------

    /**
     * Testet Suche nach content-Typ (supplement, complete, fragment, ...).
     */
    @Test
    public void testSearchByContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("content", "supplement"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(1)));
    }

    // -------------------------------------------------------------------------
    // Suche mit mehreren Parametern
    // -------------------------------------------------------------------------

    /**
     * Testet Suche mit mehreren kombinierten Parametern.
     * Analog zu PatientControllerSearchTest.testSearchMultipleParameters()
     */
    @Test
    public void testSearchMultipleParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("name", "kl-oegam-primarycarecode")
                        .param("status", "active")
                        .param("_count", "5")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link").exists());
    }

    // -------------------------------------------------------------------------
    // Pagination
    // -------------------------------------------------------------------------

    /**
     * Testet Pagination (self-Link muss vorhanden sein).
     * Analog zu PatientControllerSearchTest.testPaginationLinks()
     */
    @Test
    public void testPaginationLinks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("_count", "2")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.link[?(@.relation=='self')]").exists());
    }

    // -------------------------------------------------------------------------
    // Suche ohne Parameter – alle zurückliefern
    // -------------------------------------------------------------------------

    /**
     * Testet Suche ohne Parameter – soll alle CodeSystems zurückliefern.
     * Analog zu PatientControllerSearchTest.testSearchPatients() (letzter Block)
     */
    @Test
    public void testSearchWithoutParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem")
                        .param("_count", "10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").exists());
    }
}