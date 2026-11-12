package at.spengergasse.spengerfhirmed.entity;

import at.spengergasse.spengerfhirmed.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "s_substance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Substance extends DomainResource {

    public enum statuscode {
        active, inactive, entered_in_error
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_s_id", referencedColumnName = "id", nullable = true)
    private List<Identifier> identifier = new ArrayList<>();

    @Column(name = "s_instance")
    private Boolean instanceBoolean;

    @Enumerated(EnumType.STRING)
    @Column(name = "s_status",  nullable = true)
    private statuscode status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_s_id", referencedColumnName = "id",  nullable = true)
    private List<CodeableConcept> category = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "s_code", nullable = false)
    private CodeableReference code;

    @Column(name = "s_description")
    private String description;

    @Column(name = "s_expiry")
    private LocalDateTime expirydate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "s_quantity")
    private Quantity quantity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "in_s_id", referencedColumnName = "id")
    private List<SubstanceIngredient> ingredient = new ArrayList<>();
}