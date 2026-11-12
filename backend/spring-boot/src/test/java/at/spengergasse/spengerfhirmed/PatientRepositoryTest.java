package at.spengergasse.spengerfhirmed;

import at.spengergasse.spengerfhirmed.entity.Patient;
import at.spengergasse.spengerfhirmed.model.*;
import at.spengergasse.spengerfhirmed.repository.PatientRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PatientRepositoryTest {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    ObjectMapper om;
    @Autowired
    private ResourceLoader resourceLoader;
    @Test
    public void testSaveAndLoadOnePatient(){
//1. Erstellen einer mit Daten befüllten Patienteninstanz
        Patient p = returnOnePatientJSON(resourceLoader,om);
//2. Instanz in die DB speichern
        Patient savedP = patientRepository.save(p);
//3. Gespeicherte Daten aus der DB lesen
        Patient loadedPatient =
                patientRepository.findById(savedP.getId()).get();
//4. Vergleich des gespeicherten Objekts mit dem geladenen
//Mit assertThat werden alle Attribute von p und loadedPatient auf Gleichheit getestet
//Wenn ein Wert unterschiedlich ist, schlägt der Test fehlt
//z.B. bei p.setActive(!p.getActive()); würden wir den Wert von activeumkehren, dann würde der Test fehlschlagen
//Probiere das ruhig mal aus
//Listen können unterschiedliche Reihenfolgen haben, deshalb wird mit.ignoringCollectionOrder die Reihenfolgeprüfung ausgeschalten
        //new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        assertThat(loadedPatient)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFieldsMatchingRegexes(".*id")
                .ignoringFields("id")
                .isEqualTo(p);
//Einzelne Attribute können mit Assert auf Gleichheit getestet werden.
//Dies ist bei Tests wichtig, um ein erwartetes Ergebnis mit demtatsächlichen Wert zu vergleichen.
//z.B. wird wie folgt getestet, ob der active Wert von p true ist.Wenn nicht, schlägt der Test fehl
//Es gibt viele assert Methoden, um verschiedene Werte zu prüfen.
        assertEquals(true, p.getActive());
    }
    //Liefert ein Patientenobjekt, das in den Tests verwendet werden kann.Alle Attribute sollen Werte bekommen.
    //Auch die Collections sollen zumindest einen Wert haben.
    public static Patient returnOnePatient() {
        List<ContactPoint> telecoms = new ArrayList<>();
        List<HumanName> names = new ArrayList<>();
        names.add(HumanName.builder()
                .family("Pirker")
                .given(List.of("Simon"))
                .prefix(List.of("Mag.", "DI"))
                .suffix(List.of("Bakk.techn."))
                .period(new Period(LocalDateTime.of(2010,01,01,0,0,0),
                        LocalDateTime.of(2050,01,01,0,0,0)))
                .text("<div> Mag. DI Simon Pirker, Bakk.techn.</div>")
                .use(HumanName.UseCode.official)
                .build());
        List<Identifier> identifiers = new ArrayList<>();
        List<Coding> ccCodings = new ArrayList<>();
        ccCodings.add(Coding.builder()
                .display("Sozialversicherungsnummer")
                .code("SS")
                .build());
        identifiers.add(Identifier.builder()
                .value("0123012345")
                .type(CodeableConcept.builder()
                        .coding(ccCodings)
                        .build())
                .use(Identifier.UseCode.official)
                .build());
        List<Coding> ccCodings1 = new ArrayList<>();
        ccCodings1.add(Coding.builder()
                .display("Führerscheinnummer")
                .code("DL")
                .build());
        identifiers.add(Identifier.builder()
                .value("01234567")
                .type(CodeableConcept.builder()
                        .coding(ccCodings1)
                        .build())
                .use(Identifier.UseCode.official)
                .build());
        List<Adress> addresses = new ArrayList<>();
        List<String> addressLines = new ArrayList<>();
        addressLines.add("Simon Pirker");
        addressLines.add("Spengergasse 20");
        addressLines.add("1050 Vienna");
        addressLines.add("Austria");addresses.add(Adress.builder()
                .use(Adress.UseCode.work)
                .type(Adress.TypeCode.postal)
                .district("Margareten")
                .country("Austria")
                .city("Vienna")
                .postalcode("1050")
                .state("Vienna")
                .period(new Period(LocalDateTime.of(2010,01,01,0,0,0),
                        LocalDateTime.of(2050,01,01,0,0,0)))
                .line(addressLines)
                .text("<div>Address...</div>")
                .build());
        telecoms.add(ContactPoint.builder()
                .system(ContactPoint.SystemCode.email)
                .use(ContactPoint.UseCode.work)
                .value("pirker@spengergasse.at")
                .rank(1)
                .period(new Period(LocalDateTime.of(2010,01,01,0,0,0),
                        LocalDateTime.of(2030,01,01,0,0,0)))
                .build());
        telecoms.add(ContactPoint.builder()
                .system(ContactPoint.SystemCode.phone)
                .use(ContactPoint.UseCode.work)
                .value("01234256")
                .rank(1)
                .period(new Period(LocalDateTime.of(2010,01,01,0,0,0),
                        LocalDateTime.of(2030,01,01,0,0,0)))
                .build());
        Patient p = Patient.builder()
                .telecom(telecoms)
                .identifier(identifiers)
                .name(names)
                .gender(Patient.GenderCode.female)
                .deceasedDateTime(LocalDateTime.of(1990,01,02,15,0,0))
                .birthDate(LocalDate.of(1960,01,02))
                .adresses(addresses)
                .active(false)
                .build();
        return p;
    }
    //Alternativ zu manuell erstellten Objekten können wir aus dem JSON einObjekt erzeugen
    public static Patient returnOnePatientJSON(ResourceLoader resourceLoader,
                                               ObjectMapper om) {
        String json = "";//OM Configure
        Patient p = null;
//OM Configure
        try {
            File dataFile =
                    resourceLoader.getResource("classpath:Patient.json").getFile();
            System.out.println("File exists"+dataFile.exists());
            p = om.readValue(dataFile, Patient.class);
        } catch (Exception e) {
            System.out.println("Error reading JSON Object");
        }
        return p;
    }
}

