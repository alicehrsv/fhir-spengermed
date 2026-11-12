package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aa_agent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class agent extends BakcboneElement{


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aa_type_cc", referencedColumnName = "id")
    private CodeableConcept type;

}
