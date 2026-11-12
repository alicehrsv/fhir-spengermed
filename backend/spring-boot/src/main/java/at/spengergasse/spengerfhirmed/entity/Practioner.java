package at.spengergasse.spengerfhirmed.entity;

import at.spengergasse.spengerfhirmed.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pr_practioner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Practioner extends DomainResource {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pr_id")
    public List<Identifier> identifier = new ArrayList<Identifier>();

    @Column(name = "pr_active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pr_hn_id")
    private List<HumanName> name = new ArrayList<HumanName>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="cp_pr_id", referencedColumnName = "id")
    private List<ContactPoint> telecom = new ArrayList<ContactPoint>();

    @Column(name = "pr_gender")
    private String gender;

    @Column(name = "pr_birthdate")
    private Date birthDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name= "a_pr_id", referencedColumnName = "id")
    private List<Adress> address = new ArrayList<Adress>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="at_pr_id", referencedColumnName = "id")
    private List<Atachment> attachment = new ArrayList<Atachment>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "qu_pr_id", referencedColumnName = "id")
    private List<Qualification> qualification = new ArrayList<Qualification>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_pr_communication",  referencedColumnName = "id")
    private List<CodeableConcept> communication = new ArrayList<CodeableConcept>();

    public void setBirthDate(LocalDate of) {
    }
}
