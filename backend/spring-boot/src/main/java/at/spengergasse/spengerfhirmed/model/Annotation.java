package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "a_annotation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Annotation extends Element {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "a_author_reference_id")
    private Reference authorReference;

    @Column(name = "a_author_string")
    private String authorString;

    @Column(name = "a_time")
    private LocalDateTime time;

    @Column(name = "a_text", columnDefinition = "TEXT", nullable = false)
    private String text;
}