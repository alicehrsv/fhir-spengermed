package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.aspectj.apache.bcel.classfile.Code;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ee_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class entity extends BakcboneElement {

    public enum rolecode {
        source("source"),
        ;

        rolecode(String source) {
        }
    }
    @Enumerated(EnumType.STRING)
    @Column(name="ee_code", columnDefinition = "LONGTEXT")
    private Code rolecode;

}
