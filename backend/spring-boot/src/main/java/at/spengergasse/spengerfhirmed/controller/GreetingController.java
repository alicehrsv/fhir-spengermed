package at.spengergasse.spengerfhirmed.controller;

import at.spengergasse.spengerfhirmed.model.Greeting;
import at.spengergasse.spengerfhirmed.repository.GreetingRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

//Erkläre mir was in dieser Klasse passiert bitte - ich verstehe es nicht
//Die Klasse GreetingController ist ein RestController, der die Anfragen an die Datenbank weiterleitet.
//Es gibt zwei Methoden, die aufgerufen werden können: greeting und updategreeting. greeting nimmt einen Parameter id entgegen und gibt ein Greeting-Objekt zurück. Dieses Objekt wird in der Datenbank gesucht und falls es nicht gefunden wird, wird ein neues Objekt erstellt. Das Objekt wird dann um 1 erhöht und in der Datenbank gespeichert. updategreeting nimmt die Parameter id, name und age entgegen und gibt ein Greeting-Objekt zurück. Das Objekt wird in der Datenbank gesucht und falls es nicht gefunden wird, wird ein neues Objekt erstellt. Das Objekt wird dann mit den neuen Werten aktualisiert und in der Datenbank gespeichert. Es gibt auch eine Methode greetings, die alle Greetings mit einem bestimmten counter-Wert zurückgibt. Diese Methode gibt eine Liste von Greeting-Objekten zurück, die in der Datenbank nach dem counter-Wert sortiert sind.
//Die Klasse verwendet das GreetingRepository, um auf die Datenbank zuzugreifen. Das GreetingRepository ist eine Schnittstelle, die von Spring Data JPA automatisch implementiert wird. Es enthält Methoden zum Speichern, Aktualisieren, Löschen und Suchen von Greeting-Objekten in der Datenbank.
//Die Klasse verwendet auch die Annotationen @Autowired, @GetMapping und @PutMapping, um
//Was ist ein RestController?
//Ein RestController ist eine Klasse, die HTTP-Anfragen verarbeitet und HTTP-Antworten zurückgibt. Es wird verwendet, um RESTful Web Services zu implementieren, die CRUD-Operationen auf Ressourcen durchführen. Ein RestController verwendet Annotationen wie @GetMapping, @PostMapping, @PutMapping und @DeleteMapping, um die verschiedenen HTTP-Methoden zu definieren, die von der Klasse unterstützt werden.
//Was ist eine Annotation?
//Eine Annotation ist eine spezielle Art von Metadaten, die in Java-Code verwendet wird, um zusätzliche Informationen über Klassen, Methoden, Variablen und anderen Elementen des Codes bereitzustellen. Annotationen werden mit dem @-Zeichen vor dem Namen der Annotation geschrieben und können verwendet werden, um Konfigurationen, Verhalten und Dokumentation im Code zu definieren.
//Was ist @Autowired?
//Die @Autowired-Annotation wird verwendet, um eine Abhängigkeit in Spring zu injizieren. Wenn eine Klasse eine andere Klasse benötigt, um ihre Funktionalität auszuführen, kann die @Autowired-Annotation verwendet werden, um die benötigte Klasse automatisch zu injizieren. Spring sucht nach
//Was ist @GetMapping?
//Die @GetMapping-Annotation wird verwendet, um eine Methode in einem RestController zu definieren, die auf eine HTTP-GET-Anfrage reagiert. Wenn ein Client eine GET-Anfrage an die URL der Methode sendet, wird die Methode ausgeführt und die Rückgabe der Methode wird als HTTP-Antwort zurückgegeben.
//Was ist @PutMapping?
//
@RestController
public class GreetingController {
    //was passiert hier bei Autowired?
    //Die @Autowired-Annotation wird verwendet, um eine Abhängigkeit in Spring zu injizieren. In diesem Fall wird das GreetingRepository injiziert, um auf die Datenbank zuzugreifen.
    @Autowired
    GreetingRepository greetingRepository;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    // RequestParam holt die daten ausu der url
    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "id", required = true) Long id) {
        Optional<Greeting> optionalGreeting = greetingRepository.findById(id);
        Greeting greeting = new Greeting(String.valueOf(id), "World", 0, 150);
        if (optionalGreeting.isPresent()) {
            greeting = optionalGreeting.get();
        }
        greeting.setCounter(greeting.getCounter() + 1);
        return greetingRepository.save(greeting);
    }

    @PutMapping("/greeting")
    public Greeting updategreeting(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "age", required = false) int age) {
        Optional<Greeting> optionalGreeting = greetingRepository.findById(id);
        Greeting greeting = new Greeting(String.valueOf(id), name, 0, age);
        if (optionalGreeting.isPresent()) {
            greeting = optionalGreeting.get();
            greeting.setName(name);
            greeting.setAge(age);
        }
        return greetingRepository.save(greeting);
    }


//Weiteres Get um alle Greetings nach einem counterwert anzufragen
// absteigend nach id sortiert
@GetMapping("/greetings/counter")
    public List<Greeting> greetings(@RequestParam(value = "counter", required = true) long counter) {
    return greetingRepository.findGreetingByCounterOrderByIdDesc(counter);
}
}
