package at.spengergasse.spengerfhirmed.entity;

import at.spengergasse.spengerfhirmed.model.Adress;
import at.spengergasse.spengerfhirmed.model.Contact;
import at.spengergasse.spengerfhirmed.model.DomainResource;
import at.spengergasse.spengerfhirmed.model.Identifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "or_organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Organization extends DomainResource {


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_o_id", referencedColumnName = "id")
    private List<Identifier> identifier = new ArrayList<Identifier>();


    @Column(name = "o_active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "a_p_id", referencedColumnName = "id")
    private List<Adress> adresses = new ArrayList<Adress>();

    @Column(name = "o_name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "c_o_p", referencedColumnName = "id")
    private List<Contact> contact = new ArrayList<Contact>();

}
