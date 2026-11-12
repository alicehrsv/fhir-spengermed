package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.controller.OrganizationController;
import at.spengergasse.spengerfhirmed.entity.Practioner;
import at.spengergasse.spengerfhirmed.entity.Organization;
import at.spengergasse.spengerfhirmed.repository.OrganizationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrganizationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationController organizationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(organizationController).build();
    }

    @Test
    public void testGetAllPractitioners() throws Exception {
        mockMvc.perform(get("/api/organization"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPractitioner() throws Exception {
        Organization organization = new Organization();
        organization.setId("1");
        when(organizationRepository.findById(anyString())).thenReturn(Optional.of(organization));

        mockMvc.perform(get("/api/organization/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void testCreatePractitioner() throws Exception {
        Organization organization = new Organization();
        organization.setId("1");
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        mockMvc.perform(post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(organization)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/organization"));
    }

    @Test
    public void testUpdatePractitioner() throws Exception {
        Organization organization = new Organization();
        organization.setId("1");
        when(organizationRepository.findById(anyString())).thenReturn(Optional.of(organization));
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        mockMvc.perform(put("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(organization)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void testDeletePractitioner() throws Exception {
        Organization organization = new Organization();
        organization.setId("1");
        when(organizationRepository.findById(anyString())).thenReturn(Optional.of(organization));
        doNothing().when(organizationRepository).delete(any(Organization.class));

        mockMvc.perform(delete("/api/organization/1"))
                .andExpect(status().isOk());
    }
}