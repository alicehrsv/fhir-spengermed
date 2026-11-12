package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cc_codeableconcept")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class    CodeableConcept extends Element{
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_coding_id")
    private List<Coding> coding = new ArrayList<Coding>();
    @Column(name = "cc_text")
    private String text;


}

