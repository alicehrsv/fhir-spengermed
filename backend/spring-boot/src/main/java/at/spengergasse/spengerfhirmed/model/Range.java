package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "r_range")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Range extends Element {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_low_id")
    private SimpleQuantity low;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_high_id")
    private SimpleQuantity high;
}