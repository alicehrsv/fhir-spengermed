package at.spengergasse.spengerfhirmed;


import at.spengergasse.spengerfhirmed.entity.Organization;
import at.spengergasse.spengerfhirmed.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    public static void returnOneOrganization() {
    }

    @Test
    void testSaveAndFindOrganization() {
        // Create organization
        Organization organization = new Organization();
        organization.setActive(true);
        organization.setName("Alice");


        // Save the organization
        Organization savedOrganization = organizationRepository.save(organization);

        // Retrieve the organization by ID
        // Wenn die ID ein Long ist:
        // Korrektur: Stelle sicher, dass die ID als String behandelt wird.
        Organization foundOrganization = organizationRepository.findById(savedOrganization.getId()).orElse(null);

        // Assert that the organization is the same
        assertEquals(savedOrganization.getId(), foundOrganization.getId());
        assertEquals(savedOrganization.getName(), foundOrganization.getName());
    }
}