package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
@Entity
@Table(name = "c_contact")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Contact extends BakcboneElement{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "co_c_id", referencedColumnName = "id", nullable = true)
    private CodeableConcept purpose;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hn_c_id", referencedColumnName = "id", nullable = true)
    private HumanName name;
}
