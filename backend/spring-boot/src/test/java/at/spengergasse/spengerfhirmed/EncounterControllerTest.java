package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.controller.EncounterController;
import at.spengergasse.spengerfhirmed.entity.Encounter;
import at.spengergasse.spengerfhirmed.service.EncounterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EncounterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EncounterService encounterService;

    @InjectMocks
    private EncounterController encounterController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(encounterController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllEncounters() throws Exception {
        // Arrange
        Encounter encounter = new Encounter();
        encounter.setId("1");
        given(encounterService.getAllResources()).willReturn(List.of(encounter));

        // Act & Assert
        mockMvc.perform(get("/api/encounter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    public void testGetEncounterById() throws Exception {
        // Arrange
        Encounter encounter = new Encounter();
        encounter.setId("1");
        given(encounterService.getResource("1")).willReturn(Optional.of(encounter));

        // Act & Assert
        mockMvc.perform(get("/api/encounter/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void testCreateEncounter() throws Exception {
        // Arrange
        Encounter encounter = new Encounter();
        encounter.setId("1");
        given(encounterService.createResource(any(Encounter.class))).willReturn(encounter);

        // Act & Assert
        mockMvc.perform(post("/api/encounter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(encounter)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void testUpdateEncounter() throws Exception {
        // Arrange
        Encounter encounter = new Encounter();
        encounter.setId("1");
        given(encounterService.updateResource(anyString(), any(Encounter.class))).willReturn(Optional.of(encounter));

        // Act & Assert
        mockMvc.perform(put("/api/encounter/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(encounter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void testDeleteEncounter() throws Exception {
        // Arrange
        // Angenommen, deleteResource gibt einen Wert zurück (z. B. boolean oder Optional)
        when(encounterService.deleteResource("1")).thenReturn(true); // Beispiel: Rückgabewert ist boolean

        // Act & Assert
        mockMvc.perform(delete("/api/encounter/1"))
                .andExpect(status().isOk());
    }
}