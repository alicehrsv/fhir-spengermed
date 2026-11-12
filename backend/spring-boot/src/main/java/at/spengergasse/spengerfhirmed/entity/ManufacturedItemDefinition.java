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
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ma_manufactureditemdef")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@SVNRValid

public class ManufacturedItemDefinition extends DomainResource {
    public enum code {
        draft, active, retired, unknown
    }


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_ma_id", referencedColumnName = "id")
    private List<Identifier> identifier = new ArrayList<Identifier>();


    @Enumerated(EnumType.STRING)
    @Column(name = "ma_status")
    private code status;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="manufacturedDoseForm",nullable = false,referencedColumnName = "id")
    private CodeableConcept manufacturedDoseForm;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pr_ma_id", referencedColumnName = "id")
    private List<Property> property = new ArrayList<Property>();




}