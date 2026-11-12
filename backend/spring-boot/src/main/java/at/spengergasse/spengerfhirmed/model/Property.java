package at.spengergasse.spengerfhirmed.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Table(name = "pr_property")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Property extends BakcboneElement{


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "co_pr_id", referencedColumnName = "id", nullable = false)
    private CodeableConcept type;


    @Column(name= "valueDate")
    private Date valueDate;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="valueCodeableConcept",referencedColumnName = "id")
    private CodeableConcept valueCodeableConcept;


    @AssertTrue(message = "nicht beide dürfen gesetzt sein.")
    public boolean value() {
        return valueDate == null || valueCodeableConcept == null;

    }

}
