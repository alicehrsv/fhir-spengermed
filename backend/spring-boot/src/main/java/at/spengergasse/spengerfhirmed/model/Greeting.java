package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.persistence.Table;
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "greeting")
@Builder
public class Greeting extends Resource {
    @Id
    private String id;
    private String name;
    private long counter;
    @Column(name = "age")
    private int age;



}
