package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.entity.Organization;
import at.spengergasse.spengerfhirmed.entity.Patient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="i_identifier")
@SuperBuilder
public class Identifier extends Element{

    public enum UseCode{
        usual, official, temp, secondary, old
    }
    @Enumerated(EnumType.STRING)
    @Column(name="i_use")
    private UseCode use;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="i_cc_id")
    private CodeableConcept type;
    @Column(name="i_system")
    private String system;
    @Column(name="i_value")
    private String value;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_pe_id", referencedColumnName = "id")
    private Period period;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_o_id", referencedColumnName = "id")
    private Organization organozation;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_re_id", referencedColumnName = "id")
    private Reference assigner;
    @ManyToOne
    @JoinColumn(name="i_p_id",referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Patient patient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(use, that.use) &&
                Objects.equals(type, that.type) &&
                Objects.equals(system, that.system) &&
                Objects.equals(value, that.value) &&
                Objects.equals(period, that.period) &&
                Objects.equals(assigner, that.assigner);
    }

}