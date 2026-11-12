package at.spengergasse.spengerfhirmed;
import at.spengergasse.spengerfhirmed.entity.CodeSystem;

import at.spengergasse.spengerfhirmed.model.*;
import at.spengergasse.spengerfhirmed.repository.CodeSystemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CodeSystemRepositoryTest {

    @Autowired
    CodeSystemRepository codeSystemRepository;

    // -------------------------------------------------------------------------
    // Hilfsmethoden – analog zu PatientRepositoryTest.returnOnePatient()
    // -------------------------------------------------------------------------

    /**
     * Erstellt ein vollständiges CodeSystem-Objekt mit allen Feldern befüllt.
     * Bildet die Excel-Daten aus ICPC2_SNOMED_ICD10_Ontologie_Ausgangslage.xlsx ab.
     * Header-Werte kommen aus dem "Hinweise"-Blatt.
     */
    public static CodeSystem returnOneCodeSystem() {

        // --- CodeSystem.property (Spaltendefinitionen) ---
        List<CodeSystemProperty> properties = new ArrayList<>();

        properties.add(CodeSystemProperty.builder()
                .code(CodeCode.SNOMEDCTID)
                .description("Tatsächliche SNOMED CT ID")
                .type(CodeSystemProperty.PropertyType.code)
                .build());

        properties.add(CodeSystemProperty.builder()
                .code(CodeCode.SNOMEDCTBEGRIFFE)
                .description("FSN SNOMED CT International Browser Bezeichnung")
                .type(CodeSystemProperty.PropertyType.string)
                .build());

        properties.add(CodeSystemProperty.builder()
                .code(CodeCode.ICPC2ID)
                .description("ICPC2 Code")
                .type(CodeSystemProperty.PropertyType.string)
                .build());

        properties.add(CodeSystemProperty.builder()
                .code(CodeCode.KRITERIEN)
                .description("Kriterien")
                .type(CodeSystemProperty.PropertyType.string)
                .build());

        properties.add(CodeSystemProperty.builder()
                .code(CodeCode.HINWEISE)
                .description("Hinweise")
                .type(CodeSystemProperty.PropertyType.string)
                .build());

        properties.add(CodeSystemProperty.builder()
                .code(CodeCode.FARBE)
                .description("Farbe als Integer-Wert (1=Symptome hellgrün, 2=Prozeduren blaugrau, ...)")
                .type(CodeSystemProperty.PropertyType.integer)
                .build());

        properties.add(CodeSystemProperty.builder()
                .code(CodeCode.KOMPONENTE)
                .description("Komponente als Integer-Wert (1=Symptome, 2=Diagnostik, ...)")
                .type(CodeSystemProperty.PropertyType.integer)
                .build());

        properties.add(CodeSystemProperty.builder()
                .code(CodeCode.ICD10BEGRIFFE)
                .description("ICD-10 Begriff")
                .type(CodeSystemProperty.PropertyType.string)
                .build());

        // --- Concept 1: "Diffuser Schmerz" (Zeile 1 aus Excel) ---
        // Designation: Synonyme
        List<CodeSystemConceptDesignation> designations1 = new ArrayList<>();
        designations1.add(CodeSystemConceptDesignation.builder()
                .value("Unspezifischer Schmerz")
                .build());

        // Properties
        List<CodeSystemConceptProperty> conceptProperties1 = new ArrayList<>();
        conceptProperties1.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.SNOMEDCTID)
                .valueCode("2134003")
                .build());
        conceptProperties1.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.SNOMEDCTBEGRIFFE)
                .valueString("Diffuse pain (finding)")
                .build());
        conceptProperties1.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.ICPC2ID)
                .valueString("A01")
                .build());
        conceptProperties1.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.KRITERIEN)
                .valueString("Schmerz, nicht näher bezeichnet")
                .build());
        conceptProperties1.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.KOMPONENTE)
                .valueInteger(1)
                .build());
        conceptProperties1.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.FARBE)
                .valueInteger(1)
                .build());
        conceptProperties1.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.ICD10BEGRIFFE)
                .valueString("Schmerz, nicht näher bezeichnet")
                .build());

        CodeSystemConcept concept1 = CodeSystemConcept.builder()
                .code("2134003")
                .display("Diffuser Schmerz")
                .designation(designations1)
                .property(conceptProperties1)
                .build();

        // --- Concept 2: "Schmerz" (Zeile 2 aus Excel) ---
        List<CodeSystemConceptProperty> conceptProperties2 = new ArrayList<>();
        conceptProperties2.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.SNOMEDCTID)
                .valueCode("22253000")
                .build());
        conceptProperties2.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.SNOMEDCTBEGRIFFE)
                .valueString("Pain (finding)")
                .build());
        conceptProperties2.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.ICPC2ID)
                .valueString("A01")
                .build());
        conceptProperties2.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.KRITERIEN)
                .valueString("Schmerz, nicht näher bezeichnet")
                .build());
        conceptProperties2.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.KOMPONENTE)
                .valueInteger(1)
                .build());
        conceptProperties2.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.FARBE)
                .valueInteger(1)
                .build());
        conceptProperties2.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.ICD10BEGRIFFE)
                .valueString("Schmerz, nicht näher bezeichnet")
                .build());

        CodeSystemConcept concept2 = CodeSystemConcept.builder()
                .code("22253000")
                .display("Schmerz")
                .designation(new ArrayList<>())
                .property(conceptProperties2)
                .build();

        // --- Concept 3: "Akuter Schmerz" (Zeile 3 aus Excel) ---
        List<CodeSystemConceptProperty> conceptProperties3 = new ArrayList<>();
        conceptProperties3.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.SNOMEDCTID)
                .valueCode("274663001")
                .build());
        conceptProperties3.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.SNOMEDCTBEGRIFFE)
                .valueString("Acute pain (finding)")
                .build());
        conceptProperties3.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.ICPC2ID)
                .valueString("A01")
                .build());
        conceptProperties3.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.KRITERIEN)
                .valueString("Schmerz, nicht näher bezeichnet")
                .build());
        conceptProperties3.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.KOMPONENTE)
                .valueInteger(1)
                .build());
        conceptProperties3.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.FARBE)
                .valueInteger(1)
                .build());
        conceptProperties3.add(CodeSystemConceptProperty.builder()
                .code(CodeCode.ICD10BEGRIFFE)
                .valueString("Schmerz, nicht näher bezeichnet")
                .build());

        CodeSystemConcept concept3 = CodeSystemConcept.builder()
                .code("274663001")
                .display("Akuter Schmerz")
                .designation(new ArrayList<>())
                .property(conceptProperties3)
                .build();

        // --- CodeSystem zusammenbauen (Header aus Excel "Hinweise"-Blatt) ---
        return CodeSystem.builder()
                .url("https://termgit.elga.gv.at/CodeSystem/kl-oegam-primarycarecode")
                .version("1.0.0+20230915")
                .name("kl-oegam-primarycarecode")
                .title("KL ÖGAM PrimaryCareCodes")
                .status(CodeSystem.PublicationStatus.active)
                .description("The valueset kl-oegam-primarycarecode is intended to provide the " +
                        "technical and scientific basis for indirect coding in primary care.")
                .copyright("Copyright by Dr. Helmut Dultinger und Dr. Christoph Powondra, " +
                        "Karl Landsteiner Privatuniversität für Gesundheitswissenschaften.")
                .content(CodeSystem.CodeSystemContentMode.supplement)
                .supplements("http://snomed.info/sct")
                .property(properties)
                .concept(List.of(concept1, concept2, concept3))
                .build();
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    /**
     * Testet das Speichern und Laden eines CodeSystem aus der Datenbank.
     * Analog zu PatientRepositoryTest.testSaveAndLoadOnePatient()
     */
    @Test
    public void testSaveAndLoadOneCodeSystem() {
        // 1. Erstellen einer mit Daten befüllten CodeSystem-Instanz
        CodeSystem cs = returnOneCodeSystem();

        // 2. Instanz in die DB speichern
        CodeSystem savedCs = codeSystemRepository.save(cs);
        assertNotNull(savedCs.getId());

        // 3. Gespeicherte Daten aus der DB lesen
        CodeSystem loadedCs = codeSystemRepository.findById(savedCs.getId()).get();

        // 4. Vergleich des gespeicherten Objekts mit dem geladenen
        assertThat(loadedCs)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFieldsMatchingRegexes(".*id")
                .ignoringFields("id")
                .isEqualTo(cs);

        // Einzelne Pflichtfelder prüfen
        assertEquals("kl-oegam-primarycarecode", loadedCs.getName());
        assertEquals(CodeSystem.PublicationStatus.active, loadedCs.getStatus());
        assertEquals(CodeSystem.CodeSystemContentMode.supplement, loadedCs.getContent());
        assertEquals("http://snomed.info/sct", loadedCs.getSupplements());
    }

    /**
     * Testet, dass alle Property-Definitionen korrekt gespeichert werden.
     * (8 Spalten: SNOMEDCTID, SNOMEDCTBEGRIFFE, ICPC2ID, KRITERIEN,
     *  HINWEISE, FARBE, KOMPONENTE, ICD10BEGRIFFE)
     */
    @Test
    public void testCodeSystemPropertiesAreSaved() {
        CodeSystem cs = returnOneCodeSystem();
        CodeSystem savedCs = codeSystemRepository.save(cs);
        CodeSystem loadedCs = codeSystemRepository.findById(savedCs.getId()).get();

        assertNotNull(loadedCs.getProperty());
        assertEquals(8, loadedCs.getProperty().size());

        // Prüfen ob FARBE und KOMPONENTE als integer definiert sind
        boolean hasFarbe = loadedCs.getProperty().stream()
                .anyMatch(p -> p.getCode() == CodeCode.FARBE
                        && p.getType() == CodeSystemProperty.PropertyType.integer);
        boolean hasKomponente = loadedCs.getProperty().stream()
                .anyMatch(p -> p.getCode() == CodeCode.KOMPONENTE
                        && p.getType() == CodeSystemProperty.PropertyType.integer);
        assertTrue(hasFarbe, "Property FARBE mit Typ integer muss vorhanden sein");
        assertTrue(hasKomponente, "Property KOMPONENTE mit Typ integer muss vorhanden sein");
    }

    /**
     * Testet, dass Concepts (Zeilen aus der Excel) korrekt gespeichert werden.
     */
    @Test
    public void testCodeSystemConceptsAreSaved() {
        CodeSystem cs = returnOneCodeSystem();
        CodeSystem savedCs = codeSystemRepository.save(cs);
        CodeSystem loadedCs = codeSystemRepository.findById(savedCs.getId()).get();

        assertNotNull(loadedCs.getConcept());
        assertEquals(3, loadedCs.getConcept().size());

        // Prüfe erstes Concept "Diffuser Schmerz"
        CodeSystemConcept diffuserSchmerz = loadedCs.getConcept().stream()
                .filter(c -> c.getCode().equals("2134003"))
                .findFirst()
                .orElse(null);

        assertNotNull(diffuserSchmerz, "Concept mit Code 2134003 muss vorhanden sein");
        assertEquals("Diffuser Schmerz", diffuserSchmerz.getDisplay().trim());
    }

    /**
     * Testet, dass Designations (Synonyme) korrekt gespeichert werden.
     * Die 8 Synonym-Spalten aus der Excel werden als concept.designation gespeichert.
     */
    @Test
    public void testConceptDesignationsAreSaved() {
        CodeSystem cs = returnOneCodeSystem();
        CodeSystem savedCs = codeSystemRepository.save(cs);
        CodeSystem loadedCs = codeSystemRepository.findById(savedCs.getId()).get();

        // "Diffuser Schmerz" hat das Synonym "Unspezifischer Schmerz"
        CodeSystemConcept diffuserSchmerz = loadedCs.getConcept().stream()
                .filter(c -> c.getCode().equals("2134003"))
                .findFirst()
                .orElse(null);

        assertNotNull(diffuserSchmerz);
        assertFalse(diffuserSchmerz.getDesignation().isEmpty(),
                "Concept 2134003 muss mindestens eine Designation (Synonym) haben");
        assertEquals("Unspezifischer Schmerz",
                diffuserSchmerz.getDesignation().get(0).getValue());
    }

    /**
     * Testet, dass ConceptProperties (Zellenwerte) korrekt gespeichert werden.
     * Prüft valueString, valueInteger und valueCode.
     */
    @Test
    public void testConceptPropertiesAreSaved() {
        CodeSystem cs = returnOneCodeSystem();
        CodeSystem savedCs = codeSystemRepository.save(cs);
        CodeSystem loadedCs = codeSystemRepository.findById(savedCs.getId()).get();

        CodeSystemConcept diffuserSchmerz = loadedCs.getConcept().stream()
                .filter(c -> c.getCode().equals("2134003"))
                .findFirst()
                .orElse(null);

        assertNotNull(diffuserSchmerz);

        // SNOMEDCTID als valueCode
        CodeSystemConceptProperty snomedId = diffuserSchmerz.getProperty().stream()
                .filter(p -> p.getCode() == CodeCode.SNOMEDCTID)
                .findFirst().orElse(null);
        assertNotNull(snomedId);
        assertEquals("2134003", snomedId.getValueCode());

        // ICPC2ID als valueString
        CodeSystemConceptProperty icpc2 = diffuserSchmerz.getProperty().stream()
                .filter(p -> p.getCode() == CodeCode.ICPC2ID)
                .findFirst().orElse(null);
        assertNotNull(icpc2);
        assertEquals("A01", icpc2.getValueString());

        // FARBE als valueInteger
        CodeSystemConceptProperty farbe = diffuserSchmerz.getProperty().stream()
                .filter(p -> p.getCode() == CodeCode.FARBE)
                .findFirst().orElse(null);
        assertNotNull(farbe);
        assertEquals(1, farbe.getValueInteger());

        // KOMPONENTE als valueInteger
        CodeSystemConceptProperty komponente = diffuserSchmerz.getProperty().stream()
                .filter(p -> p.getCode() == CodeCode.KOMPONENTE)
                .findFirst().orElse(null);
        assertNotNull(komponente);
        assertEquals(1, komponente.getValueInteger());
    }

    /**
     * Testet, dass der Header des CodeSystem korrekt gespeichert wird.
     * Werte kommen aus dem "Hinweise"-Blatt der Excel-Datei.
     */
    @Test
    public void testCodeSystemHeaderFields() {
        CodeSystem cs = returnOneCodeSystem();
        CodeSystem savedCs = codeSystemRepository.save(cs);
        CodeSystem loadedCs = codeSystemRepository.findById(savedCs.getId()).get();

        assertEquals("https://termgit.elga.gv.at/CodeSystem/kl-oegam-primarycarecode", loadedCs.getUrl());
        assertEquals("1.0.0+20230915", loadedCs.getVersion());
        assertEquals("kl-oegam-primarycarecode", loadedCs.getName());
        assertEquals("KL ÖGAM PrimaryCareCodes", loadedCs.getTitle());
        assertEquals(CodeSystem.PublicationStatus.active, loadedCs.getStatus());
        assertEquals(CodeSystem.CodeSystemContentMode.supplement, loadedCs.getContent());
        assertEquals("http://snomed.info/sct", loadedCs.getSupplements());
        assertNotNull(loadedCs.getDescription());
        assertNotNull(loadedCs.getCopyright());
    }
}