package at.spengergasse.spengerfhirmed;
import at.spengergasse.spengerfhirmed.config.TestSecurityConfig;
import at.spengergasse.spengerfhirmed.entity.PLF5A22;
import at.spengergasse.spengerfhirmed.model.Narrative;
import at.spengergasse.spengerfhirmed.entity.PLF5A22;
import at.spengergasse.spengerfhirmed.repository.PLF5A22Repository;
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
public class PLF5A22ControllerTest {





    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper om;
    @Autowired
    PLF5A22Repository plf5A22Repository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ResourceLoader resourceLoader;
//Erster Test, um alle PLF5A22en unter der URL /api/PLF5A22 mit GET

    //andExpect überprüft, ob der zurückgegebene Status 200 (OK) ist.
    @Test
    public void
    getAllPLF5A22s() {
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.get("/api/plf5A22/"))
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
    public void getAPLF5A22() {
        // Vorbereiten eines PLF5A22en mit der ID 1
        PLF5A22 plf5A22 = new PLF5A22();
        plf5A22 = plf5A22Repository.save(plf5A22); // Speichern in der Testdatenbank

        assertNotNull(plf5A22.getId()); // Sicherstellen, dass die ID generiert wurde

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/plf5A22/" + plf5A22.getId()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void
    PLF5A22() {
        PLF5A22 plf5A22 = PLF5A22Repository.returnOnePLF5A22();
        String json = null;
        try {
            json = om.writeValueAsString(plf5A22);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/api/plf5A22/")
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
    putAPLF5A22() {

        PLF5A22 plf5A22save =
                PLF5A22RepositoryTest.returnOnePLF5A22JSON(resourceLoader, om);
        plf5A22Save.setId(null);
        PLF5A22 plf5A22 = plf5A22Repository.save(plf5A22Save);
        assertNotNull(plf5A22);

        plf5A22save.setPolicy(!plf5A22save());
        plf5A22save.setWhy(PLF5A22.markdown);
        String json = null;
        try {
            json = om.writeValueAsString(plf5A22);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.put("/api/plf5A22/" + plf5A22.getId())
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

    //Der PLF5A22 wird geöscht.
//Die id muss es bereits geben.
//Erwartete Antwort ist 200 (OK)
    @Test
    public void
    deleteAPLF5A22() {
        PLF5A22 p = PLF5A22RepositoryTest.returnOnePLF5A22();
        PLF5A22 pWithId = plf5A22Repository.save(p);
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.delete("/api/plf5A22/" + pWithId.getId()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void
    postInvalidDeceasedPLF5A22() {
        PLF5A22 plf5A22 = PLF5A22RepositoryTest.returnOnePLF5A22();
//plf5A22.setDeceasedDateTime(LocalDateTime.now());
        plf5A22.Agent(LocalDateTime.now());
        plf5A22.setEntity(true);
        String json = null;
        try {
            json = om.writeValueAsString(plf5A22);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/api/plf5A22/")
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
    postInvalidNarrativePLF5A22() {
        PLF5A22 plf5A22 = PLF5A22RepositoryTest.returnOnePLF5A22();
        Narrative narrative = new Narrative();
        narrative.setStatus(null);
        narrative.setDiv(null);
        plf5A22.setText(narrative);
        String json = null;
        try {
            json = om.writeValueAsString(plf5A22);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/api/plf5A22/")
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
    public void testSearchPLF5A22s() throws Exception {
        // Arrange - save test plf5A22
        PLF5A22 testPLF5A22 = PLF5A22RepositoryTest.returnOnePLF5A22JSON(resourceLoader, om);
        testPLF5A22.setId(null); // ID sollte null sein, damit eine neue generiert wird
        plf5A22Repository.save(testPLF5A22);

        // Act & Assert - search by family name
        mockMvc.perform(MockMvcRequestBuilders.get("/api/plf5A22")
                        .param("family", "Chalmers")
                        .param("_count", "10")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").isArray());

        // Search by gender
        mockMvc.perform(MockMvcRequestBuilders.get("/api/plf5A22")
                        .param("policy", "Http..."))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(Matchers.greaterThanOrEqualTo(0)));

        // Search by birthdate
        mockMvc.perform(MockMvcRequestBuilders.get("/api/plf5A22")
                        .param("why", "das ereigns..."))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"));

        // Search with pagination
        mockMvc.perform(MockMvcRequestBuilders.get("/api/plf5A22")
                        .param("_count", "5")
                        .param("_page", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.link[?(@.relation=='self')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entry").isArray());

        // Search without parameters - should return all plf5A22s
        mockMvc.perform(MockMvcRequestBuilders.get("/api/plf5A22")
                        .param("_count", "10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("searchset"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").exists());
    }



}

