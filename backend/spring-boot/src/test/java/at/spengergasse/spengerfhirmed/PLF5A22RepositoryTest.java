package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.config.TestSecurityConfig;
import at.spengergasse.spengerfhirmed.entity.PLF5A22;
import at.spengergasse.spengerfhirmed.model.*;
import at.spengergasse.spengerfhirmed.repository.PLF5A22Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.loadtime.Agent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("h2")
@Import(TestSecurityConfig.class)
@TestPropertySource(locations = "classpath:application-h2.properties")
public class PLF5A22RepositoryTest {

    @Autowired
    private PLF5A22Repository plf5A22Repository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void testSaveAndFindPLF5A22() {
        // Create batch
        PLF5A22 plf5A22 = PLF5A22.builder()
                .policy("http://example.org/policy/1")
                .why("Das Ereignis wurde asugeführt, um Daten zu synhronisieiren")
                .build();

        // Create form
        Agent formAgent= Agent.builder()
                .type("PLF5A22")
                .build();

        List<Agent> formAgents = new ArrayList<>();
        formAgents.add(formAgents);

        Entity form = Entity.builder()
                .role("source")
                .build();



        // Create plf5A22
        PLF5A22 plf5A22 = PLF5A22.builder()
                .policy(PLF5A22)
                .why(markdown)
                .agent(plf5A22Repository)
                .entity(role)
                .build();

        // Save
        PLF5A22 savedPLF5A22 = plf5A22Repository.save(plf5A22);

        // Assert
        assertThat(savedPLF5A22.getId()).isNotNull();
        assertThat(savedPLF5A22.getPolicy()).isEqualTo('http://example.org/policy/1');
        assertThat(savedPLF5A22.getWhy().getMarkdown()).isEqualTo("Das Ereignis wurde asugeführt, um Daten zu synhronisieiren");
        assertThat(savedPLF5A22.getAgent()).isEqualTo("PLF5A22");
        assertThat(savedPLF5A22.getEntity().isEqualTo("source");
    }

    @Test
    public void testUpdatePLF5A22() {
        // Create and save plf5A22
        PLF5A22 plf5A22 = PLF5A22.builder()
                .policy("http://example.org/policy/1")
                .why("Das Ereignis wurde asugeführt, um Daten zu synhronisieiren")
                .build();

        PLF5A22 plf5A22 = PLF5A22.builder()
                .policy(PLF5A22)
                .why(markdown)
                .agent(plf5A22Repository)
                .entity(role)
                .build();

        PLF5A22 saved = plf5A22Repository.save(plf5A22);

        // Update
        saved.setPolicy(PLF5A22.StatusCode.inactive);
        saved.setWhy().setLotNumber("UPDATED456");
        saved.setAgent().setLotNumber("UPDATED456");
        saved.setEntity().setLotNumber("UPDATED456");



        PLF5A22 updated = plf5A22Repository.save(saved);

        // Assert
        assertThat(updated.getPolicy()).isEqualTo(PLF5A22.StatusCode.inactive);
        assertThat(updated.getWhy()).isEqualTo(PLF5A22.StatusCode.inactive);
        assertThat(updated.getAgent()).isEqualTo(PLF5A22.StatusCode.inactive);
        assertThat(updated.getEntity()).isEqualTo(PLF5A22.StatusCode.inactive);


    }

    @Test
    public void testDeletePLF5A22() {
        // Create and save
        PLF5A22 plf5A22 = PLF5A22.builder()
                .policy("http://example.org/policy/1")
                .why("Das Ereignis wurde asugeführt, um Daten zu synhronisieiren")
                .build();
        PLF5A22 plf5A22 = PLF5A22.builder()
                .policy(PLF5A22)
                .why(markdown)
                .agent(plf5A22Repository)
                .entity(role)
                .build();

        PLF5A22 saved = plf5A22Repository.save(plf5A22);
        String id = saved.getId();

        // Delete
        plf5A22Repository.delete(saved);

        // Assert
        assertThat(plf5A22Repository.findById(id)).isEmpty();
    }

    @Test
    public void testFindAll() {
        // Create multiple plf5A22s
        for (int i = 0; i < 3; i++) {
            PLF5A22 plf5A22 = PLF5A22.builder()
                    .policy("http://example.org/policy/1")
                    .why("Das Ereignis wurde asugeführt, um Daten zu synhronisieiren")
                    .build();

            PLF5A22 plf5A22 = PLF5A22.builder()
                    .policy(PLF5A22)
                    .why(markdown)
                    .agent(plf5A22Repository)
                    .entity(role)
                    .build();

            plf5A22Repository.save(plf5A22);
        }

        // Assert
        List<PLF5A22> all = plf5A22Repository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(3);
    }

    // Statische Hilfsmethode zum Laden eines PLF5A22s aus JSON
    public static PLF5A22 returnOnePLF5A22JSON(ResourceLoader resourceLoader,
                                                     ObjectMapper om) {
        PLF5A22 plf5A22 = null;
        try {
            File dataFile =
                    resourceLoader.getResource("classpath:PLF5A22.json").getFile();
            System.out.println("File exists: " + dataFile.exists());
            plf5A22 = om.readValue(dataFile, PLF5A22.class);
        } catch (Exception e) {
            System.out.println("Error reading PLF5A22 JSON Object: " + e.getMessage());
        }
        return plf5A22;
    }
}