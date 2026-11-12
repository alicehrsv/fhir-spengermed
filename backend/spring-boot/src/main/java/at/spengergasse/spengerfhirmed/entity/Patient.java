package at.spengergasse.spengerfhirmed.entity;

import at.spengergasse.spengerfhirmed.Validator.SVNRValid;
import at.spengergasse.spengerfhirmed.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pa_patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@SVNRValid

public class Patient extends DomainResource {
    public enum GenderCode {
        male, female, other, unknown
    }


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_p_id", referencedColumnName = "id")
    private List<Identifier> identifier = new ArrayList<Identifier>();


    @Column(name = "p_active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hn_p_id", nullable = true, referencedColumnName = "id")
    private List<HumanName> name = new ArrayList<HumanName>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "c_p_id", referencedColumnName = "id")
    private List<ContactPoint> telecom = new ArrayList<ContactPoint>();

    @Enumerated(EnumType.STRING)
    @Column(name = "p_gender")
    private GenderCode gender;

    @Column(name = "p_birthdate")
    private LocalDate birthDate;

    @Column(name = "p_deceasedboolean")
    private Boolean deceasedBoolean;

    @Column(name = "p_deceaseddatetime")
    private LocalDateTime deceasedDateTime;


    @AssertTrue(message = "Deceased fields cannot both be set.")
    public boolean isDeceasedValid() {
        return !(deceasedBoolean != null && deceasedBoolean && deceasedDateTime != null);
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "a_p_id", referencedColumnName = "id")
    private List<Adress> adresses = new ArrayList<Adress>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "at_pa_id", referencedColumnName = "id")
    private List<Atachment> photo = new ArrayList<Atachment>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "re_pa_id", referencedColumnName = "id")
    private List<Reference> reference = new ArrayList<Reference>();


}