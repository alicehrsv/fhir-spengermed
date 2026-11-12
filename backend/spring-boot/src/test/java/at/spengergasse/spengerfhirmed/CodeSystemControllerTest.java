package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.config.TestSecurityConfig;
import at.spengergasse.spengerfhirmed.entity.CodeSystem;
import at.spengergasse.spengerfhirmed.repository.CodeSystemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Import(TestSecurityConfig.class)
@TestPropertySource(locations = "classpath:application-h2.properties")
public class CodeSystemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @Autowired
    CodeSystemRepository codeSystemRepository;

    // -------------------------------------------------------------------------
    // GET alle CodeSystems
    // -------------------------------------------------------------------------

    /**
     * Testet GET /api/codesystem – liefert alle CodeSystems zurück.
     * Analog zu PatientControllerTest.getAllPatients()
     */
    @Test
    public void getAllCodeSystems() {
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.get("/api/codesystem/"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // GET einzelnes CodeSystem
    // -------------------------------------------------------------------------

    /**
     * Testet GET /api/codesystem/{id} – liefert ein einzelnes CodeSystem zurück.
     * Analog zu PatientControllerTest.getAPatient()
     */
    @Test
    public void getACodeSystem() {
        // Vorbereiten: CodeSystem speichern
        CodeSystem cs = CodeSystemRepositoryTest.returnOneCodeSystem();
        cs = codeSystemRepository.save(cs);
        assertNotNull(cs.getId());

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/codesystem/" + cs.getId()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cs.getId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("kl-oegam-primarycarecode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // POST neues CodeSystem
    // -------------------------------------------------------------------------

    /**
     * Testet POST /api/codesystem – legt ein neues CodeSystem an.
     * Analog zu PatientControllerTest.postAPatient()
     */
    @Test
    public void postACodeSystem() {
        CodeSystem cs = CodeSystemRepositoryTest.returnOneCodeSystem();
        String json = null;
        try {
            json = om.writeValueAsString(cs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/api/codesystem/")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("kl-oegam-primarycarecode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // PUT bestehendes CodeSystem aktualisieren
    // -------------------------------------------------------------------------

    /**
     * Testet PUT /api/codesystem/{id} – aktualisiert ein bestehendes CodeSystem.
     * Analog zu PatientControllerTest.putAPatient()
     */
    @Test
    public void putACodeSystem() {
        // Speichern
        CodeSystem cs = CodeSystemRepositoryTest.returnOneCodeSystem();
        cs.setId(null);
        CodeSystem savedCs = codeSystemRepository.save(cs);
        assertNotNull(savedCs);

        // Ändern
        savedCs.setVersion("2.0.0");
        savedCs.setStatus(CodeSystem.PublicationStatus.retired);

        String json = null;
        try {
            json = om.writeValueAsString(savedCs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.put("/api/codesystem/" + savedCs.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.version").value("2.0.0"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("retired"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    // -------------------------------------------------------------------------
    // DELETE CodeSystem
    // -------------------------------------------------------------------------

    /**
     * Testet DELETE /api/codesystem/{id} – löscht ein CodeSystem.
     * Analog zu PatientControllerTest.deleteAPatient()
     */
    @Test
    public void deleteACodeSystem() {
        CodeSystem cs = CodeSystemRepositoryTest.returnOneCodeSystem();
        CodeSystem savedCs = codeSystemRepository.save(cs);

        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.delete("/api/codesystem/" + savedCs.getId()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // Validierung – ungültige Eingaben
    // -------------------------------------------------------------------------

    /**
     * Testet POST mit fehlendem Pflichtfeld "status" → 400 Bad Request erwartet.
     * status ist 1..1 (required) laut FHIR CodeSystem Spezifikation.
     */
    @Test
    public void postInvalidCodeSystemMissingStatus() {
        CodeSystem cs = CodeSystemRepositoryTest.returnOneCodeSystem();
        cs.setStatus(null); // status ist Pflichtfeld (1..1)

        String json = null;
        try {
            json = om.writeValueAsString(cs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/api/codesystem/")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Testet POST mit fehlendem Pflichtfeld "content" → 400 Bad Request erwartet.
     * content ist 1..1 (required) laut FHIR CodeSystem Spezifikation.
     */
    @Test
    public void postInvalidCodeSystemMissingContent() {
        CodeSystem cs = CodeSystemRepositoryTest.returnOneCodeSystem();
        cs.setContent(null); // content ist Pflichtfeld (1..1)

        String json = null;
        try {
            json = om.writeValueAsString(cs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/api/codesystem/")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}