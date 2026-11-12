package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ExtendedContactDetail extends Element {



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ecd_purpose_id", referencedColumnName = "id")
    private CodeableConcept purpose;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ecd_name_id", referencedColumnName = "id")
    private List<HumanName> name = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ecd_telecom_id", referencedColumnName = "id")
    private List<ContactPoint> telecom = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ecd_address_id", referencedColumnName = "id")
    private Adress address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ecd_organization_id", referencedColumnName = "id")
    private Reference organization;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ecd_period_id", referencedColumnName = "id")
    private Period period;
}